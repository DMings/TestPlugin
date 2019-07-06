package com.dming.simple.plugin.service;

import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ServiceInfo;
import com.dming.simple.SPlugin;
import com.dming.simple.utils.DLog;

public class ServicePitEvent {

    volatile static ServicePitEvent sActPitEvent;

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
        if (intent.getComponent() != null) {
            String serviceName = intent.getComponent().getClassName();
            ServiceInfo serviceInfo = ServicePlugin.getPluginServiceInfo(serviceName);
            intent.putExtra("ServiceInfo", serviceInfo);
            intent.putExtra("ClassName",serviceName);
            intent.setClassName(SPlugin.getInstance().getHostPkgName(), PluginProxyService.class.getName());
            DLog.i("startService serviceName: " + serviceName);
            return true;
        }
        return false;
    }

    public boolean bindService(Intent intent, ServiceConnection conn, int flags) {
        if (intent.getComponent() != null) {
            String serviceName = intent.getComponent().getClassName();
            ServiceInfo serviceInfo = ServicePlugin.getPluginServiceInfo(serviceName);
            intent.putExtra("ServiceInfo", serviceInfo);
            intent.putExtra("ClassName",serviceName);
            intent.setClassName(SPlugin.getInstance().getHostPkgName(), PluginProxyService.class.getName());
            DLog.i("bindService serviceName: " + serviceName);
            return true;
        }
        return false;
    }

    public boolean unbindService(ServiceConnection conn) {
        return true;
    }

    public boolean stopService(Intent intent) {
        if (intent.getComponent() != null) {
            String serviceName = intent.getComponent().getClassName();
            intent.putExtra("ClassName",serviceName);
            intent.setClassName(SPlugin.getInstance().getHostPkgName(), PluginProxyService.class.getName());
            DLog.i("stopService serviceName: " + serviceName);
            return true;
        }
        return false;
    }

}
