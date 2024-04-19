package com.xiaopeng.autoshow.eventtracking;
/* loaded from: classes.dex */
public enum PagesEnum {
    E28_AUTO_SHOW_PAGE("P10005", "E28展车模式页面"),
    D21B_AUTO_SHOW_PAGE("P10006", "D21B展车模式页面");
    
    private String mPageId;
    private String mPageName;

    PagesEnum(String str, String str2) {
        this.mPageId = str;
        this.mPageName = str2;
    }

    public String getPageId() {
        return this.mPageId;
    }

    public String getPageName() {
        return this.mPageName;
    }
}
