package com.xiaopeng.autoshow.dialog.model;
/* loaded from: classes.dex */
public interface IDialogModel {
    public static final int EVENT_AIR = 2;
    public static final int EVENT_LIMIT_GEAR = 6;
    public static final int EVENT_LIMIT_VOLUME = 4;
    public static final int EVENT_LLU = 3;
    public static final int EVENT_WELCOME = 5;
    public static final int EXHIBITION_MODE = 2;
    public static final int EXIT_MODE = 0;
    public static final int TEST_DRIVE_MODEL = 1;

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
