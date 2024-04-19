package com.xiaopeng.autoshow.dialog;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import com.xiaopeng.autoshow.AutoShowApp;
import com.xiaopeng.autoshow.Config;
import com.xiaopeng.autoshow.R;
import com.xiaopeng.autoshow.dialog.presenter.DialogPresenter;
import com.xiaopeng.autoshow.dialog.presenter.IDialogPresenter;
import com.xiaopeng.autoshow.eventtracking.EventTrackingHelper;
import com.xiaopeng.autoshow.model.AutoShowModel;
import com.xiaopeng.autoshow.service.IServiceView;
import com.xiaopeng.autoshow.utils.LogUtils;
import com.xiaopeng.autoshow.vcu.impl.IVcuChangeListener;
import com.xiaopeng.autoshow.vcu.manager.XpVcuManager;
import com.xiaopeng.lib.utils.SharedPreferencesUtils;
import com.xiaopeng.libtheme.ThemeManager;
import com.xiaopeng.xui.app.XDialogSystemType;
import com.xiaopeng.xui.widget.XImageButton;
import com.xiaopeng.xui.widget.XLinearLayout;
import com.xiaopeng.xui.widget.XSwitch;
import com.xiaopeng.xui.widget.XTabLayout;
import com.xiaopeng.xui.widget.XTextView;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class BasePlatDialog extends Dialog implements DialogInterface.OnDismissListener, ComponentCallbacks, View.OnClickListener, CompoundButton.OnCheckedChangeListener, XTabLayout.OnTabChangeListener, IVcuChangeListener {
    protected final String TAG;
    protected XSwitch mAirConditionSwitch;
    private XImageButton mCloseButton;
    protected Context mContext;
    protected XTextView mLLuTips;
    protected XLinearLayout mLluLayout;
    protected XTabLayout mLluTab;
    protected String mPhoneNum;
    private PmBroadcastReceiver mPmBroadcastReceiver;
    protected IDialogPresenter mPresenter;
    private IServiceView mServiceView;
    protected XTextView mTitleView;
    protected XSwitch mVolumeSwitch;
    protected XSwitch mWelcomeSwitch;

    protected abstract int getLayout();

    protected abstract void initListener();

    protected abstract void initSwitchState();

    protected abstract void initView();

    @Override // com.xiaopeng.xui.widget.XTabLayout.OnTabChangeListener
    public boolean onInterceptTabChange(XTabLayout xTabLayout, int i, boolean z, boolean z2) {
        return false;
    }

    @Override // android.content.ComponentCallbacks
    public void onLowMemory() {
    }

    @Override // com.xiaopeng.xui.widget.XTabLayout.OnTabChangeListener
    public void onTabChangeStart(XTabLayout xTabLayout, int i, boolean z, boolean z2) {
    }

    public BasePlatDialog(Context context) {
        super(context, 2131755379);
        this.TAG = getClass().getSimpleName();
        this.mPhoneNum = SharedPreferencesUtils.getInstance(AutoShowApp.getContext()).getString(Config.AUTO_SHOW_USER_PHONE_NUM, "");
        this.mContext = context;
        setContentView(getLayout());
        findView();
        initListener();
        initSwitchState();
        setNightMode();
        setOnDismissListener(this);
        setCanceledOnTouchOutside(false);
    }

    private void setNightMode() {
        if (AutoShowModel.isDay()) {
            LogUtils.d(this.TAG, "is day");
            ThemeManager.applyDayNightMode(AutoShowApp.getContext(), 2);
        }
    }

    protected void findView() {
        ((Window) Objects.requireNonNull(getWindow())).setType(XDialogSystemType.TYPE_SYSTEM_DIALOG);
        this.mTitleView = (XTextView) findViewById(R.id.title);
        this.mCloseButton = (XImageButton) findViewById(R.id.x_dialog_close);
        this.mCloseButton.setOnClickListener(this);
        this.mPresenter = new DialogPresenter();
        initView();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTextSpan(XTextView xTextView, String str) {
        if (TextUtils.isEmpty(str) || xTextView == null) {
            return;
        }
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.x_theme_text_03, null)), 4, str.length(), 33);
        spannableString.setSpan(new VerticalAlignTextSpan(24), 4, str.length(), 33);
        xTextView.setText(spannableString);
    }

    public void showDialog(IServiceView iServiceView) {
        this.mServiceView = iServiceView;
        if (this.mContext != null) {
            if (this.mPmBroadcastReceiver == null) {
                this.mPmBroadcastReceiver = new PmBroadcastReceiver();
            }
            this.mContext.registerReceiver(this.mPmBroadcastReceiver, new IntentFilter("com.xiaopeng.broadcast.ACTION_PM_STATUS_CHANGE"));
            this.mContext.registerComponentCallbacks(this);
        }
        show();
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
                LogUtils.d(BasePlatDialog.this.TAG, "收到下电广播，dimiss dialog");
                BasePlatDialog.this.dismiss();
            }
        }
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        IDialogPresenter iDialogPresenter;
        if (compoundButton == this.mAirConditionSwitch) {
            IDialogPresenter iDialogPresenter2 = this.mPresenter;
            if (iDialogPresenter2 != null) {
                iDialogPresenter2.onCheckedChanged(2, z);
                EventTrackingHelper.sendMolecast(2, this.mPhoneNum, Boolean.valueOf(z));
            }
        } else if (compoundButton == this.mWelcomeSwitch) {
            IDialogPresenter iDialogPresenter3 = this.mPresenter;
            if (iDialogPresenter3 != null) {
                iDialogPresenter3.onCheckedChanged(5, z);
                EventTrackingHelper.sendMolecast(5, this.mPhoneNum, Boolean.valueOf(z));
            }
        } else if (compoundButton != this.mVolumeSwitch || (iDialogPresenter = this.mPresenter) == null) {
        } else {
            iDialogPresenter.onCheckedChanged(4, z);
            EventTrackingHelper.sendMolecast(4, this.mPhoneNum, Boolean.valueOf(z));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateCommonSwitch(boolean z) {
        if (z) {
            if (this.mAirConditionSwitch != null) {
                this.mAirConditionSwitch.setChecked(this.mPresenter.getConfig(2));
            }
            if (this.mLluTab != null) {
                this.mLluTab.selectTab(this.mPresenter.getLluTabIndex());
            }
            if (this.mVolumeSwitch != null) {
                this.mVolumeSwitch.setChecked(this.mPresenter.getConfig(4));
            }
            if (this.mWelcomeSwitch != null) {
                this.mWelcomeSwitch.setChecked(this.mPresenter.getConfig(5));
                return;
            }
            return;
        }
        XSwitch xSwitch = this.mAirConditionSwitch;
        if (xSwitch != null) {
            xSwitch.setChecked(false);
        }
        XTabLayout xTabLayout = this.mLluTab;
        if (xTabLayout != null) {
            xTabLayout.selectTab(0);
        }
        XSwitch xSwitch2 = this.mVolumeSwitch;
        if (xSwitch2 != null) {
            xSwitch2.setChecked(false);
        }
        XSwitch xSwitch3 = this.mWelcomeSwitch;
        if (xSwitch3 != null) {
            xSwitch3.setChecked(false);
        }
    }

    @Override // com.xiaopeng.xui.widget.XTabLayout.OnTabChangeListener
    public void onTabChangeEnd(XTabLayout xTabLayout, int i, boolean z, boolean z2) {
        IDialogPresenter iDialogPresenter = this.mPresenter;
        if (iDialogPresenter != null) {
            iDialogPresenter.onTabChanged(i);
            if (z2) {
                EventTrackingHelper.sendMolecast(3, this.mPhoneNum, String.valueOf(i));
            }
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() != R.id.x_dialog_close) {
            return;
        }
        dismiss();
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        LogUtils.v(this.TAG, "dismiss");
        super.dismiss();
        XpVcuManager.get().removeVcuChangeListener(this);
        IServiceView iServiceView = this.mServiceView;
        if (iServiceView != null) {
            iServiceView.onStopSelf();
        }
    }
}
