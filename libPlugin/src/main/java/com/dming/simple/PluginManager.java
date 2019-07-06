package com.dming.simple;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dming.simple.utils.DLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PluginManager {

    public static Resources sResources;
    public static ApplicationInfo sApplicationInfo;
    public static ClassLoader sClassLoader;
    private static Object sActPitEvent;
    private static Object sServicePitEvent;
    private static Object sProPitEvent;
    
    public static void setPicEvent(Object actObj, Object serviceObj,Object sProviderObj) {
        sActPitEvent = actObj;
        sServicePitEvent = serviceObj;
        sProPitEvent = sProviderObj;
        DLog.i("PluginManager.sActPitEvent: " + PluginManager.sActPitEvent.getClass().getClassLoader());
    }

    public static boolean startActivityForResult(Intent intent, int requestCode, Bundle options) {
        boolean b = false;
        try {
            DLog.i("PluginManager.intent: " + intent.getClass().getClassLoader());
            Method startActivityForResult = PluginManager.sActPitEvent.getClass()
                    .getDeclaredMethod("startActivityForResult", Intent.class, int.class, Bundle.class);
            DLog.i("PluginManager.sStartActivityForResult: " + startActivityForResult.getClass().getClassLoader());
            b = (boolean) startActivityForResult.invoke(PluginManager.sActPitEvent, intent, requestCode, options);
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        }
        return b;
    }

    public static boolean startService(Intent intent) {
        boolean b = false;
        try {
            Method startService = PluginManager.sServicePitEvent.getClass()
                    .getDeclaredMethod("startService", Intent.class);
            b = (boolean) startService.invoke(PluginManager.sServicePitEvent, intent);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return b;
    }

    public static boolean bindService(Intent intent, ServiceConnection conn, int flags) {
        boolean b = false;
        try {
            Method bindService = PluginManager.sServicePitEvent.getClass()
                    .getDeclaredMethod("bindService", Intent.class, ServiceConnection.class, int.class);
            b = (boolean) bindService.invoke(PluginManager.sServicePitEvent, intent, conn, flags);
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        }
        return b;
    }

    public static boolean unbindService(ServiceConnection conn) {
        boolean b = false;
        try {
            Method unbindService = PluginManager.sServicePitEvent.getClass()
                    .getDeclaredMethod("unbindService", ServiceConnection.class);
            b = (boolean) unbindService.invoke(PluginManager.sServicePitEvent, conn);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return b;
    }

    public static boolean stopService(Intent intent) {
        boolean b = false;
        try {
            Method stopService = PluginManager.sServicePitEvent.getClass()
                    .getDeclaredMethod("stopService", Intent.class);
            b = (boolean) stopService.invoke(PluginManager.sServicePitEvent, intent);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return b;
    }

    @Nullable
    public static Cursor query(Context context,@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        DLog.i("PluginContentResolver query: " + uri.toString());
        if (PluginManager.sClassLoader != null) {
            try {
                Method query = sProPitEvent.getClass()
                        .getDeclaredMethod("query", Uri.class, String[].class, String.class, String[].class, String.class);
                return (Cursor) query.invoke(sProPitEvent, uri, projection, selection, selectionArgs, sortOrder);
            } catch (NoSuchMethodException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        } else {
            if (context != null) {
                return context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
            }
        }
        return null;
    }

    @Nullable
    public static String getType(Context context,@NonNull Uri uri) {
        DLog.i("PluginContentResolver getType: " + uri.toString());
        if (PluginManager.sClassLoader != null) {
            try {
                Method getType = sProPitEvent.getClass()
                        .getDeclaredMethod("getType", Uri.class);
                return (String) getType.invoke(sProPitEvent, uri);
            } catch (NoSuchMethodException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        } else {
            if (context != null) {
                return context.getContentResolver().getType(uri);
            }
        }
        return null;
    }

    @Nullable
    public static Uri insert(Context context,@NonNull Uri uri, @Nullable ContentValues values) {
        DLog.i("PluginContentResolver insert: " + uri.toString());
        if (PluginManager.sClassLoader != null) {
            try {
                Method insert = sProPitEvent.getClass()
                        .getDeclaredMethod("insert", Uri.class, ContentValues.class);
                return (Uri) insert.invoke(sProPitEvent, uri, values);
            } catch (NoSuchMethodException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        } else {
            if (context != null) {
                return context.getContentResolver().insert(uri, values);
            }
        }
        return null;
    }

    public static int delete(Context context,@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        DLog.i("PluginContentResolver delete: " + uri.toString());
        if (PluginManager.sClassLoader != null) {
            try {
                Method delete = sProPitEvent.getClass()
                        .getDeclaredMethod("delete", Uri.class, String.class, String[].class);
                return (int) delete.invoke(sProPitEvent, uri, selection, selectionArgs);
            } catch (NoSuchMethodException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        } else {
            if (context != null) {
                return context.getContentResolver().delete(uri, selection, selectionArgs);
            }
        }
        return 0;
    }

    public int update(Context context,@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        DLog.i("PluginContentResolver update: " + uri.toString());
        if (PluginManager.sClassLoader != null) {
            try {
                Method update = sProPitEvent.getClass()
                        .getDeclaredMethod("update", Uri.class, ContentValues.class, String.class, String[].class);
                return (int) update.invoke(sProPitEvent, uri, values, selection, selectionArgs);
            } catch (NoSuchMethodException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        } else {
            if (context != null) {
                return context.getContentResolver().update(uri, values, selection, selectionArgs);
            }
        }
        return 0;
    }

}
