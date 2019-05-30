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
        DLog.e("loadPluginClass className: " + name);
        return c;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//        DLog.e("FClassLoader: "+name);
        return super.loadClass(name, resolve);
    }
}
