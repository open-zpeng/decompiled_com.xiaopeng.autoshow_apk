package com.xiaopeng.lib.framework.moduleinterface.systemdelegate;

import android.os.RemoteException;
/* loaded from: classes.dex */
public interface ISystemDelegate {
    String getCertificate() throws RemoteException;

    void setSystemProperty(String key, String value) throws RemoteException;
}
