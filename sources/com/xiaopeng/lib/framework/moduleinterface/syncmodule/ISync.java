package com.xiaopeng.lib.framework.moduleinterface.syncmodule;

import android.app.Application;
import java.util.List;
/* loaded from: classes.dex */
public interface ISync {
    void init(Application application, String appID, String appSecret, String vehicleSeries, String vin, String hardwareID);

    void onNetworkAvailable();

    void onUserChanged(Long uid);

    void restore();

    void save(List<SyncData> data);

    void saveAll(List<SyncData> list);
}
