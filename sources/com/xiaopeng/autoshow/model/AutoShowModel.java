package com.xiaopeng.autoshow.model;

import android.content.Context;
import android.content.Intent;
import android.media.AudioConfig.AudioConfig;
import com.xiaopeng.autoshow.AutoShowApp;
import com.xiaopeng.autoshow.Config;
import com.xiaopeng.autoshow.dialog.D21BSettingDialog;
import com.xiaopeng.autoshow.dialog.exhibitioncar.D22SettingDialog;
import com.xiaopeng.autoshow.dialog.exhibitioncar.D55SettingDialog;
import com.xiaopeng.autoshow.dialog.exhibitioncar.E28SettingDialog;
import com.xiaopeng.autoshow.dialog.testdrive.D22TestDriveDialog;
import com.xiaopeng.autoshow.dialog.testdrive.D55TestDriveDialog;
import com.xiaopeng.autoshow.dialog.testdrive.E28TestDriveDialog;
import com.xiaopeng.autoshow.llu.LluCycleService;
import com.xiaopeng.autoshow.llu.MusicLluPresenter;
import com.xiaopeng.autoshow.service.IServiceView;
import com.xiaopeng.autoshow.utils.CarVersionUtil;
import com.xiaopeng.autoshow.utils.ContentResolverUtil;
import com.xiaopeng.autoshow.utils.LogUtils;
import com.xiaopeng.lib.utils.ThreadUtils;
import com.xiaopeng.libtheme.ThemeManager;
/* loaded from: classes.dex */
public class AutoShowModel {
    private static final String TAG = "AutoShowModel";
    private AudioConfig mAudioConfig;
    private MusicLluPresenter mMusicLluPresenter;

    public static AutoShowModel getInstance() {
        return Holder.INSTANCE;
    }

    public static boolean isDay() {
        return !ThemeManager.isNightMode(AutoShowApp.getContext());
    }

