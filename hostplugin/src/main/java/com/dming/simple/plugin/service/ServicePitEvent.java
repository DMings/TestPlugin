package com.dming.simple.plugin.service;

import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ServiceInfo;
import android.text.TextUtils;
import com.dming.simple.plugin.activity.ActPlugin;
import com.dming.simple.utils.DLog;

import java.util.Map;

public class ServicePitEvent {

    private volatile static ServicePitEvent sActPitEvent;

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
        if (intent.getComponent() != null && ActPlugin.sPackageName != null) {
            String serviceName = intent.getComponent().getClassName();
            String servicePit = ServicePlugin.findServicePit(serviceName);
            DLog.i("startService servicePit: " + servicePit + " serviceName: " + serviceName);
            if (servicePit == null) {
                return false;
            }
            ServicePlugin.sHostServiceMap.put(servicePit, serviceName);
            intent.setClassName(ActPlugin.sPackageName, servicePit);
            ServiceInfo serviceInfo = ServicePlugin.getPluginServiceInfo(serviceName);
            intent.putExtra("ServiceInfo", serviceInfo);
            return true;
        }
        return false;
    }

    public boolean bindService(Intent intent, ServiceConnection conn, int flags) {
        if (intent.getComponent() != null && ActPlugin.sPackageName != null) {
            String serviceName = intent.getComponent().getClassName();
            String servicePit = ServicePlugin.findServicePit(serviceName);
            DLog.i("bindService servicePit: " + servicePit + " serviceName: " + serviceName);
            if (servicePit == null) {
                return false;
            }
            ServicePlugin.sHostServiceMap.put(servicePit, serviceName);
            intent.setClassName(ActPlugin.sPackageName, servicePit);
            ServiceInfo serviceInfo = ServicePlugin.getPluginServiceInfo(serviceName);
            intent.putExtra("ServiceInfo", serviceInfo);
            return true;
        }
        return false;
    }

    public boolean unbindService(ServiceConnection conn) {
        return true;
    }

    public boolean stopService(Intent intent) {
        if (intent.getComponent() != null && ActPlugin.sPackageName != null) {
            String pluginName = intent.getComponent().getClassName();
            String hostName = ServicePlugin.findPluginService(pluginName);
            if(hostName == null)return false;
            intent.setClassName(ActPlugin.sPackageName, hostName);
            return true;
        }
        return false;
    }

    public void clearServicePit(String pluginName){
        DLog.i("clearServicePit: "+pluginName);
        if (!TextUtils.isEmpty(pluginName)) {
            for(Map.Entry<String,String> entry : ServicePlugin.sHostServiceMap.entrySet()){
                if(pluginName.equals(entry.getValue())){
                    ServicePlugin.sHostServiceMap.put(entry.getKey(), null); //清除坑位
                }
            }
        }
    }

}
