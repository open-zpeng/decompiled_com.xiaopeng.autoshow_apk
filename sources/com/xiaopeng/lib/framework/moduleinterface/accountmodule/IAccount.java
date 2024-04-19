package com.xiaopeng.lib.framework.moduleinterface.accountmodule;

import android.app.Application;
/* loaded from: classes.dex */
public interface IAccount {
    IUserInfo getUserInfo() throws AbsException;

    void init(Application application, String appId) throws AbsException;

    void init(Application application, String appId, String packageName) throws AbsException;

    void login() throws AbsException;

    void logout() throws AbsException;

    void requestOAuth(ICallback<IAuthInfo, IError> callback);

    void requestOAuth(String appID, ICallback<IAuthInfo, IError> callback);

    void requestOTP(String deviceID, ICallback<IOTPInfo, IError> callback);
}
