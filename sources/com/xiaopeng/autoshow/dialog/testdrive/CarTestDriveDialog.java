package com.xiaopeng.autoshow.dialog.testdrive;

import android.content.Context;
import android.os.SystemProperties;
import android.view.View;
import android.widget.CompoundButton;
import com.xiaopeng.autoshow.CarConstant;
import com.xiaopeng.autoshow.R;
import com.xiaopeng.autoshow.dialog.BasePlatDialog;
import com.xiaopeng.autoshow.utils.CarVersionUtil;
import com.xiaopeng.autoshow.vcu.manager.XpVcuManager;
import com.xiaopeng.xui.widget.XLinearLayout;
import com.xiaopeng.xui.widget.XSwitch;
import com.xiaopeng.xui.widget.XTabLayout;
import com.xiaopeng.xui.widget.XTextView;
/* loaded from: classes.dex */
public abstract class CarTestDriveDialog extends BasePlatDialog {
    protected XTextView mPaddingView;
    protected XLinearLayout mTestDriveDetailView;
    protected XSwitch mTestDriveSwitch;

    public CarTestDriveDialog(Context context) {
        super(context);
    }

    @Override // com.xiaopeng.autoshow.dialog.BasePlatDialog
    protected void initView() {
        this.mTestDriveDetailView = (XLinearLayout) findViewById(R.id.testdrive_detail_layout);
        this.mPaddingView = (XTextView) findViewById(R.id.padding_view);
        this.mTestDriveSwitch = (XSwitch) findViewById(R.id.testdrive_setting_switch);
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
        this.mTestDriveSwitch.setOnCheckedChangeListener(this);
        if (this.mAirConditionSwitch != null) {
            this.mAirConditionSwitch.setOnCheckedChangeListener(this);
        }
        if (this.mLluTab != null) {
            this.mLluTab.setOnTabChangeListener(this);
        }
        if (this.mVolumeSwitch != null) {
            this.mVolumeSwitch.setOnCheckedChangeListener(this);
        }
        if (this.mWelcomeSwitch != null) {
            this.mWelcomeSwitch.setOnCheckedChangeListener(this);
        }
    }

    @Override // com.xiaopeng.autoshow.dialog.BasePlatDialog
    protected void initSwitchState() {
        boolean z = this.mPresenter.getCurrentModel() == 1;
        this.mTestDriveSwitch.setChecked(z);
        this.mTestDriveDetailView.setVisibility(z ? 0 : 8);
        this.mPaddingView.setVisibility(z ? 8 : 0);
        setTextSpan(this.mLLuTips, this.mContext.getString(R.string.llu_title));
        updateCommonSwitch(z);
    }

    @Override // com.xiaopeng.autoshow.dialog.BasePlatDialog, android.view.View.OnClickListener
    public void onClick(View view) {
        super.onClick(view);
        view.getId();
    }

    @Override // com.xiaopeng.autoshow.dialog.BasePlatDialog, android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (compoundButton == this.mTestDriveSwitch) {
            this.mPresenter.onModelChanged(z ? 1 : 0);
            initSwitchState();
            return;
        }
        super.onCheckedChanged(compoundButton, z);
    }

    @Override // com.xiaopeng.autoshow.vcu.impl.IVcuChangeListener
    public void onGearChange(boolean z) {
        dismiss();
    }
}
