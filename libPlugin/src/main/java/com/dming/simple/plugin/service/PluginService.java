package com.dming.simple.plugin.service;

import android.app.Service;
import com.dming.simple.PluginManager;

public abstract class PluginService extends Service {

    @Override
    public void onDestroy() {
        if (PluginManager.sClassLoader != null) {
            PluginManager.clearServicePit(this.getClass().getName());
        }
        super.onDestroy();
    }
}
