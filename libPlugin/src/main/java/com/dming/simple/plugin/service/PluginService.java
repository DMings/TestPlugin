package com.dming.simple.plugin.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import com.dming.simple.PluginManager;
import com.dming.simple.utils.DLog;
import com.dming.simple.utils.ReflectUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

public class PluginService extends Service {

    private Service mClientService;
    private Class serviceClass = Service.class;

    Method ON_CREATE = ReflectUtils.getMethod(serviceClass, "onCreate");
    Method ON_START_COMMAND = ReflectUtils.getMethod(serviceClass, "onStartCommand", Bundle.class,int.class,int.class);
    Method ON_BIND = ReflectUtils.getMethod(serviceClass, "onBind",Intent.class);
    Method ON_UNBIND = ReflectUtils.getMethod(serviceClass, "onUnbind",Intent.class);
    Method ON_REBIND = ReflectUtils.getMethod(serviceClass, "onRebind",Intent.class);
    Method ON_TASK_REMOVED = ReflectUtils.getMethod(serviceClass, "onTaskRemoved",Intent.class);
    Method ON_DESTROY = ReflectUtils.getMethod(serviceClass, "onDestroy");

    @Override
    public void onCreate() {
        super.onCreate();
        DLog.i("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String className = intent.getStringExtra("ClassName");
        createService(className);
        callTargetServiceMethod(ON_CREATE);
        return (int) callTargetServiceMethod(ON_START_COMMAND,intent,flags,startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return (IBinder) callTargetServiceMethod(ON_BIND,intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return (boolean) callTargetServiceMethod(ON_UNBIND,intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        callTargetServiceMethod(ON_REBIND,intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        callTargetServiceMethod(ON_TASK_REMOVED,rootIntent);
    }

    @Override
    public void onDestroy() {
        if (PluginManager.sClassLoader != null) {
            PluginManager.clearServicePit(this.getClass().getName());
        }
        super.onDestroy();
        callTargetServiceMethod(ON_DESTROY);
    }

    private void createService(String className) {
        try {
            Class loadClass = PluginManager.sClassLoader.loadClass(className);
            Object act = loadClass.newInstance();
            if (act instanceof Service) {
                mClientService = (Service) loadClass.newInstance();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        if (mClientService == null) {
            mClientService = new Service(){

                @Override
                public IBinder onBind(Intent intent) {
                    return null;
                }
            };
        }
    }

    private Object callTargetServiceMethod(Method method, Object... args) {
        DLog.d("callTargetActivityMethod: " + Arrays.toString(args));
        try {
            if (mClientService == null) {
                throw new IllegalStateException("target activity is null");
            }
            return method.invoke(mClientService, args);
        } catch (Throwable e) {
            DLog.e("callTargetActivityMethod: " + method.getName());
            return false;
        }
    }
}
