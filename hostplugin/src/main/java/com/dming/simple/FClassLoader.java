package com.dming.simple;

import com.dming.simple.utils.DLog;
import dalvik.system.DexClassLoader;

public class FClassLoader extends DexClassLoader {

    public FClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, librarySearchPath, parent);
    }

    public Class<?> loadPluginWithParantClass(String name) throws ClassNotFoundException {
        return super.loadClass(name, false);
    }

    public Class<?> loadPluginWithParantClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
//        Class<?> c = findLoadedClass(name);
//        if (c == null) {
//            c = findClass(name);
//        }
//        return c;
        return super.loadClass(name);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//        Class<?> c = findLoadedClass(name);
//        if (c == null) {
//            c = findClass(name);
//        }
//        return c;
        return super.loadClass(name,resolve);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        DLog.e("FClassLoader findClass: "+name);
        return super.findClass(name);
    }
}
