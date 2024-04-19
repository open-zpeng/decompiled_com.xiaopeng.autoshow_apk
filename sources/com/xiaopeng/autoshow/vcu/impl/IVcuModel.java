package com.xiaopeng.autoshow.vcu.impl;

import android.car.Car;
/* loaded from: classes.dex */
public interface IVcuModel {
    void addVcuChangeListener(IVcuChangeListener iVcuChangeListener);

    boolean checkGearLimit();

    void initVcuManager(Car car);

    boolean isCarGearP();

    boolean isEvSysUnReady();

    boolean isExhibitionMode();

    void registerCallback();

    void removeVcuChangeListener(IVcuChangeListener iVcuChangeListener);

    void unRegisterCallback();
}
