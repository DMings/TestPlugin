package com.dming.simple;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import androidx.appcompat.view.ContextThemeWrapper;
import com.dming.simple.utils.DLog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PluginContext extends ContextThemeWrapper {

    private Resources mResources;
    private ApplicationInfo mApplicationInfo;
    private ClassLoader mClassLoader;
    private LayoutInflater mInflater;

    public PluginContext(Context base, Resources resources, ApplicationInfo applicationInfo, ClassLoader classLoader) {
        super(base, android.R.style.Theme);
        this.mResources = resources;
        this.mApplicationInfo = applicationInfo;
        this.mClassLoader = classLoader;
    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mInflater == null) {
                LayoutInflater inflater = (LayoutInflater) super.getSystemService(name);
                // 新建一个，设置其工厂
                mInflater = inflater.cloneInContext(this);
                mInflater.setFactory(mFactory);
//                // 再新建一个，后续可再次设置工厂
//                mInflater = mInflater.cloneInContext(this);
            }
            return mInflater;
        }
        return super.getSystemService(name);

//        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
//            DLog.i("getSystemService-: "+name);
//            if (mInflater == null) {
//                mInflater = (LayoutInflater) super.getSystemService(name);
//            }
//            return mInflater;
//        }
//        return super.getSystemService(name);
    }

    @Override
    public Context getBaseContext() {
        return super.getBaseContext();
    }

    LayoutInflater.Factory mFactory = new LayoutInflater.Factory() {

        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            return handleCreateView(name, context, attrs);
        }
    };

    private final View handleCreateView(String name, Context context, AttributeSet attrs) {
        View v = null;
        try {
            Class c = mClassLoader.loadClass(name);
            do{
                if (c == null) {
                    // 没找到，不管
                    break;
                }
                if (c == ViewStub.class) {
                    // 系统特殊类，不管
                    break;
                }
                if (c.getClassLoader() != mClassLoader) {
                    // 不是插件类，不管
                    break;
                }
                Constructor<?> construct = c.getConstructor(Context.class, AttributeSet.class);
                v = (View) construct.newInstance(context, attrs);
            }while (false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        DLog.i("handleCreateView name: " + name + " v: " + v);
        return v;
//        // 忽略表命中，返回
//        if (mLoader.mIgnores.contains(name)) {
//            // 只有开启“详细日志”才会输出，防止“刷屏”现象
////            if (LogDebug.LOG && RePlugin.getConfig().isPrintDetailLog()) {
////                LogDebug.d(PLUGIN_TAG, "layout.cache: ignore plugin=" + mPlugin + " name=" + name);
////            }
//            return null;
//        }
//
//        // 构造器缓存
//        Constructor<?> construct = mLoader.mConstructors.get(name);
//
//        // 缓存失败
//        if (construct == null) {
//            // 找类
//            Class<?> c = null;
//            boolean found = false;
//            do {
//                try {
//                    c = mClassLoader.loadClass(name);
//                    if (c == null) {
//                        // 没找到，不管
//                        break;
//                    }
//                    if (c == ViewStub.class) {
//                        // 系统特殊类，不管
//                        break;
//                    }
//                    if (c.getClassLoader() != mClassLoader) {
//                        // 不是插件类，不管
//                        break;
//                    }
//                    // 找到
//                    found = true;
//                } catch (ClassNotFoundException e) {
//                    // 失败，不管
//                    break;
//                }
//            } while (false);
//            if (!found) {
//                // 只有开启“详细日志”才会输出，防止“刷屏”现象
////                if (LogDebug.LOG && RePlugin.getConfig().isPrintDetailLog()) {
////                    LogDebug.d(PLUGIN_TAG, "layout.cache: new ignore plugin=" + mPlugin + " name=" + name);
////                }
//                mLoader.mIgnores.add(name);
//                return null;
//            }
//            // 找构造器
//            try {
//                construct = c.getConstructor(Context.class, AttributeSet.class);
////                if (LOG) {
////                    LogDebug.d(PLUGIN_TAG, "layout.cache: new constructor. plugin=" + mPlugin + " name=" + name);
////                }
//                mLoader.mConstructors.put(name, construct);
//            } catch (Exception e) {
//                InflateException ie = new InflateException(attrs.getPositionDescription() + ": Error inflating mobilesafe class " + name, e);
//                throw ie;
//            }
//        }
//
//        // 构造
//        try {
//            View v = (View) construct.newInstance(context, attrs);
//            // 只有开启“详细日志”才会输出，防止“刷屏”现象
////            if (LogDebug.LOG && RePlugin.getConfig().isPrintDetailLog()) {
////                LogDebug.d(PLUGIN_TAG, "layout.cache: create view ok. plugin=" + mPlugin + " name=" + name);
////            }
//            return v;
//        } catch (Exception e) {
//            InflateException ie = new InflateException(attrs.getPositionDescription() + ": Error inflating mobilesafe class " + name, e);
//            throw ie;
//        }
    }

    @Override
    public AssetManager getAssets() {
        DLog.i("getAssets--->");
        if (mResources != null) {
            return mResources.getAssets();
        }
        return super.getAssets();
    }

    @Override
    public Resources getResources() {
        DLog.i("getResources--->");
        if (mResources != null) {
            return mResources;
        }
        return super.getResources();
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
//        DLog.i("getApplicationInfo--->");
        if (mApplicationInfo != null) {
            return mApplicationInfo;
        }
        return super.getApplicationInfo();
    }

    @Override
    public ClassLoader getClassLoader() {
        DLog.i("getClassLoader--->");
        if (mClassLoader != null) {
            return mClassLoader;
        }
        return super.getClassLoader();
    }
}
