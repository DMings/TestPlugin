package com.dming.simple.plugin.provider;

import android.content.ContentProvider;
import android.content.pm.PackageInfo;
import android.content.pm.ProviderInfo;
import com.dming.simple.utils.DLog;

import java.util.ArrayList;
import java.util.List;

public class ProPlugin {

    public static String sPackageName;
    private static List<ContentProvider> sContentProviderList = new ArrayList<>();

    public static void addContentProvider(ContentProvider contentProvider) {
        sContentProviderList.add(contentProvider);
    }

    public static void clearContentProviderMap() {
        sContentProviderList.clear();
    }

    public static void dealPluginProvider(PackageInfo pInfo) {
        ProviderInfo[] providers = pInfo.providers;
        sPackageName = pInfo.packageName;
        clearContentProviderMap();
        for (ProviderInfo providerInfo : providers) {
            DLog.i("SPlugin ProviderInfo>" + providerInfo.authority + " -> " + providerInfo.name);
            try {
                Class provider = Class.forName(providerInfo.name);
                addContentProvider((ContentProvider) provider.newInstance());
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
