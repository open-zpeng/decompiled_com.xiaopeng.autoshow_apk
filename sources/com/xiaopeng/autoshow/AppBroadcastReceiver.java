package com.xiaopeng.autoshow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.xiaopeng.autoshow.service.AppService;
import com.xiaopeng.autoshow.utils.LogUtils;
/* loaded from: classes.dex */
public class AppBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "AppBroadcastReceiver";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        LogUtils.d(TAG, "Action--->" + action);
        if (Config.ACTION_FACTORY_CAR_SHOW.equals(action) || Config.ACTION_FACTORY_CAR_TESTDRIVE.equals(action)) {
            Intent intent2 = new Intent(context, AppService.class);
            intent2.setAction(action);
            context.startService(intent2);
        }
    }
}
