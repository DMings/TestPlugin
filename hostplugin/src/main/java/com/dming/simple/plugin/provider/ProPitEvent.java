package com.dming.simple.plugin.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dming.simple.utils.DLog;

import java.util.Map;

public class ProPitEvent {

    private static volatile ProPitEvent sProPitEvent;

    public static ProPitEvent getInstance() {
        if (sProPitEvent == null) {
            synchronized (ProPitEvent.class) {
                if (sProPitEvent == null) {
                    sProPitEvent = new ProPitEvent();
                }
            }
        }
        return sProPitEvent;
    }

    private ContentProvider getPluginContentProvider(Uri uri) {
        for (Map.Entry<String, ContentProvider> entry : ProPlugin.sContentProviderMap.entrySet()) {
            if (entry.getKey().equals(uri.getAuthority())) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Nullable
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        DLog.i("ProviderDispatch query: " + uri.toString());
        ContentProvider contentProvider = getPluginContentProvider(uri);
        if (contentProvider != null) {
            return contentProvider.query(uri, projection, selection, selectionArgs, sortOrder);
        }
        return null;
    }

    @Nullable
    public String getType(@NonNull Uri uri) {
        DLog.i("ProviderDispatch getType: " + uri.toString());
        ContentProvider contentProvider = getPluginContentProvider(uri);
        if (contentProvider != null) {
            return contentProvider.getType(uri);
        }
        return null;
    }

    @Nullable
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        DLog.i("ProviderDispatch insert: " + uri.toString());
        ContentProvider contentProvider = getPluginContentProvider(uri);
        if (contentProvider != null) {
            return contentProvider.insert(uri, values);
        }
        return null;
    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        DLog.i("ProviderDispatch delete: " + uri.toString());
        ContentProvider contentProvider = getPluginContentProvider(uri);
        if (contentProvider != null) {
            return contentProvider.delete(uri, selection, selectionArgs);
        }
        return 0;
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        DLog.i("ProviderDispatch update: " + uri.toString());
        ContentProvider contentProvider = getPluginContentProvider(uri);
        if (contentProvider != null) {
            return contentProvider.update(uri, values, selection, selectionArgs);
        }
        return 0;
    }

}
