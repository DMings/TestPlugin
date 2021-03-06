package com.dming.simple.plugin.activity;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import androidx.appcompat.view.ContextThemeWrapper;
import com.dming.simple.PluginManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PluginContext extends ContextThemeWrapper {

    private Resources mResources;
    private ApplicationInfo mApplicationInfo;
    private ClassLoader mClassLoader;
    private LayoutInflater mInflater;

    public PluginContext(Context base) {
        super(base, android.R.style.Theme);
        this.mResources = PluginManager.sResources;
        this.mApplicationInfo = PluginManager.sApplicationInfo;
        this.mClassLoader = PluginManager.sClassLoader;
    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name) && mClassLoader != null) {
            if (mInflater == null) {
                LayoutInflater inflater = (LayoutInflater) super.getSystemService(name);
                // 新建一个，设置其工厂
                mInflater = inflater.cloneInContext(this);
                mInflater.setFactory(mFactory);
            }
            return mInflater;
        }
        return super.getSystemService(name);
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
            do {
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
            } while (false);
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
        } catch (IllegalAccessException e) {
//            e.printStackTrace();
        } catch (InstantiationException e) {
//            e.printStackTrace();
        } catch (InvocationTargetException e) {
//            e.printStackTrace();
        }
//        DLog.i("handleCreateView name: " + name + " v: " + v);
        return v;
    }

    @Override
    public AssetManager getAssets() {
        if (mResources != null) {
            return mResources.getAssets();
        }
        return super.getAssets();
    }

    @Override
    public Resources getResources() {
        if (mResources != null) {
            return mResources;
        }
        return super.getResources();
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        if (mApplicationInfo != null) {
            return mApplicationInfo;
        }
        return super.getApplicationInfo();
    }

    @Override
    public ClassLoader getClassLoader() {
        if (mClassLoader != null) {
            return mClassLoader;
        }
        return super.getClassLoader();
    }

    @Override
    public ComponentName startService(Intent service) {
        if (mClassLoader != null) {
            PluginManager.startService(service);
        }
        return super.startService(service);
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        if (mClassLoader != null) {
            PluginManager.bindService(service,conn,flags);
        }
        return super.bindService(service, conn, flags);
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        if (mClassLoader != null) {
            PluginManager.unbindService(conn);
        }
        super.unbindService(conn);
    }

    @Override
    public boolean stopService(Intent name) {
        if (mClassLoader != null) {
            PluginManager.stopService(name);
        }
        return super.stopService(name);
    }

}
