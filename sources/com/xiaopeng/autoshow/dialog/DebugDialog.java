package com.xiaopeng.autoshow.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xiaopeng.autoshow.EnvConfig;
import com.xiaopeng.autoshow.R;
import com.xiaopeng.xui.app.XDialog;
import com.xiaopeng.xui.app.XDialogInterface;
import com.xiaopeng.xui.widget.XTabLayout;
/* loaded from: classes.dex */
public class DebugDialog implements XTabLayout.OnTabChangeListener {
    private XDialog mDialog;

    @Override // com.xiaopeng.xui.widget.XTabLayout.OnTabChangeListener
    public void onTabChangeEnd(XTabLayout xTabLayout, int i, boolean z, boolean z2) {
    }

    @Override // com.xiaopeng.xui.widget.XTabLayout.OnTabChangeListener
    public void onTabChangeStart(XTabLayout xTabLayout, int i, boolean z, boolean z2) {
    }

    public DebugDialog(Context context) {
        this.mDialog = new XDialog(context, (int) R.style.XDialogView);
        this.mDialog.setTitle("Debug").setNegativeButtonEnable(true).setNegativeButton("关闭", new XDialogInterface.OnClickListener() { // from class: com.xiaopeng.autoshow.dialog.-$$Lambda$DebugDialog$wJmiLk5Vq9bL8xN3m65zWUadcqs
            @Override // com.xiaopeng.xui.app.XDialogInterface.OnClickListener
            public final void onClick(XDialog xDialog, int i) {
                xDialog.dismiss();
            }
        });
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_debug, (ViewGroup) null);
        this.mDialog.setCustomView(inflate);
        if (this.mDialog.getDialog().getWindow() != null) {
            this.mDialog.getDialog().getWindow().setType(2048);
        }
        this.mDialog.getDialog().setCanceledOnTouchOutside(false);
        XTabLayout xTabLayout = (XTabLayout) inflate.findViewById(R.id.tabLayout);
        int env = EnvConfig.getEnv();
        if (env == 1) {
            xTabLayout.selectTab(0);
        } else if (env == 2) {
            xTabLayout.selectTab(1);
        }
        xTabLayout.setOnTabChangeListener(this);
    }

    public void show() {
        if (this.mDialog.getDialog().isShowing()) {
            return;
        }
        this.mDialog.show();
    }

    public void dismiss() {
        XDialog xDialog = this.mDialog;
        if (xDialog != null) {
            xDialog.dismiss();
        }
    }

    @Override // com.xiaopeng.xui.widget.XTabLayout.OnTabChangeListener
    public boolean onInterceptTabChange(XTabLayout xTabLayout, int i, boolean z, boolean z2) {
        if (z2) {
            if (i == 0) {
                EnvConfig.switchTest();
                return false;
            }
            EnvConfig.switchProduct();
            return false;
        }
        return false;
    }
}
