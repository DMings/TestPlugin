package com.dming.simple.plugin.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dming.simple.utils.DLog;

import java.util.HashMap;
import java.util.Map;

public class ProviderDispatch extends ContentProvider {

    private static Map<String,ContentProvider> sContentProviderMap = new HashMap<>();

    public static void addContentProvider(String name,ContentProvider contentProvider){
        sContentProviderMap.put(name,contentProvider);
    }

    public void clearContentProviderMap(){
        sContentProviderMap.clear();
    }

//for (Map.Entry<String, SubBroadcastReceiver> entry : sBroadcastReceiverMap.entrySet()) {
//        SubBroadcastReceiver subReceiver = entry.getValue();
//        context.registerReceiver(subReceiver.getReceiver(),subReceiver.getFilter());
//    }

    @Override
    public boolean onCreate() {
        DLog.i("ProviderDispatch onCreate");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        DLog.i("ProviderDispatch query: "+uri.toString());
        String name = uri.getPath();
        if("AAA".equals(name)){
            ContentProvider contentProvider = sContentProviderMap.get(name);
            if(contentProvider != null){
                return contentProvider.query(uri,projection,selection,selectionArgs,sortOrder);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
