package com.xiaopeng.autoshow.dialog;

import android.content.Context;
import android.widget.CompoundButton;
import com.xiaopeng.autoshow.Config;
import com.xiaopeng.autoshow.R;
import com.xiaopeng.autoshow.eventtracking.EventTrackingHelper;
import com.xiaopeng.autoshow.model.AutoShowModel;
import com.xiaopeng.autoshow.utils.ContentResolverUtil;
import com.xiaopeng.autoshow.utils.LogUtils;
import com.xiaopeng.xui.widget.XLinearLayout;
import com.xiaopeng.xui.widget.XSwitch;
/* loaded from: classes.dex */
public class D21BSettingDialog extends BaseSettingDialog {
    private static final String TAG = "D21BSettingDialog";
    private XLinearLayout mVolumeLayout;
    private XSwitch mVolumeSwitch;

    @Override // com.xiaopeng.autoshow.dialog.BaseSettingDialog
    int getLayout() {
        return R.layout.d21b_dialog_autoshow_setting_layout;
    }

    public D21BSettingDialog(Context context) {
        super(context);
    }

    @Override // com.xiaopeng.autoshow.dialog.BaseSettingDialog
    public void initDialog() {
        this.mVolumeSwitch = (XSwitch) findViewById(R.id.volume_switch);
        this.mVolumeLayout = (XLinearLayout) findViewById(R.id.volume_layout);
        super.initDialog();
    }

    @Override // com.xiaopeng.autoshow.dialog.BaseSettingDialog
    void initItems(boolean z) {
        if (!z) {
            this.mVolumeLayout.setVisibility(8);
            return;
        }
        this.mVolumeSwitch.setChecked(ContentResolverUtil.getBooleanValue(Config.AUTO_SHOW_VOLUME_CONFIG_KEY));
        this.mVolumeLayout.setVisibility(0);
    }

    @Override // com.xiaopeng.autoshow.dialog.BaseSettingDialog
    public void setListener() {
        super.setListener();
        this.mVolumeSwitch.setOnCheckedChangeListener(this);
    }

    @Override // com.xiaopeng.autoshow.dialog.BaseSettingDialog, android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (compoundButton == this.mVolumeSwitch) {
            boolean putBooleanValue = ContentResolverUtil.putBooleanValue(Config.AUTO_SHOW_VOLUME_CONFIG_KEY, z);
            LogUtils.i(TAG, "设置音量减半为：" + z + " ; 是否设置成功" + putBooleanValue);
            AutoShowModel.getInstance().decreaseVolume(z);
            EventTrackingHelper.sendMolecast(4, this.mPhoneNum, Boolean.valueOf(z));
            return;
        }
        super.onCheckedChanged(compoundButton, z);
    }

    @Override // com.xiaopeng.autoshow.dialog.BaseSettingDialog
    protected void onSettingChange(boolean z) {
        this.mVolumeLayout.setVisibility(z ? 0 : 8);
        this.mVolumeSwitch.setChecked(z);
    }
}
