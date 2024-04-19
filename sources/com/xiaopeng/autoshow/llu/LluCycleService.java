package com.xiaopeng.autoshow.llu;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.xiaopeng.autoshow.utils.LogUtils;
/* loaded from: classes.dex */
public class LluCycleService extends Service {
    private final String TAG = LluCycleService.class.getSimpleName();
    private CycleLluPresenter mLluPresenter;

    @Override // android.app.Service
    public void onCreate() {
        LogUtils.d(this.TAG, "onCreate...");
        super.onCreate();
        init();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        LogUtils.d(this.TAG, "onStartCommand...");
        CycleLluPresenter cycleLluPresenter = this.mLluPresenter;
        if (cycleLluPresenter != null) {
            cycleLluPresenter.connectApi();
        }
        return super.onStartCommand(intent, i, i2);
    }

    @Override // android.app.Service
    public void onDestroy() {
        LogUtils.d(this.TAG, "onDestroy...");
        unInit();
        super.onDestroy();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        LogUtils.d(this.TAG, "onBind...");
        return null;
    }

    private void init() {
        this.mLluPresenter = new CycleLluPresenter();
    }

    public void unInit() {
        CycleLluPresenter cycleLluPresenter = this.mLluPresenter;
        if (cycleLluPresenter != null) {
            cycleLluPresenter.destroy();
            this.mLluPresenter = null;
        }
    }
}
