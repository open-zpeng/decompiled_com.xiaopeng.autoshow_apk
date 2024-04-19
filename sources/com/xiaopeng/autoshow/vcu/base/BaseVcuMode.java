package com.xiaopeng.autoshow.vcu.base;

import android.car.CarNotConnectedException;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.vcu.CarVcuManager;
import com.xiaopeng.autoshow.utils.LogUtils;
import com.xiaopeng.autoshow.vcu.impl.IVcuChangeListener;
import com.xiaopeng.autoshow.vcu.impl.IVcuModel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public abstract class BaseVcuMode implements IVcuModel {
    protected CarVcuManager mCarVcuManager;
    protected ArrayList mPropertyIds;
    public final String TAG = getClass().getSimpleName();
    protected CopyOnWriteArrayList<IVcuChangeListener> mVcuListenerList = new CopyOnWriteArrayList<>();
    public final CarVcuManager.CarVcuEventCallback mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.autoshow.vcu.base.BaseVcuMode.1
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            if (carPropertyValue != null) {
                BaseVcuMode.this.onChangeEvent(carPropertyValue);
            }
        }

        public void onErrorEvent(int i, int i2) {
            BaseVcuMode.this.onErrorEvent(i, i2);
        }
    };

    protected abstract List<Integer> getPropertyIds();

    protected abstract void onChangeEvent(CarPropertyValue carPropertyValue);

    protected abstract void onErrorEvent(int i, int i2);

    @Override // com.xiaopeng.autoshow.vcu.impl.IVcuModel
    public void addVcuChangeListener(IVcuChangeListener iVcuChangeListener) {
        if (this.mVcuListenerList.contains(iVcuChangeListener)) {
            return;
        }
        this.mVcuListenerList.add(iVcuChangeListener);
    }

    @Override // com.xiaopeng.autoshow.vcu.impl.IVcuModel
    public void removeVcuChangeListener(IVcuChangeListener iVcuChangeListener) {
        this.mVcuListenerList.remove(iVcuChangeListener);
    }

    @Override // com.xiaopeng.autoshow.vcu.impl.IVcuModel
    public void registerCallback() {
        try {
            if (this.mCarVcuManager != null) {
                getPropertyIds();
                this.mCarVcuManager.registerPropCallback(this.mPropertyIds, this.mCarVcuEventCallback);
            }
        } catch (CarNotConnectedException e) {
            e.printStackTrace();
        }
    }

    @Override // com.xiaopeng.autoshow.vcu.impl.IVcuModel
    public void unRegisterCallback() {
        try {
            if (this.mCarVcuManager != null) {
                this.mCarVcuManager.unregisterPropCallback(this.mPropertyIds, this.mCarVcuEventCallback);
            }
        } catch (CarNotConnectedException e) {
            e.printStackTrace();
        }
    }

    @Override // com.xiaopeng.autoshow.vcu.impl.IVcuModel
    public boolean isCarGearP() {
        CarVcuManager carVcuManager = this.mCarVcuManager;
        if (carVcuManager != null) {
            try {
                int displayGearLevel = carVcuManager.getDisplayGearLevel();
                String str = this.TAG;
                LogUtils.v(str, "isCarGearP.. getRealGearLevel: " + displayGearLevel);
                return displayGearLevel == 4;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override // com.xiaopeng.autoshow.vcu.impl.IVcuModel
    public boolean isEvSysUnReady() {
        CarVcuManager carVcuManager = this.mCarVcuManager;
        if (carVcuManager != null) {
            try {
                int evSysReady = carVcuManager.getEvSysReady();
                String str = this.TAG;
                LogUtils.v(str, "isEvSysUnReady.. getEvSysReady: " + evSysReady);
                return evSysReady != 2;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override // com.xiaopeng.autoshow.vcu.impl.IVcuModel
    public boolean isExhibitionMode() {
        CarVcuManager carVcuManager = this.mCarVcuManager;
        if (carVcuManager != null) {
            try {
                int exhibModeSwitchStatus = carVcuManager.getExhibModeSwitchStatus();
                String str = this.TAG;
                LogUtils.v(str, "isExhibitionMode.. getExhibModeSwitchStatus: " + exhibModeSwitchStatus);
                return exhibModeSwitchStatus == 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override // com.xiaopeng.autoshow.vcu.impl.IVcuModel
    public boolean checkGearLimit() {
        return isEvSysUnReady() && isCarGearP() && isExhibitionMode();
    }
}
