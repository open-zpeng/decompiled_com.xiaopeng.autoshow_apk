package com.xiaopeng.lib.framework.moduleinterface.configurationmodule;

import android.app.Application;
/* loaded from: classes.dex */
public interface IConfiguration {
    String getConfiguration(String key, String defaultValue);

    void init(Application application, String appID);
}
