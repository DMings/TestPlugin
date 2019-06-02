package com.dming.simple.plugin.service;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ServiceInfo;
import android.text.TextUtils;
import com.dming.simple.SPlugin;
import com.dming.simple.utils.DLog;
import com.dming.simple.utils.SToast;

import java.util.HashMap;
import java.util.Map;

public class ServicePlugin {

    public HashMap<String, String> sHostServiceMap = new HashMap<>();
    private HashMap<String, ServiceInfo> sPluginServiceMap = new HashMap<>();
    private static final String PLUGIN_START_NAME = "com.dming.simple.Service";
    public String sPackageName;

    private static volatile ServicePlugin sServicePlugin;

    public static ServicePlugin getInstance() {
        if (sServicePlugin == null) {
            synchronized (ServicePlugin.class) {
                if (sServicePlugin == null) {
                    sServicePlugin = new ServicePlugin();
                }
            }
        }
        return sServicePlugin;
    }


    public void startService(Context context, Intent intent) {
        if (SPlugin.getInstance().isLoadPlugin()) {
            boolean b = ServicePitEvent.getInstance().startService(intent);
            if (b) {
                if (intent.getComponent() != null) {
                    DLog.i("startService pkgName: " + intent.getComponent().getPackageName() + " component: " + intent.getComponent().getClassName());
                }
                context.startService(intent);
            } else {
                SToast.showPluginError(context);
            }
        } else {
            SToast.showPluginError(context);
        }
    }

    public void stopService(Context context, Intent intent) {
        if (SPlugin.getInstance().isLoadPlugin()) {
            boolean b = ServicePitEvent.getInstance().stopService(intent);
            if (b) {
                if (intent.getComponent() != null) {
                    DLog.i("startService pkgName: " + intent.getComponent().getPackageName() + " component: " + intent.getComponent().getClassName());
                }
                context.stopService(intent);
            } else {
                SToast.showPluginError(context);
            }
        } else {
            SToast.showPluginError(context);
        }
    }

    public String findServicePit() {
        for (Map.Entry<String, String> entry : sHostServiceMap.entrySet()) {
            if (TextUtils.isEmpty(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }


    public String findPluginService(String className) {
        return sHostServiceMap.get(className);
    }

    public String solveServiceClass(String className) {
        if (className.startsWith(PLUGIN_START_NAME)) {
            String service = sHostServiceMap.get(className);
            DLog.i("className: " + className + " service: " + service);
            if (!TextUtils.isEmpty(service)) {
//                sHostServiceMap.put(className, null); //清除坑位
                return service;
            }
        }
        return null;
    }

    public ServiceInfo getPluginServiceInfo(String serviceName) {
        return sPluginServiceMap.get(serviceName);
    }

    public void obtainHostService(PackageInfo pInfo) {
        ServiceInfo[] services = pInfo.services;
        sPackageName = pInfo.packageName;
        for (ServiceInfo serviceInfo : services) {
            DLog.i("Host serviceInfo>" + serviceInfo.name + " packageName: " + serviceInfo.packageName);
            if (serviceInfo.name.startsWith(PLUGIN_START_NAME)) {
                sHostServiceMap.put(serviceInfo.name, null);
            }
        }
    }

    public void obtainPluginService(PackageInfo pInfo) {
        ServiceInfo[] services = pInfo.services;
        for (ServiceInfo serviceInfo : services) {
            DLog.i("Plugin serviceInfo>" + serviceInfo.name + " packageName: " + serviceInfo.packageName);
            sPluginServiceMap.put(serviceInfo.name, serviceInfo);
        }
    }

}
