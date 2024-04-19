package com.xiaopeng.autoshow.dialog.exhibitioncar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.SystemProperties;
import android.view.MotionEvent;
import android.view.View;
import com.xiaopeng.autoshow.CarConstant;
import com.xiaopeng.autoshow.Config;
import com.xiaopeng.autoshow.R;
import com.xiaopeng.autoshow.dialog.BasePlatDialog;
import com.xiaopeng.autoshow.dialog.DebugDialog;
import com.xiaopeng.autoshow.dialog.IVerityCallback;
import com.xiaopeng.autoshow.dialog.VerityDialog;
import com.xiaopeng.autoshow.dialog.exhibitioncar.CarExhibitionDialog;
import com.xiaopeng.autoshow.utils.CarVersionUtil;
import com.xiaopeng.autoshow.utils.ContentResolverUtil;
import com.xiaopeng.autoshow.utils.FeedbackUtil;
import com.xiaopeng.autoshow.utils.HintGestureDetector;
import com.xiaopeng.autoshow.utils.LogUtils;
import com.xiaopeng.autoshow.utils.OnGearLimitListener;
import com.xiaopeng.autoshow.vcu.manager.XpVcuManager;
import com.xiaopeng.lib.utils.ThreadUtils;
import com.xiaopeng.lib.utils.info.BuildInfoUtils;
import com.xiaopeng.xui.widget.XButton;
import com.xiaopeng.xui.widget.XLinearLayout;
import com.xiaopeng.xui.widget.XSwitch;
import com.xiaopeng.xui.widget.XTabLayout;
import com.xiaopeng.xui.widget.XTextView;
/* loaded from: classes.dex */
public abstract class CarExhibitionDialog extends BasePlatDialog {
    protected static final String TAG = "AbsExhibitionDialog";
    private final int ACTION_LIMIT_GEAR;
    private final int ACTION_RELIEVE_GEAR;
    protected XLinearLayout mAutoShowDetailLayout;
    private XButton mAutoShowGearBt;
    protected XLinearLayout mAutoShowGearLayout;
    protected XTextView mAutoShowGearTitle;
    private DebugDialog mDebugDialog;
    private HintGestureDetector mDebugGestureDetector;
    protected XTextView mPaddingView;
    private VerityDialog mVerityDialog;

    public CarExhibitionDialog(Context context) {
        super(context);
        this.ACTION_LIMIT_GEAR = 1;
        this.ACTION_RELIEVE_GEAR = 2;
        initDebug();
    }

    private void initDebug() {
        if (BuildInfoUtils.isDebuggableVersion()) {
            this.mDebugGestureDetector = new HintGestureDetector(7, new HintGestureDetector.SimpleHintGestureListener() { // from class: com.xiaopeng.autoshow.dialog.exhibitioncar.CarExhibitionDialog.1
                @Override // com.xiaopeng.autoshow.utils.HintGestureDetector.SimpleHintGestureListener, com.xiaopeng.autoshow.utils.HintGestureDetector.OnHintGestureListener
                public void onTrig() {
                    if (CarExhibitionDialog.this.mDebugDialog == null) {
                        CarExhibitionDialog carExhibitionDialog = CarExhibitionDialog.this;
                        carExhibitionDialog.mDebugDialog = new DebugDialog(carExhibitionDialog.getContext().getApplicationContext());
                    }
                    CarExhibitionDialog.this.mDebugDialog.show();
                }
            });
            if (this.mTitleView != null) {
                this.mTitleView.setOnTouchListener(new View.OnTouchListener() { // from class: com.xiaopeng.autoshow.dialog.exhibitioncar.-$$Lambda$CarExhibitionDialog$kowljvkU-oxyffsFz9UxUsPZfeQ
                    @Override // android.view.View.OnTouchListener
                    public final boolean onTouch(View view, MotionEvent motionEvent) {
                        return CarExhibitionDialog.this.lambda$initDebug$0$CarExhibitionDialog(view, motionEvent);
                    }
                });
            }
        }
    }

