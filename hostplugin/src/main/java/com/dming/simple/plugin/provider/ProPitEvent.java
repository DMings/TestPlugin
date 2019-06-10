package com.dming.simple.plugin.provider;

public class ProPitEvent {

    private static volatile ProPitEvent sProPitEvent;

    public static ProPitEvent getInstance() {
        if (sProPitEvent == null) {
            synchronized (ProPitEvent.class) {
                if (sProPitEvent == null) {
                    sProPitEvent = new ProPitEvent();
                }
            }
        }
        return sProPitEvent;
    }

}
