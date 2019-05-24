package com.dming.simple;

import android.app.Application;
import com.dming.simple.utils.DLog;

public class SPlugin {

    public static void init(Application application){
        boolean b = PatchClassLoaderUtils.patch(application);
        if (b) {
            PMF.initInThread(application);
            DLog.i( "Patch ClassLoader success");
        } else {
            DLog.i("Patch ClassLoader false");
        }
    }

}
