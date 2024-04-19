package com.xiaopeng.autoshow;

import android.app.Application;
import android.content.Context;
import com.xiaopeng.autoshow.model.AutoShowModel;
import com.xiaopeng.autoshow.vcu.manager.XpVcuManager;
import com.xiaopeng.lib.bughunter.BugHunter;
import com.xiaopeng.lib.framework.module.Module;
import com.xiaopeng.lib.framework.netchannelmodule.NetworkChannelsEntry;
import com.xiaopeng.lib.http.HttpsUtils;
import com.xiaopeng.lib.security.xmartv1.RandomKeySecurity;
import com.xiaopeng.xui.Xui;
/* loaded from: classes.dex */
public class AutoShowApp extends Application {
    private static Context sContext;
    private static AutoShowApp sInstance;

    public static AutoShowApp getInstance() {
        return sInstance;
    }

    public static Context getContext() {
        sContext = sInstance.getApplicationContext();
        return sContext;
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        BugHunter.init(this);
        Xui.init(this);
        XpVcuManager.get();
        XpVcuManager.init(this);
        HttpsUtils.init(this, false);
        Module.register(NetworkChannelsEntry.class, new NetworkChannelsEntry());
        try {
            RandomKeySecurity.getInstance().init((Context) this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AutoShowModel.getInstance().init();
    }
}
