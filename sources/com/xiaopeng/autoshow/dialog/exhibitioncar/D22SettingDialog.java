package com.xiaopeng.autoshow.dialog.exhibitioncar;

import android.content.Context;
import com.xiaopeng.autoshow.R;
import com.xiaopeng.xui.widget.XTabLayout;
/* loaded from: classes.dex */
public class D22SettingDialog extends CarExhibitionDialog implements XTabLayout.OnTabChangeListener {
    @Override // com.xiaopeng.autoshow.dialog.BasePlatDialog
    protected int getLayout() {
        return R.layout.d22_dialog_autoshow_setting_layout;
    }

    public D22SettingDialog(Context context) {
        super(context);
    }
}
