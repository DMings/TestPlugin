package com.dming.simple.plugin.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dming.simple.utils.DLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PluginContentResolver {

    private static Object sProPitEvent;

    public PluginContentResolver(Object sProviderObj) {
        sProPitEvent = sProviderObj;
    }

    @Nullable
    public static Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        DLog.i("PluginContentResolver query: " + uri.toString());
        try {
            Method query = PluginContentResolver.sProPitEvent.getClass()
                    .getDeclaredMethod("query", Uri.class, String[].class, String.class, String[].class, String.class);
            return (Cursor) query.invoke(PluginContentResolver.sProPitEvent, uri, projection, selection, selectionArgs, sortOrder);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return null;
    }

    @Nullable
    public static String getType(@NonNull Uri uri) {
        DLog.i("PluginContentResolver getType: " + uri.toString());
        try {
            Method getType = PluginContentResolver.sProPitEvent.getClass()
                    .getDeclaredMethod("getType", Uri.class);
            return (String) getType.invoke(PluginContentResolver.sProPitEvent, uri);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return null;
    }

    @Nullable
    public static Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        DLog.i("PluginContentResolver insert: " + uri.toString());
        try {
            Method insert = PluginContentResolver.sProPitEvent.getClass()
                    .getDeclaredMethod("insert", Uri.class, ContentValues.class);
            return (Uri) insert.invoke(PluginContentResolver.sProPitEvent, uri, values);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return null;
    }

    public static int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        DLog.i("PluginContentResolver delete: " + uri.toString());
        try {
            Method delete = PluginContentResolver.sProPitEvent.getClass()
                    .getDeclaredMethod("delete", Uri.class, String.class, String[].class);
            return (int) delete.invoke(PluginContentResolver.sProPitEvent, uri, selection, selectionArgs);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return 0;
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        DLog.i("PluginContentResolver update: " + uri.toString());
        try {
            Method update = PluginContentResolver.sProPitEvent.getClass()
                    .getDeclaredMethod("update", Uri.class, ContentValues.class, String.class, String[].class);
            return (int) update.invoke(PluginContentResolver.sProPitEvent, uri, values, selection, selectionArgs);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return 0;
    }

}
