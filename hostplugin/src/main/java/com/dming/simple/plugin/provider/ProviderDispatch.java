package com.dming.simple.plugin.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dming.simple.utils.DLog;

import java.util.Map;

public class ProviderDispatch extends ContentProvider {

    public static final String AUTHORITIES = "com.simple.provider";

    @Override
    public boolean onCreate() {
        DLog.i("ProviderDispatch onCreate: "+Thread.currentThread());
        return true;
    }

    private ContentProvider getContentProviderByName(Uri uri) {
        String path = uri.getPath();
        if (path == null) return null;
        for (Map.Entry<String, ContentProvider> entry : ProPlugin.sContentProviderMap.entrySet()) {
            String[] pathList = path.split("/");
            if(pathList.length > 1){
                if (entry.getKey().equals(pathList[1])) {
                    return ProPlugin.sContentProviderMap.get(entry.getKey());
                }
            }
        }
        return null;
    }

    private Uri getContentProviderUriByName(Uri uri) {
        return Uri.parse(uri.toString().replace("AUTHORITIES"+"/",""));
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        DLog.i("ProviderDispatch query: " + uri.toString());
        ContentProvider contentProvider = getContentProviderByName(uri);
        if (contentProvider != null) {
            return contentProvider.query(getContentProviderUriByName(uri), projection, selection, selectionArgs, sortOrder);
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        DLog.i("ProviderDispatch getType: " + uri.toString());
        ContentProvider contentProvider = getContentProviderByName(uri);
        if (contentProvider != null) {
            return contentProvider.getType(getContentProviderUriByName(uri));
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        DLog.i("ProviderDispatch insert: " + uri.toString());
        ContentProvider contentProvider = getContentProviderByName(uri);
        if (contentProvider != null) {
            return contentProvider.insert(getContentProviderUriByName(uri), values);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        DLog.i("ProviderDispatch delete: " + uri.toString());
        ContentProvider contentProvider = getContentProviderByName(uri);
        if (contentProvider != null) {
            return contentProvider.delete(getContentProviderUriByName(uri), selection, selectionArgs);
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        DLog.i("ProviderDispatch update: " + uri.toString());
        ContentProvider contentProvider = getContentProviderByName(uri);
        if (contentProvider != null) {
            return contentProvider.update(getContentProviderUriByName(uri), values, selection, selectionArgs);
        }
        return 0;
    }
}