    public /* synthetic */ boolean lambda$initDebug$0$CarExhibitionDialog(View view, MotionEvent motionEvent) {
        return this.mDebugGestureDetector.onTouchEvent(motionEvent);
    }

    @Override // com.xiaopeng.autoshow.dialog.BasePlatDialog
    protected void initView() {
        this.mAutoShowGearLayout = (XLinearLayout) findViewById(R.id.autoShow_gear_layout);
        this.mAutoShowGearTitle = (XTextView) findViewById(R.id.autoShow_gear_title);
        this.mAutoShowGearBt = (XButton) findViewById(R.id.autoShow_gear_bt);
        this.mPaddingView = (XTextView) findViewById(R.id.padding_view);
        this.mAutoShowDetailLayout = (XLinearLayout) findViewById(R.id.autoShow_detail_layout);
        this.mAirConditionSwitch = (XSwitch) findViewById(R.id.aircondition_switch);
        this.mLluLayout = (XLinearLayout) findViewById(R.id.llu_layout);
        this.mLluTab = (XTabLayout) findViewById(R.id.llu_tab);
        this.mLLuTips = (XTextView) findViewById(R.id.llu_tips);
        this.mVolumeSwitch = (XSwitch) findViewById(R.id.volume_switch);
        this.mWelcomeSwitch = (XSwitch) findViewById(R.id.welcome_switch);
        if ("1".equals(SystemProperties.get("persist.sys.xiaopeng.LLU", "0"))) {
            this.mLluLayout.setVisibility(0);
        } else {
            this.mLluLayout.setVisibility(8);
        }
        if (CarConstant.CDU_TYPE_D55A.equals(CarVersionUtil.getXpCduType())) {
            findViewById(R.id.welcome_layout).setVisibility(8);
        }
    }

    @Override // com.xiaopeng.autoshow.dialog.BasePlatDialog
    protected void initListener() {
        XpVcuManager.get().addVcuChangeListener(this);
        this.mAutoShowGearBt.setOnClickListener(this);
        if (this.mAirConditionSwitch != null) {
            this.mAirConditionSwitch.setOnCheckedChangeListener(this);
        }
        if (this.mVolumeSwitch != null) {
            this.mVolumeSwitch.setOnCheckedChangeListener(this);
        }
        if (this.mLluTab != null) {
            this.mLluTab.setOnTabChangeListener(this);
        }
        if (this.mWelcomeSwitch != null) {
            this.mWelcomeSwitch.setOnCheckedChangeListener(this);
        }
    }

    @Override // com.xiaopeng.autoshow.dialog.BasePlatDialog
    protected void initSwitchState() {
        boolean checkGearLimit = this.mPresenter.checkGearLimit();
        LogUtils.v(TAG, "isGearLimit:" + checkGearLimit);
        lambda$onGearChange$1$CarExhibitionDialog(checkGearLimit);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: setSwitchState */
    public void lambda$onGearChange$1$CarExhibitionDialog(boolean z) {
        LogUtils.v(TAG, "setSwitchState: isGearLimit:" + z);
        setTextSpan(this.mLLuTips, this.mContext.getString(R.string.llu_title));
        initGearTitleAndBtn(z);
        updateCommonSwitch(z);
        this.mPresenter.saveOperatorNo(z ? this.mPhoneNum : "");
        ContentResolverUtil.putBooleanValue(Config.AUTO_SHOW_SETTING_SWITCH_CONFIG_KEY, z);
        uploadGearState(z);
    }

    private void initGearTitleAndBtn(boolean z) {
        this.mPaddingView.setText(R.string.gear_limit_msg);
        this.mPaddingView.setVisibility(z ? 8 : 0);
        this.mAutoShowDetailLayout.setVisibility(z ? 0 : 8);
        this.mAutoShowGearTitle.setText(z ? R.string.keep_p_gear : R.string.no_vehicles);
        this.mAutoShowGearBt.setText(z ? R.string.relieve_gear : R.string.limit_gear);
    }

    @Override // com.xiaopeng.autoshow.dialog.BasePlatDialog, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        VerityDialog verityDialog = this.mVerityDialog;
        if (verityDialog != null) {
            verityDialog.dismiss();
            this.mVerityDialog = null;
        }
        DebugDialog debugDialog = this.mDebugDialog;
        if (debugDialog != null) {
            debugDialog.dismiss();
            this.mDebugDialog = null;
        }
    }

