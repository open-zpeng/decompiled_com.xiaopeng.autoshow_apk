package com.ut.mini.plugin;
/* loaded from: classes.dex */
public abstract class UTPluginMsgDispatchDelegate {
    private Object g;

    public boolean isMatchPlugin(UTPlugin uTPlugin) {
        return true;
    }

    public final Object getMsgObj() {
        return this.g;
    }

    public UTPluginMsgDispatchDelegate(Object obj) {
        this.g = null;
        this.g = obj;
    }

    public Object getDispatchObject(UTPlugin uTPlugin) {
        return this.g;
    }
}
