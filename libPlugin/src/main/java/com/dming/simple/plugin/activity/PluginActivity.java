package com.dming.simple.plugin.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.dming.simple.PluginManager;
import com.dming.simple.utils.DLog;
import com.dming.simple.utils.ReflectUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

public class PluginActivity extends AppCompatActivity {

    private Activity mClientActivity;
    private Class activityClass = AppCompatActivity.class;

    Method ON_CREATE = ReflectUtils.getMethod(activityClass, "onCreate", Bundle.class);
    Method ON_POST_CREATE = ReflectUtils.getMethod(activityClass, "onPostCreate", Bundle.class);
    Method ON_START = ReflectUtils.getMethod(activityClass, "onStart");
    Method ON_RESUME = ReflectUtils.getMethod(activityClass, "onResume");
    Method ON_POST_RESUME = ReflectUtils.getMethod(activityClass, "onPostResume");
    Method ON_PAUSE = ReflectUtils.getMethod(activityClass, "onPause");
    Method ON_STOP = ReflectUtils.getMethod(activityClass, "onStop");
    Method ON_DESTROY = ReflectUtils.getMethod(activityClass, "onDestroy");
    Method ON_SAVE_INSTANCE_STATE = ReflectUtils.getMethod(activityClass, "onSaveInstanceState", Bundle.class);
    Method ON_RESTORE_INSTANCE_STATE = ReflectUtils.getMethod(activityClass, "onRestoreInstanceState", Bundle.class);
    Method ON_ATTACHED_TO_WINDOW = ReflectUtils.getMethod(activityClass, "onAttachedToWindow");
    Method ON_DETACHED_FROM_WINDOW = ReflectUtils.getMethod(activityClass, "onDetachedFromWindow");
    Method ON_KEY_DOWN = ReflectUtils.getMethod(activityClass, "onKeyDown", int.class, KeyEvent.class);
    Method ON_KEY_UP = ReflectUtils.getMethod(activityClass, "onKeyUp", int.class, KeyEvent.class);
    Method ON_BACK_PRESSED = ReflectUtils.getMethod(activityClass, "onBackPressed");
    Method ON_ACTIVITY_RESULT = ReflectUtils.getMethod(activityClass, "onActivityResult",
            int.class, int.class, Intent.class);
    Method ON_NEW_INTENT = ReflectUtils.getMethod(activityClass, "onNewIntent", Intent.class);
    Method ON_REQUEST_PERMISSIONS_RESULT = ReflectUtils.getMethod(activityClass, "onRequestPermissionsResult",
            int.class, String[].class, int[].class);

    @Override
    protected void attachBaseContext(Context context) {
        PluginContext pluginContext = new PluginContext(context);
        super.attachBaseContext(pluginContext);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (PluginManager.sClassLoader != null) {
            Intent intent = getIntent();
            String className = intent.getStringExtra("ClassName");
            createActivity(className);
            callTargetActivityMethod(ON_CREATE);
            //
            ActivityInfo activityInfo = intent.getParcelableExtra("ActivityInfo");
            setTheme(activityInfo != null && activityInfo.theme != 0 ? activityInfo.theme : getApplicationInfo().theme);
            if (activityInfo != null) {
                DLog.i("activityInfo.theme: " + activityInfo.theme);
            }
        }
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        callTargetActivityMethod(ON_NEW_INTENT, intent);
        if (PluginManager.sClassLoader != null) {
            if (PluginManager.startActivityForResult(intent, requestCode, options)) {
                super.startActivityForResult(intent, requestCode, options);
            }
        } else {
            super.startActivityForResult(intent, requestCode, options);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        callTargetActivityMethod(ON_NEW_INTENT, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callTargetActivityMethod(ON_ACTIVITY_RESULT, requestCode, resultCode, data);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        callTargetActivityMethod(ON_ATTACHED_TO_WINDOW);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        callTargetActivityMethod(ON_DETACHED_FROM_WINDOW);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (boolean) callTargetActivityMethod(ON_KEY_DOWN, keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return (boolean) callTargetActivityMethod(ON_KEY_UP, keyCode, event);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        callTargetActivityMethod(ON_BACK_PRESSED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callTargetActivityMethod(ON_REQUEST_PERMISSIONS_RESULT,
                requestCode, permissions, grantResults);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        callTargetActivityMethod(ON_RESTORE_INSTANCE_STATE, savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        callTargetActivityMethod(ON_SAVE_INSTANCE_STATE, outState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        callTargetActivityMethod(ON_POST_CREATE, savedInstanceState);
    }


    @Override
    protected void onStart() {
        super.onStart();
        callTargetActivityMethod(ON_START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        callTargetActivityMethod(ON_RESUME);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        callTargetActivityMethod(ON_POST_RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        callTargetActivityMethod(ON_PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        callTargetActivityMethod(ON_STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        callTargetActivityMethod(ON_DESTROY);
    }

    private void createActivity(String className) {
        try {
            Class loadClass = PluginManager.sClassLoader.loadClass(className);
            Object act = loadClass.newInstance();
            if (act instanceof Activity) {
                mClientActivity = (Activity) loadClass.newInstance();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        if (mClientActivity == null) {
            mClientActivity = new Activity();
        }
    }

    private Object callTargetActivityMethod(Method method, Object... args) {
        DLog.d("callTargetActivityMethod: " + Arrays.toString(args));
        try {
            if (mClientActivity == null) {
                throw new IllegalStateException("target activity is null");
            }
            return method.invoke(mClientActivity, args);
        } catch (Throwable e) {
            DLog.e("callTargetActivityMethod: " + method.getName());
            return false;
        }
    }


}
