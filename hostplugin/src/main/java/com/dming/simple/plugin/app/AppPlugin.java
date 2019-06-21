package com.dming.simple.plugin.app;

import android.app.Application;
import android.content.pm.PackageInfo;
import com.dming.simple.SPlugin;

public class AppPlugin {

    public static void dealPluginApp(PackageInfo pInfo) {
        if(pInfo.applicationInfo != null &&
                pInfo.applicationInfo.className != null){
            try {
                Class applicationClass = SPlugin.getInstance().getClassLoader().loadClass(pInfo.applicationInfo.className);
                Application application = (Application) applicationClass.newInstance();
                application.onCreate();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }

    }

}
