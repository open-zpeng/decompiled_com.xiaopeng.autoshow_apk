package com.xiaopeng.autoshow.dialog.exhibitioncar;

import android.content.Context;
import com.xiaopeng.autoshow.R;
import com.xiaopeng.xui.widget.XTabLayout;
/* loaded from: classes.dex */
public class E28SettingDialog extends CarExhibitionDialog implements XTabLayout.OnTabChangeListener {
    @Override // com.xiaopeng.autoshow.dialog.BasePlatDialog
    protected int getLayout() {
        return R.layout.e28_dialog_autoshow_setting_layout;
    }

    public E28SettingDialog(Context context) {
        super(context);
    }
}
