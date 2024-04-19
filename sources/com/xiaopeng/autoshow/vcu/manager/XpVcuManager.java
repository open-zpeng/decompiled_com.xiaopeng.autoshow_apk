package com.xiaopeng.autoshow.vcu.manager;

import android.car.Car;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import com.xiaopeng.autoshow.utils.LogUtils;
import com.xiaopeng.autoshow.vcu.impl.IVcuChangeListener;
import com.xiaopeng.autoshow.vcu.impl.IVcuModel;
import com.xiaopeng.autoshow.vcu.model.D22VcuModel;
/* loaded from: classes.dex */
public class XpVcuManager {
    private static final String TAG = "VcuManager";
    private static XpVcuManager sInstance;
    protected Handler mBackgroundHandler = initBackgroundHandler();
    private Car mCarApiClient;
    private IVcuModel mVcuMode;
    protected HandlerThread mWorkThread;

    private synchronized void createWorkThread() {
        if (this.mWorkThread == null) {
            this.mWorkThread = new HandlerThread(TAG, 10);
            this.mWorkThread.start();
        }
    }

    private Handler initBackgroundHandler() {
        createWorkThread();
        Handler handler = this.mBackgroundHandler;
        return handler == null ? new Handler(this.mWorkThread.getLooper()) : handler;
    }

    public static synchronized XpVcuManager init(Context context) {
        XpVcuManager xpVcuManager;
        synchronized (XpVcuManager.class) {
            if (sInstance == null) {
                synchronized (XpVcuManager.class) {
                    if (sInstance == null) {
                        sInstance = new XpVcuManager(context);
                    }
                }
            }
            xpVcuManager = sInstance;
        }
        return xpVcuManager;
    }

    public static synchronized XpVcuManager get() {
        XpVcuManager xpVcuManager;
        synchronized (XpVcuManager.class) {
            xpVcuManager = sInstance;
        }
        return xpVcuManager;
    }

    private XpVcuManager(Context context) {
        initFeatureMode();
        this.mCarApiClient = Car.createCar(context, new ServiceConnection() { // from class: com.xiaopeng.autoshow.vcu.manager.XpVcuManager.1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                LogUtils.d(XpVcuManager.TAG, "connect carService completed!");
                XpVcuManager.this.mVcuMode.initVcuManager(XpVcuManager.this.mCarApiClient);
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                LogUtils.e(XpVcuManager.TAG, "carService disconnected");
            }
        }, this.mBackgroundHandler);
        LogUtils.d(TAG, "connect carService");
        this.mCarApiClient.connect();
    }

    private void initFeatureMode() {
        this.mVcuMode = new D22VcuModel();
    }

    public void registerCallback() {
        IVcuModel iVcuModel = this.mVcuMode;
        if (iVcuModel != null) {
            iVcuModel.registerCallback();
        }
    }

    public void unRegisterCallback() {
        IVcuModel iVcuModel = this.mVcuMode;
        if (iVcuModel != null) {
            iVcuModel.unRegisterCallback();
        }
    }

    public boolean checkGearLimit() {
        IVcuModel iVcuModel = this.mVcuMode;
        if (iVcuModel != null) {
            return iVcuModel.checkGearLimit();
        }
        return false;
    }

    public boolean isCarGearP() {
        IVcuModel iVcuModel = this.mVcuMode;
        if (iVcuModel != null) {
            return iVcuModel.isCarGearP();
        }
        return false;
    }

    public void addVcuChangeListener(IVcuChangeListener iVcuChangeListener) {
        IVcuModel iVcuModel = this.mVcuMode;
        if (iVcuModel != null) {
            iVcuModel.addVcuChangeListener(iVcuChangeListener);
        }
    }

    public void removeVcuChangeListener(IVcuChangeListener iVcuChangeListener) {
        IVcuModel iVcuModel = this.mVcuMode;
        if (iVcuModel != null) {
            iVcuModel.removeVcuChangeListener(iVcuChangeListener);
        }
    }
}
