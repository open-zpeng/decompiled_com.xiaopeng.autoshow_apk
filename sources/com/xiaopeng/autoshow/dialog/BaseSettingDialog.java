package com.xiaopeng.autoshow.dialog;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import com.xiaopeng.autoshow.AutoShowApp;
import com.xiaopeng.autoshow.Config;
import com.xiaopeng.autoshow.R;
import com.xiaopeng.autoshow.eventtracking.EventTrackingHelper;
import com.xiaopeng.autoshow.model.AutoShowModel;
import com.xiaopeng.autoshow.utils.ContentResolverUtil;
import com.xiaopeng.autoshow.utils.FeedbackUtil;
import com.xiaopeng.autoshow.utils.HintGestureDetector;
import com.xiaopeng.autoshow.utils.LogUtils;
import com.xiaopeng.lib.utils.SharedPreferencesUtils;
import com.xiaopeng.lib.utils.info.BuildInfoUtils;
import com.xiaopeng.libtheme.ThemeManager;
import com.xiaopeng.xui.app.XDialogSystemType;
import com.xiaopeng.xui.widget.XButton;
import com.xiaopeng.xui.widget.XImageButton;
import com.xiaopeng.xui.widget.XLinearLayout;
import com.xiaopeng.xui.widget.XSwitch;
import com.xiaopeng.xui.widget.XTextView;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class BaseSettingDialog extends Dialog implements DialogInterface.OnDismissListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener, ComponentCallbacks, XSwitch.OnInterceptListener {
    private static final String TAG = "BaseSettingDialog";
    private final int ACTION_LIMIT_GEAR;
    private final int ACTION_MAIN_SETTING;
    private final int ACTION_RELIEVE_GEAR;
    private XLinearLayout mAirConditionLayout;
    private XSwitch mAirConditionSwitch;
    private XButton mBtnForbidden;
    private XButton mBtnForbiddenCancel;
    private XImageButton mCloseButton;
    Context mContext;
    private DebugDialog mDebugDialog;
    private HintGestureDetector mDebugGestureDetector;
    private boolean mIsVeritySuccess;
    public String mPhoneNum;
    private PmBroadcastReceiver mPmBroadcastReceiver;
    private XSwitch mSettingSwitch;
    private XTextView mTitleView;
    private VerityDialog mVerityDialog;

    abstract int getLayout();

    abstract void initItems(boolean z);

    @Override // android.content.ComponentCallbacks
    public void onLowMemory() {
    }

    protected abstract void onSettingChange(boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseSettingDialog(Context context) {
        super(context, 2131755379);
        this.ACTION_MAIN_SETTING = 1;
        this.ACTION_LIMIT_GEAR = 2;
        this.ACTION_RELIEVE_GEAR = 3;
        this.mPhoneNum = SharedPreferencesUtils.getInstance(AutoShowApp.getContext()).getString(Config.AUTO_SHOW_USER_PHONE_NUM, "");
        this.mIsVeritySuccess = false;
        this.mContext = context;
        setContentView(getLayout());
        initDialog();
        initDebug();
    }

    private void initDebug() {
        if (BuildInfoUtils.isDebuggableVersion()) {
            this.mDebugGestureDetector = new HintGestureDetector(7, new HintGestureDetector.SimpleHintGestureListener() { // from class: com.xiaopeng.autoshow.dialog.BaseSettingDialog.1
                @Override // com.xiaopeng.autoshow.utils.HintGestureDetector.SimpleHintGestureListener, com.xiaopeng.autoshow.utils.HintGestureDetector.OnHintGestureListener
                public void onTrig() {
                    if (BaseSettingDialog.this.mDebugDialog == null) {
                        BaseSettingDialog baseSettingDialog = BaseSettingDialog.this;
                        baseSettingDialog.mDebugDialog = new DebugDialog(baseSettingDialog.getContext().getApplicationContext());
                    }
                    BaseSettingDialog.this.mDebugDialog.show();
                }
            });
            this.mTitleView.setOnTouchListener(new View.OnTouchListener() { // from class: com.xiaopeng.autoshow.dialog.-$$Lambda$BaseSettingDialog$qjFZmf5VH9xfIKJ-Tm3AE7Kf_Yw
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    return BaseSettingDialog.this.lambda$initDebug$0$BaseSettingDialog(view, motionEvent);
                }
            });
        }
    }

    public /* synthetic */ boolean lambda$initDebug$0$BaseSettingDialog(View view, MotionEvent motionEvent) {
        return this.mDebugGestureDetector.onTouchEvent(motionEvent);
    }

    public void initDialog() {
        ((Window) Objects.requireNonNull(getWindow())).setType(XDialogSystemType.TYPE_SYSTEM_DIALOG);
        this.mSettingSwitch = (XSwitch) findViewById(R.id.setting_switch);
        this.mTitleView = (XTextView) findViewById(R.id.title);
        this.mAirConditionSwitch = (XSwitch) findViewById(R.id.aircondition_switch);
        this.mAirConditionLayout = (XLinearLayout) findViewById(R.id.aircondition_layout);
        this.mCloseButton = (XImageButton) findViewById(R.id.x_dialog_close);
        this.mBtnForbidden = (XButton) findViewById(R.id.btn_forbidden);
        this.mBtnForbiddenCancel = (XButton) findViewById(R.id.btn_forbidden_cancel);
        setSwitchState();
        setListener();
    }

    private void setSwitchState() {
        boolean booleanValue = ContentResolverUtil.getBooleanValue(Config.AUTO_SHOW_SETTING_SWITCH_CONFIG_KEY);
        this.mSettingSwitch.setChecked(booleanValue);
        if (!booleanValue) {
            this.mAirConditionLayout.setVisibility(8);
        } else {
            this.mAirConditionSwitch.setChecked(ContentResolverUtil.getBooleanValue(Config.AUTO_SHOW_AIRCONDITION_CONFIG_KEY));
            this.mAirConditionLayout.setVisibility(0);
        }
        initItems(booleanValue);
    }

    public void setListener() {
        this.mBtnForbiddenCancel.setOnClickListener(this);
        this.mBtnForbidden.setOnClickListener(this);
        setOnDismissListener(this);
        this.mSettingSwitch.setOnInterceptListener(this);
        this.mAirConditionSwitch.setOnCheckedChangeListener(this);
        this.mCloseButton.setOnClickListener(this);
    }

    @Override // android.app.Dialog
    public void show() {
        if (this.mContext != null) {
            if (this.mPmBroadcastReceiver == null) {
                this.mPmBroadcastReceiver = new PmBroadcastReceiver();
            }
            this.mContext.registerReceiver(this.mPmBroadcastReceiver, new IntentFilter("com.xiaopeng.broadcast.ACTION_PM_STATUS_CHANGE"));
            this.mContext.registerComponentCallbacks(this);
        }
        super.show();
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        PmBroadcastReceiver pmBroadcastReceiver = this.mPmBroadcastReceiver;
        if (pmBroadcastReceiver != null) {
            Context context = this.mContext;
            if (context != null) {
                context.unregisterReceiver(pmBroadcastReceiver);
                this.mContext.unregisterComponentCallbacks(this);
            }
            this.mPmBroadcastReceiver = null;
        }
        VerityDialog verityDialog = this.mVerityDialog;
        if (verityDialog != null) {
            verityDialog.dismiss();
        }
        this.mVerityDialog = null;
        DebugDialog debugDialog = this.mDebugDialog;
        if (debugDialog != null) {
            debugDialog.dismiss();
        }
        this.mDebugDialog = null;
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (compoundButton == this.mAirConditionSwitch) {
            boolean putBooleanValue = ContentResolverUtil.putBooleanValue(Config.AUTO_SHOW_AIRCONDITION_CONFIG_KEY, z);
            LogUtils.i(TAG, "设置限制空调使用为：" + z + " ; 是否设置成功" + putBooleanValue);
            EventTrackingHelper.sendMolecast(2, this.mPhoneNum, Boolean.valueOf(z));
        }
    }

    @Override // com.xiaopeng.xui.widget.XSwitch.OnInterceptListener
    public boolean onInterceptCheck(View view, boolean z) {
        if (view == this.mSettingSwitch) {
            if (z) {
                if (!this.mIsVeritySuccess) {
                    showVerityDialog(1);
                    return true;
                }
            } else {
                setMainSetting(z);
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setMainSetting(boolean z) {
        this.mIsVeritySuccess = z;
        if (z && AutoShowModel.isDay()) {
            LogUtils.d(TAG, "is day");
            ThemeManager.applyDayNightMode(AutoShowApp.getContext(), 2);
        }
        this.mAirConditionLayout.setVisibility(z ? 0 : 8);
        boolean putBooleanValue = ContentResolverUtil.putBooleanValue(Config.AUTO_SHOW_SETTING_SWITCH_CONFIG_KEY, z);
        LogUtils.i(TAG, "设置总开关为：" + z + " ; 是否设置成功" + putBooleanValue);
        EventTrackingHelper.sendMolecast(1, this.mPhoneNum, Boolean.valueOf(z));
        if (!z) {
            SharedPreferencesUtils.getInstance(AutoShowApp.getContext()).putString(Config.AUTO_SHOW_USER_PHONE_NUM, "");
        } else {
            SharedPreferencesUtils.getInstance(AutoShowApp.getContext()).putString(Config.AUTO_SHOW_USER_PHONE_NUM, this.mPhoneNum);
        }
        this.mAirConditionSwitch.setChecked(z);
        onSettingChange(z);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_forbidden /* 2131296392 */:
                showVerityDialog(2);
                return;
            case R.id.btn_forbidden_cancel /* 2131296393 */:
                showVerityDialog(3);
                return;
            case R.id.x_dialog_close /* 2131296685 */:
                dismiss();
                return;
            default:
                return;
        }
    }

    private void showVerityDialog(final int i) {
        this.mVerityDialog = new VerityDialog(getContext(), new IVerityCallback() { // from class: com.xiaopeng.autoshow.dialog.BaseSettingDialog.2
            @Override // com.xiaopeng.autoshow.dialog.IVerityCallback
            public void onSuccess(String str) {
                int i2 = i;
                if (i2 == 1) {
                    BaseSettingDialog baseSettingDialog = BaseSettingDialog.this;
                    baseSettingDialog.mPhoneNum = str;
                    baseSettingDialog.setMainSetting(true);
                    BaseSettingDialog.this.mSettingSwitch.setChecked(true);
                } else if (i2 == 2) {
                    FeedbackUtil.sendLimitGear(str);
                } else if (i2 != 3) {
                } else {
                    FeedbackUtil.sendRelieveGear(str);
                }
            }

            @Override // com.xiaopeng.autoshow.dialog.IVerityCallback
            public void onFail() {
                if (i == 1) {
                    LogUtils.i(BaseSettingDialog.TAG, "短信验证失败，关闭总开关");
                    BaseSettingDialog.this.mIsVeritySuccess = false;
                    BaseSettingDialog.this.mSettingSwitch.setChecked(false);
                }
            }
        });
        this.mVerityDialog.show();
    }

    @Override // android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.x_bg_dialog);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class PmBroadcastReceiver extends BroadcastReceiver {
        private PmBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra("status", -1) == 1) {
                LogUtils.d(BaseSettingDialog.TAG, "收到下电广播，dimiss dialog");
                BaseSettingDialog.this.dismiss();
            }
        }
    }
}
