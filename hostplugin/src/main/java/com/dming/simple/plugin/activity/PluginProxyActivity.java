package com.dming.simple.plugin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dming.simple.SPlugin;
import com.dming.simple.utils.DLog;
import com.dming.simple.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class PluginProxyActivity extends Activity {

    private Activity mClientActivity;

    private static final Field[] ACTIVITY_FIELDS;

    static {
        ACTIVITY_FIELDS = Activity.class.getDeclaredFields();
    }

    Method ON_ATTACH_BASE_CONTEXT;
    Method ON_CREATE;
    Method ON_POST_CREATE;
    Method ON_START;
    Method ON_RESUME;
    Method ON_POST_RESUME;
    Method ON_PAUSE;
    Method ON_STOP;
    Method ON_DESTROY;
    Method ON_SAVE_INSTANCE_STATE;
    Method ON_RESTORE_INSTANCE_STATE;
    Method ON_ATTACHED_TO_WINDOW;
    Method ON_DETACHED_FROM_WINDOW;
    Method ON_KEY_DOWN;
    Method ON_KEY_UP;
    Method ON_BACK_PRESSED;
    Method ON_ACTIVITY_RESULT;
    Method ON_NEW_INTENT;
    Method ON_REQUEST_PERMISSIONS_RESULT;

    private void dealActivityMethod(Class activityClass) {
        ON_ATTACH_BASE_CONTEXT = ReflectUtils.getMethod(activityClass, "attachBaseContext", Context.class);
        ON_CREATE = ReflectUtils.getMethod(activityClass, "onCreate", Bundle.class);
        ON_POST_CREATE = ReflectUtils.getMethod(activityClass, "onPostCreate", Bundle.class);
        ON_START = ReflectUtils.getMethod(activityClass, "onStart");
        ON_RESUME = ReflectUtils.getMethod(activityClass, "onResume");
        ON_POST_RESUME = ReflectUtils.getMethod(activityClass, "onPostResume");
        ON_PAUSE = ReflectUtils.getMethod(activityClass, "onPause");
        ON_STOP = ReflectUtils.getMethod(activityClass, "onStop");
        ON_DESTROY = ReflectUtils.getMethod(activityClass, "onDestroy");
        ON_SAVE_INSTANCE_STATE = ReflectUtils.getMethod(activityClass, "onSaveInstanceState", Bundle.class);
        ON_RESTORE_INSTANCE_STATE = ReflectUtils.getMethod(activityClass, "onRestoreInstanceState", Bundle.class);
        ON_ATTACHED_TO_WINDOW = ReflectUtils.getMethod(activityClass, "onAttachedToWindow");
        ON_DETACHED_FROM_WINDOW = ReflectUtils.getMethod(activityClass, "onDetachedFromWindow");
        ON_KEY_DOWN = ReflectUtils.getMethod(activityClass, "onKeyDown", int.class, KeyEvent.class);
        ON_KEY_UP = ReflectUtils.getMethod(activityClass, "onKeyUp", int.class, KeyEvent.class);
        ON_BACK_PRESSED = ReflectUtils.getMethod(activityClass, "onBackPressed");
        ON_ACTIVITY_RESULT = ReflectUtils.getMethod(activityClass, "onActivityResult",
                int.class, int.class, Intent.class);
        ON_NEW_INTENT = ReflectUtils.getMethod(activityClass, "onNewIntent", Intent.class);
        ON_REQUEST_PERMISSIONS_RESULT = ReflectUtils.getMethod(activityClass, "onRequestPermissionsResult",
                int.class, String[].class, int[].class);
    }

    private void attachHostStatus() {
        for (Field field : ACTIVITY_FIELDS) {
            DLog.i("field: "+field.getName());
//            int modifiers = field.getModifiers();
//            if (Modifier.isStatic(modifiers)
//                    || Modifier.isFinal(modifiers)
//                    || Modifier.isVolatile(modifiers)
//                    || Modifier.isTransient(modifiers)) {
//                continue;
//            }
            DLog.i("field-: "+field.getName());
            field.setAccessible(true);
            try {
                Object fieldsValue = field.get(this); // host
                field.set(mClientActivity, fieldsValue);// plugin
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        String className = intent.getStringExtra("ClassName");
        createActivity(className);
        attachHostStatus();
        callTargetActivityMethod(ON_ATTACH_BASE_CONTEXT, this);
        callTargetActivityMethod(ON_CREATE, savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        callTargetActivityMethod(ON_NEW_INTENT, intent);
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


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return (boolean) callTargetActivityMethod(ON_KEY_DOWN, keyCode, event);
//    }
//
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        return (boolean) callTargetActivityMethod(ON_KEY_UP, keyCode, event);
//    }


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
            Class loadClass = SPlugin.getInstance().getClassLoader().loadClass(className);
            dealActivityMethod(loadClass);
            Object act = loadClass.newInstance();
            if (act instanceof Activity) {
                mClientActivity = (Activity) act;
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