    public void init() {
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.autoshow.model.-$$Lambda$AutoShowModel$bP9oIzyu8ZaUGrFBfapNwLYhCHw
            @Override // java.lang.Runnable
            public final void run() {
                AutoShowModel.this.lambda$init$0$AutoShowModel();
            }
        });
    }

    public /* synthetic */ void lambda$init$0$AutoShowModel() {
        boolean booleanValue = ContentResolverUtil.getBooleanValue(Config.AUTO_SHOW_SETTING_SWITCH_CONFIG_KEY);
        LogUtils.i(TAG, "展车模式初始化，总开关状态：" + booleanValue);
        boolean z = false;
        boolean z2 = ContentResolverUtil.getIntValue(Config.AUTO_SHOW_MODEL_SETTING_CONFIG_KEY) == 1;
        LogUtils.i(TAG, "试驾模式初始化，总开关状态：" + z2);
        if (booleanValue || z2) {
            z = true;
        }
        int carType = CarVersionUtil.getCarType();
        if (carType == 5) {
            initD21BSetting(booleanValue);
            return;
        }
        if (carType != 7) {
            switch (carType) {
                case 9:
                    initD22Setting(z);
                    return;
                case 10:
                    initD55Setting(z);
                    return;
                case 11:
                    break;
                default:
                    return;
            }
        }
        initE28Setting(z);
    }

    private void initD55Setting(boolean z) {
        if (!z) {
            getInstance().doLlu(0);
            return;
        }
        int intValue = ContentResolverUtil.getIntValue(Config.AUTO_SHOW_SETTING_LIGHTING_LANGUAGE_KEY);
        LogUtils.i(TAG, "展车模式初始化，灯语开关状态：" + intValue);
        getInstance().doLlu(intValue);
    }

    private void initD22Setting(boolean z) {
        if (!z) {
            getInstance().decreaseVolume(false);
            getInstance().doLlu(0);
            return;
        }
        boolean booleanValue = ContentResolverUtil.getBooleanValue(Config.AUTO_SHOW_VOLUME_CONFIG_KEY);
        LogUtils.i(TAG, "展车模式初始化，音量减半开关状态：" + booleanValue);
        getInstance().decreaseVolume(booleanValue);
        int intValue = ContentResolverUtil.getIntValue(Config.AUTO_SHOW_SETTING_LIGHTING_LANGUAGE_KEY);
        LogUtils.i(TAG, "展车模式初始化，灯语开关状态：" + intValue);
        getInstance().doLlu(intValue);
    }

    private void initE28Setting(boolean z) {
        if (!z) {
            getInstance().doLlu(0);
            return;
        }
        int intValue = ContentResolverUtil.getIntValue(Config.AUTO_SHOW_SETTING_LIGHTING_LANGUAGE_KEY);
        LogUtils.i(TAG, "展车模式初始化，灯语开关状态：" + intValue);
        getInstance().doLlu(intValue);
    }

    private void initD21BSetting(boolean z) {
        if (!z) {
            getInstance().decreaseVolume(false);
            return;
        }
        boolean booleanValue = ContentResolverUtil.getBooleanValue(Config.AUTO_SHOW_VOLUME_CONFIG_KEY);
        LogUtils.i(TAG, "展车模式初始化，音量减半开关状态：" + booleanValue);
        getInstance().decreaseVolume(booleanValue);
    }

    public void decreaseVolume(boolean z) {
        if (this.mAudioConfig == null) {
            this.mAudioConfig = new AudioConfig(AutoShowApp.getInstance());
        }
        this.mAudioConfig.setMusicLimitMode(z);
    }

    public void startAutoShow(IServiceView iServiceView) {
        int carType = CarVersionUtil.getCarType();
        if (carType != 5) {
            if (carType != 7) {
                switch (carType) {
                    case 9:
                        new D22SettingDialog(AutoShowApp.getInstance()).showDialog(iServiceView);
                        return;
                    case 10:
                        new D55SettingDialog(AutoShowApp.getInstance()).showDialog(iServiceView);
                        return;
                    case 11:
                        break;
                    default:
                        LogUtils.d(TAG, "startAutoShow（），沒有对应车型");
                        return;
                }
            }
            new E28SettingDialog(AutoShowApp.getInstance()).showDialog(iServiceView);
            return;
        }
        new D21BSettingDialog(AutoShowApp.getInstance()).show();
    }

    public void startTestDriveModel(IServiceView iServiceView) {
        switch (CarVersionUtil.getCarType()) {
            case 7:
            case 11:
                new E28TestDriveDialog(AutoShowApp.getInstance()).showDialog(iServiceView);
                return;
            case 8:
            default:
                return;
            case 9:
                new D22TestDriveDialog(AutoShowApp.getInstance()).showDialog(iServiceView);
                return;
            case 10:
                new D55TestDriveDialog(AutoShowApp.getInstance()).showDialog(iServiceView);
                return;
        }
    }

    private void startCycleLlu(boolean z) {
        Context context = AutoShowApp.getContext();
        Intent intent = new Intent(context, LluCycleService.class);
        if (z) {
            context.startService(intent);
        } else {
            context.stopService(intent);
        }
    }

    private MusicLluPresenter getMusicLluPresenter() {
        if (this.mMusicLluPresenter == null) {
            this.mMusicLluPresenter = new MusicLluPresenter();
        }
        return this.mMusicLluPresenter;
    }

    public void doLlu(int i) {
        if (i == 0) {
            startCycleLlu(false);
            getMusicLluPresenter().release();
        } else if (i == 1) {
            getMusicLluPresenter().release();
            startCycleLlu(true);
        } else if (i != 2) {
        } else {
            startCycleLlu(false);
            getMusicLluPresenter().init();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Holder {
        static final AutoShowModel INSTANCE = new AutoShowModel();

        private Holder() {
        }
    }
}
