package com.dming.simple.plugin.service;

import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ServiceInfo;
import com.dming.simple.utils.DLog;

public class ServicePitEvent {

    private static volatile ServicePitEvent sActPitEvent;

    public static ServicePitEvent getInstance() {
        if (sActPitEvent == null) {
            synchronized (ServicePitEvent.class) {
                if (sActPitEvent == null) {
                    sActPitEvent = new ServicePitEvent();
                }
            }
        }
        return sActPitEvent;
    }

    public boolean startService(Intent intent) {
        ServicePlugin servicePlugin = ServicePlugin.getInstance();
        if (intent.getComponent() != null && servicePlugin.sPackageName != null) {
            String serviceName = intent.getComponent().getClassName();
            String servicePit = servicePlugin.findServicePit();
            DLog.i("startService servicePit: " + servicePit + " serviceName: " + serviceName);
            if (servicePit == null) {
                return false;
            }
            servicePlugin.sHostServiceMap.put(servicePit, serviceName);
            intent.setClassName(servicePlugin.sPackageName, servicePit);
            ServiceInfo serviceInfo = servicePlugin.getPluginServiceInfo(serviceName);
            intent.putExtra("ServiceInfo", serviceInfo);
            return true;
        }
        return false;
    }

    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return false;
    }

    public boolean unbindService(ServiceConnection conn) {
        return false;
    }

    public boolean stopService(Intent intent) {
        ServicePlugin servicePlugin = ServicePlugin.getInstance();
        if (intent.getComponent() != null && servicePlugin.sPackageName != null) {
            String pluginName = intent.getComponent().getClassName();
            String hostName = servicePlugin.findPluginService(pluginName);
            intent.setClassName(servicePlugin.sPackageName, hostName);
        }
        return false;
    }


}
