package com.xiaopeng.autoshow.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import com.xiaopeng.autoshow.AutoShowApp;
import com.xiaopeng.autoshow.Config;
import com.xiaopeng.autoshow.R;
import com.xiaopeng.autoshow.model.AutoShowModel;
import com.xiaopeng.autoshow.utils.ContentResolverUtil;
import com.xiaopeng.autoshow.utils.LogUtils;
import com.xiaopeng.autoshow.vcu.manager.XpVcuManager;
import com.xiaopeng.xui.app.XToast;
/* loaded from: classes.dex */
public class AppService extends Service implements IServiceView {
    private final String TAG = "AppService";

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        LogUtils.d("AppService", "onCreate");
        super.onCreate();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        LogUtils.d("AppService", "onStartCommand");
        if (intent != null) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                dispatchAction(action);
            } else {
                stopSelf();
            }
        } else {
            stopSelf();
        }
        return super.onStartCommand(intent, i, i2);
    }

    private void dispatchAction(String str) {
        char c;
        int hashCode = str.hashCode();
        if (hashCode != -24712288) {
            if (hashCode == 1746617685 && str.equals(Config.ACTION_FACTORY_CAR_TESTDRIVE)) {
                c = 1;
            }
            c = 65535;
        } else {
            if (str.equals(Config.ACTION_FACTORY_CAR_SHOW)) {
                c = 0;
            }
            c = 65535;
        }
        if (c == 0) {
            if (isPermittedModel(2)) {
                AutoShowModel.getInstance().startAutoShow(this);
            }
        } else if (c == 1 && isPermittedModel(1)) {
            AutoShowModel.getInstance().startTestDriveModel(this);
        }
    }

    private boolean isPermittedModel(int i) {
        int intValue = ContentResolverUtil.getIntValue(Config.AUTO_SHOW_MODEL_SETTING_CONFIG_KEY);
        if (i == 2) {
            if (XpVcuManager.get().checkGearLimit()) {
                cleanTestModel();
                return true;
            } else if (intValue == 1) {
                XToast.show(AutoShowApp.getContext().getString(R.string.test_drive_entered));
                return false;
            }
        }
        if (i == 1 && XpVcuManager.get().checkGearLimit()) {
            XToast.show(AutoShowApp.getContext().getString(R.string.exhibition_mode_entered));
            cleanTestModel();
            return false;
        }
        return true;
    }

    private void cleanTestModel() {
        boolean putIntValue = ContentResolverUtil.putIntValue(Config.AUTO_SHOW_MODEL_SETTING_CONFIG_KEY, 0);
        LogUtils.i("AppService", "value:0 ; result:" + putIntValue);
    }

    @Override // com.xiaopeng.autoshow.service.IServiceView
    public void onStopSelf() {
        stopSelf();
    }

    @Override // android.app.Service
    public void onDestroy() {
        LogUtils.d("AppService", "onDestroy");
        super.onDestroy();
    }
}
