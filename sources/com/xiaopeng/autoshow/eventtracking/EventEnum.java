package com.xiaopeng.autoshow.eventtracking;
/* loaded from: classes.dex */
public enum EventEnum {
    E28_MAIN_SETTING("B001", "展车模式总开关", new EventTrackingParamKeys[]{EventTrackingParamKeys.KEY_PHONE, EventTrackingParamKeys.KEY_RESULT}),
    E28_LLU_SETTING("B002", "灯语效果设置", new EventTrackingParamKeys[]{EventTrackingParamKeys.KEY_PHONE, EventTrackingParamKeys.KEY_TYPE}),
    E28_AIR_SETTING("B003", "限制空调功能开关", new EventTrackingParamKeys[]{EventTrackingParamKeys.KEY_PHONE, EventTrackingParamKeys.KEY_RESULT}),
    E28_WELCOME_SETTING("B004", "扬声器随门升降开关", new EventTrackingParamKeys[]{EventTrackingParamKeys.KEY_PHONE, EventTrackingParamKeys.KEY_RESULT}),
    E28_LIMIT_GEAR_SETTING("B005", "档位限制开关", new EventTrackingParamKeys[]{EventTrackingParamKeys.KEY_PHONE, EventTrackingParamKeys.KEY_RESULT}),
    D21B_MAIN_SETTING("B001", "展车模式总开关", new EventTrackingParamKeys[]{EventTrackingParamKeys.KEY_PHONE, EventTrackingParamKeys.KEY_RESULT}),
    D21B_AIR_SETTING("B002", "限制空调功能开关", new EventTrackingParamKeys[]{EventTrackingParamKeys.KEY_PHONE, EventTrackingParamKeys.KEY_RESULT}),
    D21B_LIMIT_SOUND_SETTING("B003", "限制音量开关", new EventTrackingParamKeys[]{EventTrackingParamKeys.KEY_PHONE, EventTrackingParamKeys.KEY_RESULT}),
    D21B_LIMIT_GEAR_SETTING("B004", "档位限制开关", new EventTrackingParamKeys[]{EventTrackingParamKeys.KEY_PHONE, EventTrackingParamKeys.KEY_RESULT});
    
    private String mEventId;
    private String mEventName;
    private int mLevel;
    private EventTrackingParamKeys[] mParamsKeys;

    EventEnum(String str, String str2, int i) {
        this.mEventId = str;
        this.mEventName = str2;
        this.mLevel = i;
        this.mParamsKeys = null;
    }

    EventEnum(String str, String str2, EventTrackingParamKeys[] eventTrackingParamKeysArr) {
        this.mEventId = str;
        this.mEventName = str2;
        this.mLevel = 0;
        this.mParamsKeys = eventTrackingParamKeysArr;
    }

    EventEnum(String str, String str2) {
        this.mEventId = str;
        this.mEventName = str2;
        this.mLevel = 0;
        this.mParamsKeys = null;
    }

    EventEnum(String str, String str2, int i, EventTrackingParamKeys[] eventTrackingParamKeysArr) {
        this.mEventId = str;
        this.mEventName = str2;
        this.mLevel = i;
        this.mParamsKeys = eventTrackingParamKeysArr;
    }

    public String getEventId() {
        return this.mEventId;
    }

    public int getLevel() {
        return this.mLevel;
    }

    public EventTrackingParamKeys[] getParamsIndex() {
        return this.mParamsKeys;
    }
}
