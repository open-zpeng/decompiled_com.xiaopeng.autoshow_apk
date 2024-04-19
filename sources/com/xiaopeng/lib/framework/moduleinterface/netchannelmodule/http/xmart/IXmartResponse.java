package com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.xmart;
/* loaded from: classes.dex */
public interface IXmartResponse {
    IServerBean body();

    int code();

    Throwable getException();

    String message();
}
