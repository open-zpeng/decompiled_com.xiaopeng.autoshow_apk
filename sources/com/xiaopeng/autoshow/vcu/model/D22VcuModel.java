package com.xiaopeng.autoshow.vcu.model;

import android.car.Car;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.vcu.CarVcuManager;
import com.xiaopeng.autoshow.Config;
import com.xiaopeng.autoshow.utils.ContentResolverUtil;
import com.xiaopeng.autoshow.utils.LogUtils;
import com.xiaopeng.autoshow.vcu.base.BaseVcuMode;
import com.xiaopeng.autoshow.vcu.impl.IVcuChangeListener;
import com.xiaopeng.lib.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class D22VcuModel extends BaseVcuMode {
    @Override // com.xiaopeng.autoshow.vcu.impl.IVcuModel
    public void initVcuManager(final Car car) {
        ThreadUtils.postBackground(new Runnable() { // from class: com.xiaopeng.autoshow.vcu.model.-$$Lambda$D22VcuModel$-x96NNl-SqaXy3LGiIBFoVq6t0A
            @Override // java.lang.Runnable
            public final void run() {
                D22VcuModel.this.lambda$initVcuManager$0$D22VcuModel(car);
            }
        });
    }

    public /* synthetic */ void lambda$initVcuManager$0$D22VcuModel(Car car) {
        try {
            if (car != null) {
                this.mCarVcuManager = (CarVcuManager) car.getCarManager("xp_vcu");
                LogUtils.i(this.TAG, "registerCallback");
                registerCallback();
            } else {
                LogUtils.i(this.TAG, "carClient is null");
            }
        } catch (Exception e) {
            LogUtils.e(this.TAG, "car not connected");
            e.printStackTrace();
        }
    }

    @Override // com.xiaopeng.autoshow.vcu.base.BaseVcuMode
    protected List<Integer> getPropertyIds() {
        if (this.mPropertyIds == null) {
            this.mPropertyIds = new ArrayList();
        }
        if (!this.mPropertyIds.contains(557847056)) {
            this.mPropertyIds.add(557847056);
        }
        if (!this.mPropertyIds.contains(557847100)) {
            this.mPropertyIds.add(557847100);
        }
        if (!this.mPropertyIds.contains(557847137)) {
            this.mPropertyIds.add(557847137);
        }
        return this.mPropertyIds;
    }

    @Override // com.xiaopeng.autoshow.vcu.base.BaseVcuMode
    protected void onChangeEvent(CarPropertyValue carPropertyValue) {
        String str = this.TAG;
        LogUtils.v(str, "carPropertyValue.getPropertyId() = " + carPropertyValue.getPropertyId());
        int propertyId = carPropertyValue.getPropertyId();
        if (propertyId == 557847056 || propertyId == 557847100 || propertyId == 557847137) {
            boolean checkGearLimit = checkGearLimit();
            if (checkGearLimit) {
                boolean putIntValue = ContentResolverUtil.putIntValue(Config.AUTO_SHOW_MODEL_SETTING_CONFIG_KEY, 0);
                String str2 = this.TAG;
                LogUtils.i(str2, "value:0 ; result:" + putIntValue);
            }
            String str3 = this.TAG;
            LogUtils.i(str3, "mVcuListenerList.szie:" + this.mVcuListenerList.size());
            Iterator<IVcuChangeListener> it = this.mVcuListenerList.iterator();
            while (it.hasNext()) {
                it.next().onGearChange(checkGearLimit);
            }
        }
    }

    @Override // com.xiaopeng.autoshow.vcu.base.BaseVcuMode
    protected void onErrorEvent(int i, int i2) {
        String str = this.TAG;
        LogUtils.e(str, "carVcuEventCallback onErrorEvent:" + i + " & " + i2);
    }
}
