package com.dming.simple.plugin.provider;

import android.content.ContentProvider;
import android.content.pm.PackageInfo;
import android.content.pm.ProviderInfo;
import com.dming.simple.SPlugin;
import com.dming.simple.utils.DLog;

import java.util.HashMap;
import java.util.Map;

public class ProPlugin {

    public static Map<String,ContentProvider> sContentProviderMap = new HashMap<>();

    public static void addContentProvider(String name,ContentProvider contentProvider) {
        sContentProviderMap.put(name,contentProvider);
    }

    public static void clearContentProviderMap() {
        sContentProviderMap.clear();
    }

    public static void dealPluginProvider(PackageInfo pInfo) {
        ProviderInfo[] providers = pInfo.providers;
        clearContentProviderMap();
        for (ProviderInfo providerInfo : providers) {
            DLog.i("SPlugin ProviderInfo>" + providerInfo.authority + " -> " + providerInfo.name);
            try {
                Class provider = SPlugin.getInstance().getClassLoader().loadClass(providerInfo.name);
                addContentProvider(providerInfo.authority,(ContentProvider) provider.newInstance());
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
