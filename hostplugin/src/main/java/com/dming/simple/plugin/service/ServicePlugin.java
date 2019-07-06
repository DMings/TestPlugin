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

    private static HashMap<String, ServiceInfo> sPluginServiceMap = new HashMap<>();

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

    public static ServiceInfo getPluginServiceInfo(String serviceName) {
        return sPluginServiceMap.get(serviceName);
    }

    public static void obtainPluginService(PackageInfo pInfo) {
        ServiceInfo[] services = pInfo.services;
        for (ServiceInfo serviceInfo : services) {
            DLog.i("Plugin serviceInfo>" + serviceInfo.name + " packageName: " + serviceInfo.packageName);
            sPluginServiceMap.put(serviceInfo.name, serviceInfo);
        }
    }

    public static void clear() {
        sPluginServiceMap.clear();
        ServicePitEvent.sActPitEvent = null;
    }

}
