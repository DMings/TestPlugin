package com.dming.simple.plugin.service;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.ServiceInfo;
import android.text.TextUtils;
import com.dming.simple.SPlugin;
import com.dming.simple.utils.DLog;
import com.dming.simple.utils.SToast;

import java.util.HashMap;
import java.util.Map;

public class ServicePlugin {

    public static HashMap<String, String> sHostServiceMap = new HashMap<>();
    private static HashMap<String, ServiceInfo> sPluginServiceMap = new HashMap<>();
    private static final String PLUGIN_START_NAME = "com.dming.simple.Service";

//    private static volatile ServicePitEvent sServicePitEvent;
//
//    public static ServicePlugin getInstance() {
//        if (sServicePlugin == null) {
//            synchronized (ServicePlugin.class) {
//                if (sServicePlugin == null) {
//                    sServicePlugin = new ServicePlugin();
//                }
//            }
//        }
//        return sServicePlugin;
//    }

    public interface VerifyOperation{
        boolean check();
    }

    public static void checkPluginLoad(Context context,VerifyOperation verifyOperation){
        if (SPlugin.getInstance().isLoadPlugin()) {
            boolean b = verifyOperation.check();
            if (!b) {
                SToast.showPluginError(context);
            }
        } else {
            SToast.showPluginError(context);
        }
    }


    public static void startService(final Context context, final Intent intent) {
        checkPluginLoad(context, new VerifyOperation() {
            @Override
            public boolean check() {
                boolean b = ServicePitEvent.getInstance().startService(intent);
                if (b && intent.getComponent() != null) {
                    DLog.i("startService pkgName: " + intent.getComponent().getPackageName() + " component: " + intent.getComponent().getClassName());
                    context.startService(intent);
                    return true;
                }
                return false;
            }
        });
    }

    public static void bindService(final Context context, final Intent intent,
                                   final ServiceConnection conn, final int flags) {
        checkPluginLoad(context, new VerifyOperation() {
            @Override
            public boolean check() {
                boolean b = ServicePitEvent.getInstance().bindService(intent,conn,flags);
                if (b && intent.getComponent() != null) {
                    DLog.i("bindService pkgName: " + intent.getComponent().getPackageName() + " component: " + intent.getComponent().getClassName());
                    context.bindService(intent,conn,flags);
                    return true;
                }
                return false;
            }
        });
    }

    public static void unbindService(final Context context, final ServiceConnection conn) {
        checkPluginLoad(context, new VerifyOperation() {
            @Override
            public boolean check() {
                boolean b = ServicePitEvent.getInstance().unbindService(conn);
                if (b && conn != null) {
                    DLog.i("unbindService ServiceConnection: " + conn.toString());
                    context.unbindService(conn);
                    return true;
                }
                return false;
            }
        });
    }

    public static void stopService(final Context context, final Intent intent) {
        checkPluginLoad(context, new VerifyOperation() {
            @Override
            public boolean check() {
                boolean b = ServicePitEvent.getInstance().stopService(intent);
                if (b && intent.getComponent() != null) {
                    DLog.i("stopService pkgName: " + intent.getComponent().getPackageName() + " component: " + intent.getComponent().getClassName());
                    context.stopService(intent);
                    return true;
                }
                return false;
            }
        });
    }

    public static String findServicePit(String serviceName) {
        for (Map.Entry<String, String> entry : sHostServiceMap.entrySet()) {
            if (serviceName.equals(entry.getValue())) {  // 找到旧坑
                return entry.getKey();
            }
        }
        for (Map.Entry<String, String> entry : sHostServiceMap.entrySet()) {
            if (TextUtils.isEmpty(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }


    public static String findPluginService(String pluginName) {
        if (!TextUtils.isEmpty(pluginName)) {
            for(Map.Entry<String,String> entry : sHostServiceMap.entrySet()){
                if(pluginName.equals(entry.getValue())){
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public static String solveServiceClass(String className) {
        if (className.startsWith(PLUGIN_START_NAME)) {
            String service = sHostServiceMap.get(className);
            DLog.i("className: " + className + " service: " + service);
            return service;
        }
        return null;
    }

    public static void clearServicePit(String pluginName){
        if (!TextUtils.isEmpty(pluginName)) {
            for(Map.Entry<String,String> entry : sHostServiceMap.entrySet()){
                if(pluginName.equals(entry.getValue())){
                    sHostServiceMap.put(entry.getKey(), null); //清除坑位
                }
            }
        }
    }

    public static ServiceInfo getPluginServiceInfo(String serviceName) {
        return sPluginServiceMap.get(serviceName);
    }

    public static void obtainHostService(PackageInfo pInfo) {
        ServiceInfo[] services = pInfo.services;
        for (ServiceInfo serviceInfo : services) {
            DLog.i("Host serviceInfo>" + serviceInfo.name + " packageName: " + serviceInfo.packageName);
            if (serviceInfo.name.startsWith(PLUGIN_START_NAME)) {
                sHostServiceMap.put(serviceInfo.name, null);
            }
        }
    }

    public static void obtainPluginService(PackageInfo pInfo) {
        ServiceInfo[] services = pInfo.services;
        for (ServiceInfo serviceInfo : services) {
            DLog.i("Plugin serviceInfo>" + serviceInfo.name + " packageName: " + serviceInfo.packageName);
            sPluginServiceMap.put(serviceInfo.name, serviceInfo);
        }
    }

}
