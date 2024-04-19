package com.xiaopeng.autoshow.llu;

import android.car.Car;
import android.car.CarNotConnectedException;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.xiaopeng.autoshow.AutoShowApp;
import com.xiaopeng.autoshow.R;
import com.xiaopeng.autoshow.utils.CarVersionUtil;
import com.xiaopeng.autoshow.utils.LogUtils;
import com.xiaopeng.autoshow.vcu.manager.XpVcuManager;
import com.xiaopeng.xui.app.XToast;
import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.lightlanuage.LightLanuageManager;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class LluManager {
    private static final int DELAYED_TIME = 10000;
    private static final int START_CAR_SERVICE = 102;
    private static final int START_XUI_SERVICE = 101;
    private static final int STATUS_INVALID = -1;
    private static final int STOP_CAR_SERVICE = 104;
    private static final int STOP_XUI_SERVICE = 103;
    private static String TAG = "LluManager";
    private static LluManager sLluManager;
    private Car mCarApiClient;
    private CarBcmManager mCarBcmManager;
    private IMusicLluListener mIMusicLluListener;
    private ICycleLluListener mIcycleListener;
    private LightLanuageManager mLightLanguageManager;
    public ArrayList mPropertyIds;
    private XUIManager mXuiApiClient;
    private boolean debugLlu = false;
    private int mBcmHazardLightStatus = -1;
    private int mBcmFrontLightStatus = -1;
    private int mLeftTurnLampStatus = -1;
    private int mRightTurnLampStatus = -1;
    private CarBcmManager.CarBcmEventCallback mCarBcmEventCallback = new CarBcmManager.CarBcmEventCallback() { // from class: com.xiaopeng.autoshow.llu.LluManager.1
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            LluManager.this.handleBcmChangeEvent(carPropertyValue);
        }

        public void onErrorEvent(int i, int i2) {
            LluManager.this.handleBcmErrorEvent(i);
        }
    };
    private LightLanuageManager.EventListener mXuiSmartEventListener = new LightLanuageManager.EventListener() { // from class: com.xiaopeng.autoshow.llu.LluManager.2
        public void onUpgrade(int i, int i2) {
            String str = LluManager.TAG;
            LogUtils.d(str, "onLightEffectFinishEvent effectName:" + i + " effectMode:" + i2);
        }

        public void onStart(String str, String str2) {
            boolean z = !str2.equals(LluEffectType.LLU_AUT0_SHOW);
            LogUtils.d(LluManager.TAG, "onLightEffectShowStartEvent effectName:" + str + " effectType:" + str2 + "isOtherLight:" + z);
            if (!z || LluManager.this.mIcycleListener == null) {
                return;
            }
            LluManager.this.mIcycleListener.onInterruptLlu(false);
        }

        public void onFinish(String str, String str2) {
            String str3 = LluManager.TAG;
            LogUtils.d(str3, "onLightEffectShowFinishEvent effectName:" + str + " effectType:" + str2);
            if (LluManager.this.mIcycleListener != null) {
                LluManager.this.mIcycleListener.onContinueLlu();
            }
        }

        public void onError(String str, int i) {
            String str2 = LluManager.TAG;
            LogUtils.d(str2, "onLightEffectShowError effectName:" + str + " errorCode:" + i);
        }
    };
    private final Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.xiaopeng.autoshow.llu.LluManager.3
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 101:
                    removeMessages(103);
                    LluManager.this.initXuiApi();
                    return;
                case 102:
                    removeMessages(104);
                    LluManager.this.initCarApi();
                    return;
                case 103:
                    if (hasMessages(101)) {
                        return;
                    }
                    LluManager.this.releaseXuiApiInternal();
                    return;
                case 104:
                    if (hasMessages(102)) {
                        return;
                    }
                    LluManager.this.releaseCarApiInternal();
                    return;
                default:
                    return;
            }
        }
    };
    private Context mContext = AutoShowApp.getInstance();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum TurnLampState {
        Off,
        Left,
        Right,
        Both
    }

    private LluManager() {
    }

    public static LluManager getInstance() {
        if (sLluManager == null) {
            synchronized (LluManager.class) {
                if (sLluManager == null) {
                    sLluManager = new LluManager();
                }
            }
        }
        return sLluManager;
    }

    public void connectApi() {
        this.mHandler.sendEmptyMessage(102);
        this.mHandler.sendEmptyMessage(101);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initCarApi() {
        if (hasConnectCarApi()) {
            LogUtils.d(TAG, "has connect car api...");
            getLampStatus();
            return;
        }
        connectCarApi();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initXuiApi() {
        if (hasConnectXuiApi()) {
            LogUtils.d(TAG, "has connect xui api...");
            if (onInterceptCheck()) {
                return;
            }
            dispatchMusicLlu();
            dispatchCycleLlu();
            return;
        }
        connectXuiApi();
    }

    private void initCarApiInternal() {
        LogUtils.i(TAG, "init Car Api start");
        this.mCarApiClient = Car.createCar(this.mContext, new ServiceConnection() { // from class: com.xiaopeng.autoshow.llu.LluManager.4
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                LogUtils.i(LluManager.TAG, "car service connected ...");
                LluManager.this.initBcmCarManager();
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                LogUtils.i(LluManager.TAG, "car service dis connected ...");
                LluManager.this.releaseCarApiInternal();
            }
        });
        this.mCarApiClient.connect();
        LogUtils.i(TAG, "init Car Api end");
    }

    private void initXuiApiInternal() {
        LogUtils.i(TAG, "init Xui Api start");
        this.mXuiApiClient = XUIManager.createXUIManager(this.mContext, new ServiceConnection() { // from class: com.xiaopeng.autoshow.llu.LluManager.5
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                LogUtils.i(LluManager.TAG, "xui service connected ...");
                LluManager.this.initXuiSmartManager();
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                LogUtils.i(LluManager.TAG, "xui service dis connected ...");
                LluManager.this.releaseXuiApiInternal();
            }
        });
        this.mXuiApiClient.connect();
        LogUtils.i(TAG, "init Xui Api end");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initBcmCarManager() {
        try {
            if (this.mCarBcmManager == null) {
                LogUtils.i(TAG, "init BcmCarManager start");
                this.mCarBcmManager = (CarBcmManager) this.mCarApiClient.getCarManager("xp_bcm");
            }
            getPropertyIds();
            this.mCarBcmManager.registerPropCallback(this.mPropertyIds, this.mCarBcmEventCallback);
        } catch (CarNotConnectedException e) {
            String str = TAG;
            LogUtils.i(str, "Can not get CarBcmManager:" + e);
        }
        LogUtils.i(TAG, "init BcmCarManager end");
        getLampStatus();
    }

    protected List<Integer> getPropertyIds() {
        if (this.mPropertyIds == null) {
            this.mPropertyIds = new ArrayList();
        }
        if (!this.mPropertyIds.contains(557849653)) {
            this.mPropertyIds.add(557849653);
        }
        if (!this.mPropertyIds.contains(557849640)) {
            this.mPropertyIds.add(557849640);
        }
        if (!this.mPropertyIds.contains(557849624)) {
            this.mPropertyIds.add(557849624);
        }
        if (!this.mPropertyIds.contains(557915292)) {
            this.mPropertyIds.add(557915292);
        }
        return this.mPropertyIds;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initXuiSmartManager() {
        try {
            if (this.mLightLanguageManager == null) {
                LogUtils.i(TAG, "init XuiSmartManager start");
                this.mLightLanguageManager = (LightLanuageManager) this.mXuiApiClient.getXUIServiceManager("lightlanuage");
            }
            this.mLightLanguageManager.registerListener(this.mXuiSmartEventListener);
        } catch (Exception e) {
            String str = TAG;
            LogUtils.i(str, "Can not get XUIService:" + e);
        }
        LogUtils.i(TAG, "init XuiSmartManager end");
        if (onInterceptCheck()) {
            return;
        }
        dispatchMusicLlu();
        dispatchCycleLlu();
    }

    private void dispatchCycleLlu() {
        boolean isAllOff = isAllOff();
        if (this.mIcycleListener != null) {
            String str = TAG;
            LogUtils.i(str, " is start cycle llu :" + isAllOff);
            this.mIcycleListener.onStartLlu(isAllOff);
        }
    }

    private void dispatchMusicLlu() {
        IMusicLluListener iMusicLluListener = this.mIMusicLluListener;
        if (iMusicLluListener != null) {
            iMusicLluListener.onStartMusicLlu();
        }
    }

    private void getLampStatus() {
        if (this.debugLlu) {
            this.mBcmHazardLightStatus = 0;
            this.mBcmFrontLightStatus = 0;
            this.mRightTurnLampStatus = 0;
            this.mLeftTurnLampStatus = 0;
            return;
        }
        try {
            if (this.mCarBcmManager != null) {
                this.mRightTurnLampStatus = this.mCarBcmManager.getRightTurnLampStatus();
                this.mLeftTurnLampStatus = this.mCarBcmManager.getLeftTurnLampStatus();
                if (this.mRightTurnLampStatus == 0 && this.mLeftTurnLampStatus == 0) {
                    this.mBcmHazardLightStatus = 0;
                }
                this.mBcmFrontLightStatus = this.mCarBcmManager.getHeadLampGroup();
                String str = TAG;
                LogUtils.i(str, "RightTurnLampStatus:" + this.mRightTurnLampStatus + ";mLeftTurnLampStatus:" + this.mLeftTurnLampStatus + ";mBcmFrontLightStatus:" + this.mBcmFrontLightStatus + ";");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isAllOff() {
        int carType = CarVersionUtil.getCarType();
        return (carType == 7 || carType == 11) ? this.mBcmHazardLightStatus == 0 && this.mBcmFrontLightStatus == 0 && this.mRightTurnLampStatus == 0 && this.mLeftTurnLampStatus == 0 : this.mBcmHazardLightStatus == 0 && this.mRightTurnLampStatus == 0 && this.mLeftTurnLampStatus == 0;
    }

    private boolean isOpenHazardLight() {
        return this.mBcmHazardLightStatus == 1;
    }

    private boolean isOpenFrontLight() {
        int i = this.mBcmFrontLightStatus;
        return i == 3 || i == 1 || i == 2;
    }

    private boolean isOpenRightLampLight() {
        return this.mRightTurnLampStatus == 1;
    }

    private boolean isOpenLeftLampLight() {
        return this.mLeftTurnLampStatus == 1;
    }

    public void startPlayLlu(String str) {
        if (TextUtils.isEmpty(str)) {
            LogUtils.d(TAG, "effectName is empty");
        }
        LightLanuageManager lightLanuageManager = this.mLightLanguageManager;
        if (lightLanuageManager == null) {
            LogUtils.d(TAG, "startPlayLlu: mXuiSmartManager == null");
            return;
        }
        try {
            lightLanuageManager.playEffect(str, -1);
        } catch (XUIServiceNotConnectedException e) {
            String str2 = TAG;
            LogUtils.d(str2, "LangLightEffectMode exception:" + e);
        }
    }

    public void stopPlayLlu() {
        LightLanuageManager lightLanuageManager = this.mLightLanguageManager;
        if (lightLanuageManager == null) {
            LogUtils.d(TAG, "stopPlayLlu: mXuiSmartManager == null");
            return;
        }
        try {
            lightLanuageManager.stopEffect();
        } catch (XUIServiceNotConnectedException e) {
            String str = TAG;
            LogUtils.d(str, "stopLightEffectShow exception:" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBcmChangeEvent(CarPropertyValue carPropertyValue) {
        int intValue;
        int intValue2;
        int intValue3;
        int propertyId = carPropertyValue.getPropertyId();
        Object value = carPropertyValue.getValue();
        String str = TAG;
        LogUtils.d(str, "propertyId:" + propertyId + " propertyValue = " + value);
        switch (propertyId) {
            case 557849623:
                if (!(value instanceof Integer) || this.mLeftTurnLampStatus == (intValue = ((Integer) value).intValue())) {
                    return;
                }
                this.mLeftTurnLampStatus = intValue;
                dispatchLluListener(isOpenLeftLampLight());
                return;
            case 557849624:
                String str2 = TAG;
                LogUtils.d(str2, "CarBcmEvent==>onChangeEvent:" + value);
                if (!(value instanceof Integer) || this.mRightTurnLampStatus == (intValue2 = ((Integer) value).intValue())) {
                    return;
                }
                this.mRightTurnLampStatus = intValue2;
                dispatchLluListener(isOpenRightLampLight());
                return;
            case 557849640:
                String str3 = TAG;
                LogUtils.d(str3, "CarBcmEvent==>ID_BCM_FRONT_LIGHT_GROUP_MODE:" + value);
                if (value instanceof Integer) {
                    int intValue4 = ((Integer) value).intValue();
                    int carType = CarVersionUtil.getCarType();
                    if (carType == 7 || carType == 11) {
                        if (this.mBcmFrontLightStatus != intValue4) {
                            this.mBcmFrontLightStatus = intValue4;
                            dispatchLluListener(isOpenFrontLight());
                            return;
                        }
                        return;
                    }
                    dispatchLluListener(false);
                    return;
                }
                return;
            case 557849653:
                String str4 = TAG;
                LogUtils.d(str4, "CarBcmEvent==>onChangeEvent:" + value);
                if (!(value instanceof Integer) || this.mBcmHazardLightStatus == (intValue3 = ((Integer) value).intValue())) {
                    return;
                }
                this.mBcmHazardLightStatus = intValue3;
                dispatchLluListener(isOpenHazardLight());
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBcmErrorEvent(int i) {
        LogUtils.d(TAG, "CarBcmEvent==>onErrorEvent...");
        switch (i) {
            case 557849624:
                this.mRightTurnLampStatus = -1;
                return;
            case 557849640:
                this.mBcmFrontLightStatus = -1;
                return;
            case 557849653:
                this.mBcmHazardLightStatus = -1;
                return;
            case 557915292:
                this.mLeftTurnLampStatus = -1;
                return;
            default:
                return;
        }
    }

    private void dispatchLluListener(boolean z) {
        String str = TAG;
        LogUtils.i(str, "is Interrupt:" + z);
        if (!z) {
            dispatchCycleLlu();
            return;
        }
        ICycleLluListener iCycleLluListener = this.mIcycleListener;
        if (iCycleLluListener != null) {
            iCycleLluListener.onInterruptLlu(true);
        }
    }

    public void setMusicLlu(boolean z) {
        LightLanuageManager lightLanuageManager = this.mLightLanguageManager;
        if (lightLanuageManager != null) {
            try {
                lightLanuageManager.setRhythmEffectEnable(z);
            } catch (XUIServiceNotConnectedException e) {
                String str = TAG;
                LogUtils.d(str, "pauseMusicLlu exception:" + e);
            }
        }
    }

    public void connectCarApi() {
        Car car = this.mCarApiClient;
        if (car == null) {
            initCarApiInternal();
        } else if (car.isConnecting() || this.mCarApiClient.isConnected()) {
        } else {
            LogUtils.i(TAG, " restart connect CarApiClient ....");
            this.mCarApiClient.connect();
        }
    }

    public void connectXuiApi() {
        XUIManager xUIManager = this.mXuiApiClient;
        if (xUIManager == null) {
            initXuiApiInternal();
        } else if (xUIManager.isConnecting() || this.mXuiApiClient.isConnected()) {
        } else {
            LogUtils.i(TAG, " restart connect XuiApiClient ....");
            this.mXuiApiClient.connect();
        }
    }

    public boolean hasConnectXuiApi() {
        XUIManager xUIManager = this.mXuiApiClient;
        return xUIManager != null && xUIManager.isConnected();
    }

    public boolean hasConnectCarApi() {
        Car car = this.mCarApiClient;
        return car != null && car.isConnected();
    }

    public void release() {
        releaseCarApi();
        releaseXuiApi();
    }

    private void releaseCarApi() {
        this.mHandler.sendEmptyMessageDelayed(104, 10000L);
    }

    private void releaseXuiApi() {
        this.mHandler.sendEmptyMessageDelayed(103, 10000L);
    }

    public void releaseCarApiInternal() {
        if (this.mCarApiClient != null) {
            LogUtils.d(TAG, "release CarApi disconnect ");
            this.mCarApiClient.disconnect();
        }
        if (this.mCarBcmManager != null) {
            LogUtils.d(TAG, "CarBcmManager unregisterCallback ");
            try {
                this.mCarBcmManager.unregisterPropCallback(this.mPropertyIds, this.mCarBcmEventCallback);
            } catch (CarNotConnectedException e) {
                String str = TAG;
                LogUtils.e(str, "CarNotConnectedException:" + e);
            }
        }
    }

    public void releaseXuiApiInternal() {
        if (this.mXuiApiClient != null) {
            LogUtils.d(TAG, "release XuiApi disconnect ");
            this.mXuiApiClient.disconnect();
        }
        if (this.mLightLanguageManager != null) {
            LogUtils.d(TAG, "XuiSmartManager unregisterCallback ");
            try {
                this.mLightLanguageManager.unregisterListener(this.mXuiSmartEventListener);
            } catch (Exception e) {
                String str = TAG;
                LogUtils.e(str, "XUIServiceNotConnectedException:" + e);
            }
        }
    }

    public void setIcyclelluListener(ICycleLluListener iCycleLluListener) {
        this.mIcycleListener = iCycleLluListener;
    }

    public void setIMusicLluListener(IMusicLluListener iMusicLluListener) {
        this.mIMusicLluListener = iMusicLluListener;
    }

    public boolean onInterceptCheck() {
        boolean isCarGearP = isCarGearP();
        boolean isTurnLampOn = isTurnLampOn();
        boolean isFogLampOn = isFogLampOn();
        String str = TAG;
        LogUtils.d(str, "onInterceptCheck currentGear=" + isCarGearP + ", isTurnLampOn=" + isTurnLampOn + ", isFogLampOn=" + isFogLampOn);
        int i = (!isCarGearP || isTurnLampOn || isFogLampOn) ? R.string.llu_effect_condition_disable : 0;
        if (i != 0) {
            XToast.showShort(i);
            return true;
        }
        return false;
    }

    public boolean isCarGearP() {
        return XpVcuManager.get().isCarGearP();
    }

    public boolean isTurnLampOn() {
        return getTurnLampState() != TurnLampState.Off;
    }

    public boolean isFogLampOn() {
        int i;
        try {
            i = this.mCarBcmManager.getRearFogLamp();
        } catch (Exception e) {
            LogUtils.d("getRearFogLamp: " + e.getMessage());
            i = 0;
        }
        return i == 1;
    }

    public TurnLampState getTurnLampState() {
        int[] iArr;
        try {
            iArr = this.mCarBcmManager.getLeftAndRightTurnLampsActiveStatus();
        } catch (Exception e) {
            LogUtils.d("getTurnLampState: " + e.getMessage());
            iArr = null;
        }
        if (iArr == null || iArr.length < 2) {
            return TurnLampState.Off;
        }
        if (iArr[0] == 1 && iArr[1] == 1) {
            return TurnLampState.Both;
        }
        if (iArr[0] == 1) {
            return TurnLampState.Left;
        }
        if (iArr[1] == 1) {
            return TurnLampState.Right;
        }
        return TurnLampState.Off;
    }
}
