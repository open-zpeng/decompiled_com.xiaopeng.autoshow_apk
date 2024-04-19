package com.xiaopeng.autoshow.dialog.presenter;
/* loaded from: classes.dex */
public interface IDialogPresenter {
    boolean checkGearLimit();

    boolean getConfig(int i);

    int getCurrentModel();

    int getLluTabIndex();

    void onCheckedChanged(int i, boolean z);

    void onModelChanged(int i);

    void onTabChanged(int i);

    void saveOperatorNo(String str);

    void uploadGearState(String str);
}
