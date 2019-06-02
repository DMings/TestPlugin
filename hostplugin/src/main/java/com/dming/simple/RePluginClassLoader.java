/*
 * Copyright (C) 2005-2017 Qihoo 360 Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.dming.simple;

import android.util.Log;
import com.dming.simple.utils.DLog;
import com.dming.simple.utils.ReflectUtils;
import dalvik.system.PathClassLoader;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;


/**
 * 宿主的ClassLoader，插件框架的核心之一
 * <p>
 * 注意：为了兼容Android 7.0以上的LoadedApk.updateApplicationInfo中，对addDexPath方法的依赖，
 * 特将继承关系调整到PathClassLoader，以前是ClassLoader
 *
 * @author RePlugin Team
 */
public class RePluginClassLoader extends PathClassLoader {

    private final static boolean LOG = true;
    
    private static final String TAG = "RePluginClassLoader";

    private final ClassLoader mOrig;

    /**
     * 用load系列代替
     */
    //private Method findClassMethod;

    private Method findResourceMethod;

    private Method findResourcesMethod;

    private Method findLibraryMethod;

    private Method getPackageMethod;

    public RePluginClassLoader(ClassLoader parent, ClassLoader orig) {

        // 由于PathClassLoader在初始化时会做一些Dir的处理，所以这里必须要传一些内容进来
        // 但我们最终不用它，而是拷贝所有的Fields
        super("", "", parent);
        mOrig = orig;

        // 将原来宿主里的关键字段，拷贝到这个对象上，这样骗系统以为用的还是以前的东西（尤其是DexPathList）
        // 注意，这里用的是“浅拷贝”
        // Added by Jiongxuan Zhang
        copyFromOriginal(orig);

        initMethods(orig);
    }

    private void initMethods(ClassLoader cl) {
        Class<?> c = cl.getClass();
        findResourceMethod = ReflectUtils.getMethod(c, "findResource", String.class);
        findResourceMethod.setAccessible(true);
        findResourcesMethod = ReflectUtils.getMethod(c, "findResources", String.class);
        findResourcesMethod.setAccessible(true);
        findLibraryMethod = ReflectUtils.getMethod(c, "findLibrary", String.class);
        findLibraryMethod.setAccessible(true);
        getPackageMethod = ReflectUtils.getMethod(c, "getPackage", String.class);
        getPackageMethod.setAccessible(true);
    }

    private void copyFromOriginal(ClassLoader orig) {
        // Android 4.0以上只需要复制pathList即可
        // 以下方法在较慢的手机上用时：1ms
        copyFieldValue("pathList", orig);
    }

    private void copyFieldValue(String field, ClassLoader orig) {
        try {
            Field f = ReflectUtils.getField(orig.getClass(), field);
            if (f == null) {
                if (LOG) {
                    Log.e(TAG, "rpcl.cfv: null! f=" + field);
                }
                return;
            }

            // 删除final修饰符
            ReflectUtils.removeFieldFinalModifier(f);

            // 复制Field中的值到this里
            Object o = ReflectUtils.readField(f, orig);
            ReflectUtils.writeField(f, this, o);

            if (LOG) {
                Object test = ReflectUtils.readField(f, this);
                Log.d(TAG, "copyFieldValue: Copied. f=" + field + "; actually=" + test + "; orig=" + o);
            }
        } catch (IllegalAccessException e) {
            if (LOG) {
                Log.e(TAG, "rpcl.cfv: fail! f=" + field);
            }
        }
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        Class<?> c = null;

        c = SPlugin.getInstance().loadClass(className, resolve);
        if (c != null) {
            return c;
        }

        //
        try {
            c = mOrig.loadClass(className);
            if (c != null) {
                return c;
            }
        } catch (Throwable e) {}

        return super.loadClass(className, resolve);
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        DLog.e("RePluginClassLoader findClass: "+className);
        return super.findClass(className);
    }

    @Override
    protected URL findResource(String resName) {
        try {
            return (URL) findResourceMethod.invoke(mOrig, resName);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return super.findResource(resName);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Enumeration<URL> findResources(String resName) {
        try {
            return (Enumeration<URL>) findResourcesMethod.invoke(mOrig, resName);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return super.findResources(resName);
    }

    @Override
    public String findLibrary(String libName) {
        try {
            return (String) findLibraryMethod.invoke(mOrig, libName);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return super.findLibrary(libName);
    }

    @Override
    protected Package getPackage(String name) {
        // 金立手机的某些ROM(F103,F103L,F303,M3)代码ClassLoader.getPackage去掉了关键的保护和错误处理(2015.11~2015.12左右)，会返回null
        // 悬浮窗某些draw代码触发getPackage(...).getName()，getName出现空指针解引，导致悬浮窗进程出现了大量崩溃
        // 此处实现和AOSP一致确保不会返回null
        // SONGZHAOCHUN, 2016/02/29
        if (name != null && !name.isEmpty()) {
            Package pack = null;
            try {
                pack = (Package) getPackageMethod.invoke(mOrig, name);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if (pack == null) {
                if (LOG) {
                    Log.w(TAG, "NRH lcl.gp.1: n=" + name);
                }
                pack = super.getPackage(name);
            }
            if (pack == null) {
                if (LOG) {
                    Log.w(TAG, "NRH lcl.gp.2: n=" + name);
                }
                return definePackage(name, "Unknown", "0.0", "Unknown", "Unknown", "0.0", "Unknown", null);
            }
            return pack;
        }
        return null;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[mBase=" + mOrig.toString() + "]";
    }
}
