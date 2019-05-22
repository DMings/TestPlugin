package com.dming.testplugin;

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
        return c;
    }

}
