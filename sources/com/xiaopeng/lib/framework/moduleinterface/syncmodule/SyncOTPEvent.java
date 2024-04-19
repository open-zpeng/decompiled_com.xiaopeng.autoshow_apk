package com.xiaopeng.lib.framework.moduleinterface.syncmodule;
/* loaded from: classes.dex */
public final class SyncOTPEvent {
    public IOTPCallback callback;
    public String deviceID;
    public String seq;

    public SyncOTPEvent(String seq, String deviceID, IOTPCallback callback) {
        this.seq = seq;
        this.deviceID = deviceID;
        this.callback = callback;
    }

    public String toString() {
        return "SyncOTPEvent { seq:" + this.seq + "; deviceID:" + this.deviceID + "; }";
    }
}
