package com.dming.simple;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.*;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import com.dming.simple.plugin.activity.ActPlugin;
import com.dming.simple.utils.DLog;
import com.dming.simple.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

public class SPlugin {

    private static volatile SPlugin sSPlugin;
    public FClassLoader mPlugClassLoader;
    private boolean mPatchClassLoader = false;
    private boolean mLoadPlugin = false;

    public boolean isPatchClassLoader() {
        return mPatchClassLoader;
    }

    public boolean isLoadPlugin() {
        return mPatchClassLoader && mLoadPlugin;
    }

    private void setPatchClassLoader(boolean mPatchClassLoader) {
        this.mPatchClassLoader = mPatchClassLoader;
    }

    private void setLoadPlugin(boolean mLoadPlugin) {
        this.mLoadPlugin = mLoadPlugin;
    }

    public static SPlugin getInstance() {
        if (sSPlugin == null) {
            synchronized (SPlugin.class) {
                if (sSPlugin == null) {
                    sSPlugin = new SPlugin();
                }
            }
        }
        return sSPlugin;
    }

    public static void init(Application application) {
        SPlugin sPlugin = getInstance();
        boolean b = PatchClassLoaderUtils.patch(application);
        sPlugin.setPatchClassLoader(b);
        if (b) {
            DLog.i("Patch ClassLoader success");
        } else {
            DLog.i("Patch ClassLoader false");
        }
    }

    public static void initPlugin(final Context context, final String assetName, final OnPluginInitListener onPluginInitListener) {
        final SPlugin sPlugin = getInstance();
        if (sPlugin.isPatchClassLoader()) {
            sPlugin.initPlugin(context, onPluginInitListener, new PluginRunnable() {
                @Override
                public File getApkFile() throws IOException {
                    return sPlugin.getPluginApkFromAsset(context, assetName);
                }
            });
        }
    }

    public static void initPlugin(final Context context, final File pluginApkFile, final OnPluginInitListener onPluginInitListener) {
        final SPlugin sPlugin = getInstance();
        if (sPlugin.isPatchClassLoader()) {
            sPlugin.initPlugin(context, onPluginInitListener, new PluginRunnable() {
                @Override
                public File getApkFile() throws IOException {
                    return sPlugin.getPluginApkFromFile(context, pluginApkFile);
                }
            });
        }
    }