    @Override // com.xiaopeng.autoshow.dialog.BasePlatDialog, android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.autoShow_gear_bt) {
            showVerityDialog();
        }
        super.onClick(view);
    }

    private void showVerityDialog() {
        if (this.mPresenter.checkGearLimit()) {
            showVerityDialog(2);
        } else {
            showVerityDialog(1);
        }
    }

    private void showVerityDialog(final int i) {
        this.mVerityDialog = new VerityDialog(getContext(), new IVerityCallback() { // from class: com.xiaopeng.autoshow.dialog.exhibitioncar.CarExhibitionDialog.2
            @Override // com.xiaopeng.autoshow.dialog.IVerityCallback
            public void onFail() {
            }

            @Override // com.xiaopeng.autoshow.dialog.IVerityCallback
            public void onSuccess(String str) {
                int i2 = i;
                if (i2 == 1) {
                    CarExhibitionDialog.this.sendLimitGear(str);
                } else if (i2 == 2) {
                    FeedbackUtil.sendRelieveGear(str);
                }
                CarExhibitionDialog.this.mPhoneNum = str;
            }
        });
        this.mVerityDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.autoshow.dialog.exhibitioncar.CarExhibitionDialog$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 implements OnGearLimitListener {
        @Override // com.xiaopeng.autoshow.utils.OnGearLimitListener
        public void error() {
        }

        AnonymousClass3() {
        }

        @Override // com.xiaopeng.autoshow.utils.OnGearLimitListener
        public void success() {
            ThreadUtils.postMainThread(new Runnable() { // from class: com.xiaopeng.autoshow.dialog.exhibitioncar.-$$Lambda$CarExhibitionDialog$3$pJ0jCzh8lvY9XF2-xssgJ3Ep-NM
                @Override // java.lang.Runnable
                public final void run() {
                    CarExhibitionDialog.AnonymousClass3.this.lambda$success$0$CarExhibitionDialog$3();
                }
            });
        }

        public /* synthetic */ void lambda$success$0$CarExhibitionDialog$3() {
            if (CarExhibitionDialog.this.mPaddingView != null) {
                CarExhibitionDialog.this.mPaddingView.setText(CarExhibitionDialog.this.getPaddingTipsId());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendLimitGear(String str) {
        FeedbackUtil.sendLimitGear(str, new AnonymousClass3());
    }

    @Override // com.xiaopeng.autoshow.vcu.impl.IVcuChangeListener
    public void onGearChange(final boolean z) {
        LogUtils.v(TAG, "isGearLimit = " + z);
        ThreadUtils.postMainThread(new Runnable() { // from class: com.xiaopeng.autoshow.dialog.exhibitioncar.-$$Lambda$CarExhibitionDialog$-2j5DVeGb5xxMyiawbQpS6UhhMg
            @Override // java.lang.Runnable
            public final void run() {
                CarExhibitionDialog.this.lambda$onGearChange$1$CarExhibitionDialog(z);
            }
        });
    }

    private void uploadGearState(final boolean z) {
        if (this.mPresenter != null) {
            ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.autoshow.dialog.exhibitioncar.-$$Lambda$CarExhibitionDialog$96ZxgkXvIFiMeiBVZHUPGoykpVY
                @Override // java.lang.Runnable
                public final void run() {
                    CarExhibitionDialog.this.lambda$uploadGearState$2$CarExhibitionDialog(z);
                }
            });
        }
    }

    public /* synthetic */ void lambda$uploadGearState$2$CarExhibitionDialog(boolean z) {
        this.mPresenter.uploadGearState(z ? "1" : "0");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPaddingTipsId() {
        int carType = CarVersionUtil.getCarType();
        return (carType == 7 || carType == 11) ? R.string.gear_limit_apply_tips : R.string.gear_limit_apply_tips2;
    }
}
