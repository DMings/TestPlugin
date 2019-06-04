package com.dming.simple.plugin.service;

import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ServiceInfo;
import android.text.TextUtils;
import com.dming.simple.utils.DLog;

public class ServicePitEvent {

//    private static volatile ServicePitEvent sActPitEvent;
//
//    public static ServicePitEvent getInstance() {
//        if (sActPitEvent == null) {
//            synchronized (ServicePitEvent.class) {
//                if (sActPitEvent == null) {
//                    sActPitEvent = new ServicePitEvent();
//                }
//            }
//        }
//        return sActPitEvent;
//    }

    public static boolean startService(Intent intent) {
        if (intent.getComponent() != null && ServicePlugin.sPackageName != null) {
            String serviceName = intent.getComponent().getClassName();
            String servicePit = ServicePlugin.findServicePit();
            DLog.i("startService servicePit: " + servicePit + " serviceName: " + serviceName);
            if (servicePit == null) {
                return false;
            }
            ServicePlugin.sHostServiceMap.put(servicePit, serviceName);
            intent.setClassName(ServicePlugin.sPackageName, servicePit);
            ServiceInfo serviceInfo = ServicePlugin.getPluginServiceInfo(serviceName);
            intent.putExtra("ServiceInfo", serviceInfo);
            return true;
        }
        return false;
    }

    public static boolean bindService(Intent intent, ServiceConnection conn, int flags) {
        if (intent.getComponent() != null && ServicePlugin.sPackageName != null) {
            String pluginName = intent.getComponent().getClassName();
            String hostName = ServicePlugin.findPluginService(pluginName);
            if(hostName == null)return false;
            intent.setClassName(ServicePlugin.sPackageName, hostName);
            return true;
        }
        return false;
    }

    public static boolean unbindService(ServiceConnection conn) {
        return true;
    }

    public static boolean stopService(Intent intent) {
        if (intent.getComponent() != null && ServicePlugin.sPackageName != null) {
            String pluginName = intent.getComponent().getClassName();
            String hostName = ServicePlugin.findPluginService(pluginName);
            if(hostName == null)return false;
            intent.setClassName(ServicePlugin.sPackageName, hostName);
            return true;
        }
        return false;
    }

}
