package com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http;

import android.app.Application;
import okhttp3.Interceptor;
/* loaded from: classes.dex */
public interface IConfig {
    IConfig addInterceptor(Interceptor interceptor);

    IConfig applicationContext(Application application);

    void apply();

    int connectTimeout();

    IConfig connectTimeout(int timeoutInMilliSeconds);

    int dnsTimeout();

    IConfig dnsTimeout(int timeoutInMilliSeconds);

    IConfig enableLogging();

    IConfig enableTracing();

    IConfig enableTrafficStats();

    int readTimeout();

    IConfig readTimeout(int timeoutInMilliSeconds);

    int retryCount();

    IConfig retryCount(int retryCount);

    int writeTimeout();

    IConfig writeTimeout(int timeoutInMilliSeconds);
}
