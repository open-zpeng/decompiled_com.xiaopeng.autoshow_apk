package com.xiaopeng.autoshow.dialog.presenter;

import com.xiaopeng.autoshow.dialog.model.DialogModel;
import com.xiaopeng.autoshow.dialog.model.IDialogModel;
/* loaded from: classes.dex */
public class DialogPresenter implements IDialogPresenter {
    private IDialogModel mDialogModel = new DialogModel();

    @Override // com.xiaopeng.autoshow.dialog.presenter.IDialogPresenter
    public void onCheckedChanged(int i, boolean z) {
        IDialogModel iDialogModel = this.mDialogModel;
        if (iDialogModel != null) {
            iDialogModel.onCheckedChanged(i, z);
        }
    }

    @Override // com.xiaopeng.autoshow.dialog.presenter.IDialogPresenter
    public void onTabChanged(int i) {
        IDialogModel iDialogModel = this.mDialogModel;
        if (iDialogModel != null) {
            iDialogModel.onTabChanged(i);
        }
    }

    @Override // com.xiaopeng.autoshow.dialog.presenter.IDialogPresenter
    public boolean getConfig(int i) {
        IDialogModel iDialogModel = this.mDialogModel;
        if (iDialogModel != null) {
            return iDialogModel.getConfig(i);
        }
        return false;
    }

    @Override // com.xiaopeng.autoshow.dialog.presenter.IDialogPresenter
    public int getLluTabIndex() {
        IDialogModel iDialogModel = this.mDialogModel;
        if (iDialogModel != null) {
            return iDialogModel.getLluTabIndex();
        }
        return 0;
    }

    @Override // com.xiaopeng.autoshow.dialog.presenter.IDialogPresenter
    public void onModelChanged(int i) {
        IDialogModel iDialogModel = this.mDialogModel;
        if (iDialogModel != null) {
            iDialogModel.onModelChanged(i);
        }
    }

    @Override // com.xiaopeng.autoshow.dialog.presenter.IDialogPresenter
    public int getCurrentModel() {
        IDialogModel iDialogModel = this.mDialogModel;
        if (iDialogModel != null) {
            return iDialogModel.getCurrentModel();
        }
        return 0;
    }

    @Override // com.xiaopeng.autoshow.dialog.presenter.IDialogPresenter
    public boolean checkGearLimit() {
        IDialogModel iDialogModel = this.mDialogModel;
        if (iDialogModel != null) {
            return iDialogModel.checkGearLimit();
        }
        return false;
    }

    @Override // com.xiaopeng.autoshow.dialog.presenter.IDialogPresenter
    public void saveOperatorNo(String str) {
        IDialogModel iDialogModel = this.mDialogModel;
        if (iDialogModel != null) {
            iDialogModel.saveOperatorNo(str);
        }
    }

    @Override // com.xiaopeng.autoshow.dialog.presenter.IDialogPresenter
    public void uploadGearState(String str) {
        IDialogModel iDialogModel = this.mDialogModel;
        if (iDialogModel != null) {
            iDialogModel.uploadGearState(str);
        }
    }
}