    private void initPlugin(final Context context, final OnPluginInitListener onPluginInitListener, final PluginRunnable pluginRunnable) {
        if (isPatchClassLoader()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File apkFile = pluginRunnable.getApkFile();
                        dealPlugin(context, apkFile);
                        ActPlugin.initActivityPlugin(context, apkFile);
                        setLoadPlugin(true);
                    } catch (Exception e) {
                        setLoadPlugin(false);
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (onPluginInitListener != null) {
                                if (isLoadPlugin()) {
                                    onPluginInitListener.onSuccess();
                                } else {
                                    onPluginInitListener.onFailure();
                                }
                            }
                        }
                    });
                }
            }).start();
        }
    }


    Class<?> loadClass(String className, ClassLoader origClassLoader) {
        Class<?> clazz = null;
//        DLog.e("loadClass className: " + className);
        if (mPlugClassLoader != null) {
            String activity = ActPlugin.solveActClass(className);
            if (activity != null) {
                try {
                    clazz = mPlugClassLoader.loadPluginClass(activity);
                } catch (ClassNotFoundException e) {
                    try {
                        clazz = origClassLoader.loadClass(activity);
                    } catch (ClassNotFoundException e1) {}
                }
            }else {
                try {
                    clazz = mPlugClassLoader.loadPluginClass(className);
                } catch (ClassNotFoundException e) {}
            }
        }
        return clazz;
    }

    private void dealPlugin(Context context, File apkFile) throws PackageManager.NameNotFoundException, IOException {
        File ndkOutputDir = dealSo(context, apkFile);
        File dexOutputDir = context.getDir("dex", Activity.MODE_PRIVATE);
        FClassLoader classLoader = new FClassLoader(apkFile.getAbsolutePath(),// dexPath
                dexOutputDir.getAbsolutePath(),// optimizedDirectory
                ndkOutputDir.getAbsolutePath(),
                context.getClassLoader()
        );
        mPlugClassLoader = classLoader;
        dealHostPkgInfo(context);
        dealPluginPkgInfo(context, apkFile);
    }

    private void dealHostPkgInfo(Context context) throws PackageManager.NameNotFoundException {
        long time = System.currentTimeMillis();
        PackageManager packageManager = context.getPackageManager();
        PackageInfo pInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        DLog.i("host time>>>" + (System.currentTimeMillis() - time));
        if (pInfo != null) {
            ApplicationInfo appInfo = pInfo.applicationInfo;
            DLog.i("Host appInfo theme>" + Integer.toHexString(appInfo.theme));
            ActPlugin.obtainHostActivity(pInfo);
            ServiceInfo[] services = pInfo.services;
            for (ServiceInfo serviceInfo : services) {
                DLog.i("Host ServiceInfo>" + serviceInfo.name + " packageName: " + serviceInfo.packageName);
            }
        }
    }

    private void dealPluginPkgInfo(Context context, File apkFile) {
        String apkPath = apkFile.getAbsolutePath();
        long time = System.currentTimeMillis();
        PackageManager packageManager = context.getPackageManager();
        PackageInfo pInfo = packageManager.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES |
                        PackageManager.GET_SERVICES |
                        PackageManager.GET_RECEIVERS |
                        PackageManager.GET_PROVIDERS);
        DLog.i("SPlugin time>>>" + (System.currentTimeMillis() - time));
        if (pInfo != null) {
            ApplicationInfo appInfo = pInfo.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            //
            try {
                Resources resource = packageManager.getResourcesForApplication(appInfo);
                Class<?> pluginClass = Class.forName(PluginActivity.class.getName(), true, mPlugClassLoader);
                Field resources = pluginClass.getDeclaredField("sResources");
                Field applicationInfo = pluginClass.getDeclaredField("sApplicationInfo");
                Field theme = pluginClass.getDeclaredField("sTheme");
//                Field iActPitEvent = pluginClass.getDeclaredField("sActPitEvent");
                resources.set(null,resource);
                applicationInfo.set(null,appInfo);
                theme.set(null,resource.newTheme());
//                iActPitEvent.set(null,ActPlugin.sPitActEvent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }catch (PackageManager.NameNotFoundException e) {

            }
            DLog.i("Plugin appInfo theme>" + Integer.toHexString(appInfo.theme));
            ActPlugin.obtainPluginActivity(pInfo);

            ServiceInfo[] services = pInfo.services;
            for (ServiceInfo serviceInfo : services) {
                DLog.i("SPlugin ServiceInfo>" + serviceInfo.name + " " + serviceInfo.flags);
            }
            ActivityInfo[] receivers = pInfo.receivers;
            for (ActivityInfo receiverInfo : receivers) {
                DLog.i("SPlugin ActivityInfo receivers>" + receiverInfo.name + " " + receiverInfo.flags);
//                Class
//                BroadcastReceiverDispatch.addSubBroadcastReceiver(new SubBroadcastReceiver());
            }
            ProviderInfo[] providers = pInfo.providers;
            for (ProviderInfo providerInfo : providers) {
                DLog.i("SPlugin ProviderInfo>" + providerInfo.authority + " -> " + providerInfo.name);
            }
        }
    }

    private File dealSo(Context context, File apkFile) {
        File ndkOutputDir = context.getDir("ndk", Activity.MODE_PRIVATE);
        PluginNativeLibsHelper.install(apkFile.getAbsolutePath(), ndkOutputDir);
        File[] files = ndkOutputDir.listFiles();
        if (files != null) {
            for (File f : files)
                DLog.i("files>" + f.toString());
        }
        return ndkOutputDir;
    }

    private File getPluginApkFromAsset(Context context, String assetName) throws IOException {
        File apkFile = new File(context.getFilesDir(), "dex_" + assetName);
        InputStream inputStream = context.getAssets().open(assetName);
        FileUtils.copyInputStreamToFile(inputStream, apkFile);
        return apkFile;
    }

    private File getPluginApkFromFile(Context context, File apkFile) throws IOException {
        File destApkFile = new File(context.getFilesDir(), "dex_" + apkFile.getName());
        FileUtils.copyDir(apkFile, destApkFile);
        return destApkFile;
    }

    public interface PluginRunnable {
        File getApkFile() throws IOException;
    }

}
