package com.dming.simple;

import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.Bundle;
import com.dming.simple.utils.DLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PluginManager {

    public static Resources sResources;
    public static ApplicationInfo sApplicationInfo;
    public static ClassLoader sClassLoader;
    private static Object sActPitEvent;
    private static Object sServicePitEvent;
    private static Object sRecPitEvent;

    public static void setPicEvent(Object actObj, Object serviceObj,Object recObj) {
        sActPitEvent = actObj;
        sServicePitEvent = serviceObj;
        sRecPitEvent = recObj;
        DLog.i("PluginManager.sActPitEvent: " + PluginManager.sActPitEvent.getClass().getClassLoader());
    }

    public static boolean startActivityForResult(Intent intent, int requestCode, Bundle options) {
        boolean b = false;
        try {
            DLog.i("PluginManager.intent: " + intent.getClass().getClassLoader());
            Method startActivityForResult = PluginManager.sActPitEvent.getClass()
                    .getDeclaredMethod("startActivityForResult", Intent.class, int.class, Bundle.class);
            DLog.i("PluginManager.sStartActivityForResult: " + startActivityForResult.getClass().getClassLoader());
            b = (boolean) startActivityForResult.invoke(PluginManager.sActPitEvent, intent, requestCode, options);
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        }
        return b;
    }

    public static boolean startService(Intent intent) {
        boolean b = false;
        try {
            Method startService = PluginManager.sServicePitEvent.getClass()
                    .getDeclaredMethod("startService", Intent.class);
            b = (boolean) startService.invoke(PluginManager.sServicePitEvent, intent);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return b;
    }

    public static boolean bindService(Intent intent, ServiceConnection conn, int flags) {
        boolean b = false;
        try {
            Method bindService = PluginManager.sServicePitEvent.getClass()
                    .getDeclaredMethod("bindService", Intent.class, ServiceConnection.class, int.class);
            b = (boolean) bindService.invoke(PluginManager.sServicePitEvent, intent, conn, flags);
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        }
        return b;
    }

    public static boolean unbindService(ServiceConnection conn) {
        boolean b = false;
        try {
            Method unbindService = PluginManager.sServicePitEvent.getClass()
                    .getDeclaredMethod("unbindService", ServiceConnection.class);
            b = (boolean) unbindService.invoke(PluginManager.sServicePitEvent, conn);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return b;
    }

    public static boolean stopService(Intent intent) {
        boolean b = false;
        try {
            Method stopService = PluginManager.sServicePitEvent.getClass()
                    .getDeclaredMethod("stopService", Intent.class);
            b = (boolean) stopService.invoke(PluginManager.sServicePitEvent, intent);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return b;
    }

    public static void clearServicePit(String name) {
        try {
            Method clearServicePit = PluginManager.sServicePitEvent.getClass()
                    .getDeclaredMethod("clearServicePit", String.class);
            clearServicePit.invoke(PluginManager.sServicePitEvent, name);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
    }

    public static boolean sendBroadcast(Intent intent) {
        boolean b = false;
        try {
            Method sendBroadcast = PluginManager.sRecPitEvent.getClass()
                    .getDeclaredMethod("sendBroadcast", Intent.class);
            b = (boolean) sendBroadcast.invoke(PluginManager.sRecPitEvent, intent);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return b;
    }

}
