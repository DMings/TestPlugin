package com.dming.simple;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.dming.simple.utils.DLog;

import java.lang.reflect.InvocationTargetException;

public class PluginActivity extends AppCompatActivity {

    static {
        DLog.i("PluginActivity getClassLoader: " + PluginActivity.class.getClassLoader());
    }

    public static Resources sResources;
    public static ApplicationInfo sApplicationInfo;
    public static ClassLoader sClassLoader;
    public static Object sActPitEvent;
    public static Resources.Theme sTheme;

    @Override
    protected void attachBaseContext(Context context) {
        DLog.i("context getResources: " + context.getResources());
        PluginContext pluginContext = new PluginContext(context,sResources,sApplicationInfo,sClassLoader);
        DLog.i("PluginActivity getClassLoader-: " + PluginActivity.class.getClassLoader());
        super.attachBaseContext(pluginContext);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        DLog.i("PluginActivity.this.getResources: " + PluginActivity.this.getResources());
        Intent intent = getIntent();
        String plugin = intent.getStringExtra("Plugin");
        ActivityInfo activityInfo = intent.getParcelableExtra("ActivityInfo");
        setTheme(getApplicationInfo().theme);
        super.onCreate(savedInstanceState);
        DLog.i("plugin: " + plugin + " activityInfo: " + activityInfo);
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        DLog.i("sActPitEvent>"+sActPitEvent);
        try {
            boolean b = (boolean) sActPitEvent.getClass().getDeclaredMethod("startActivityForResult",Intent.class,int.class,Bundle.class)
                    .invoke(sActPitEvent,intent, requestCode, options);
            if (b) {
                if (Build.VERSION.SDK_INT >= 16) {
                    super.startActivityForResult(intent, requestCode, options);
                } else {
                    super.startActivityForResult(intent, requestCode);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
//        if (actPitEvent.startActivityForResult(intent, requestCode, options)) {
//            if (Build.VERSION.SDK_INT >= 16) {
//                super.startActivityForResult(intent, requestCode, options);
//            } else {
//                super.startActivityForResult(intent, requestCode);
//            }
//        }
    }

//    @Override
//    public Resources getResources() {
//        if (sResources != null) {
//            return sResources;
//        }
//        return super.getResources();
//    }

//    @Override
//    public ApplicationInfo getApplicationInfo() {
//        if(sApplicationInfo != null){
//            return sApplicationInfo;
//        }
//        return super.getApplicationInfo();
//    }

//    @Override
//    public Resources.Theme getTheme() {
//        DLog.i("getTheme: " + sTheme);
//        if (sTheme != null) {
//            return sTheme;
//        }
//        return super.getTheme();
//    }


    @Override
    public ClassLoader getClassLoader() {
        DLog.i("P getClassLoader--->");
        return super.getClassLoader();
    }
}
