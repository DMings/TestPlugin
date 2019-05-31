package com.dming.simple;

import com.dming.simple.utils.DLog;
import dalvik.system.DexClassLoader;

public class FClassLoader extends DexClassLoader {

    public FClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, librarySearchPath, parent);
    }

    public Class<?> loadPluginClass(String name) throws ClassNotFoundException {
        Class<?> c = findLoadedClass(name);
        if (c == null) {
            c = findClass(name);
        }
//        DLog.e("loadPluginClass className: " + name);
        return c;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//
        return super.loadClass(name, resolve);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        DLog.e("FClassLoader findClass: "+name);
        return super.findClass(name);
    }
}
