package com.xiaopeng.autoshow.eventtracking;
/* loaded from: classes.dex */
public enum EventTrackingParamKeys {
    KEY_RESULT(0, "result"),
    KEY_PHONE(1, "mobile_number"),
    KEY_TYPE(2, "type");
    
    public static final int INVALID_INDEX = -1;
    private int mIndex;
    private String mParamKey;

    EventTrackingParamKeys(int i, String str) {
        this.mIndex = i;
        this.mParamKey = str;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public String getParamKey() {
        return this.mParamKey;
    }

    public static int getIndex(EventTrackingParamKeys eventTrackingParamKeys) {
        for (int i = 0; i < values().length; i++) {
            EventTrackingParamKeys eventTrackingParamKeys2 = values()[i];
            if (eventTrackingParamKeys2.getIndex() == eventTrackingParamKeys.getIndex() && eventTrackingParamKeys2.getParamKey().equals(eventTrackingParamKeys.getParamKey())) {
                return i;
            }
        }
        return -1;
    }
}
