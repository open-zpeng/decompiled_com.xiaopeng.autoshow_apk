package com.xiaopeng.autoshow.eventtracking;

import com.xiaopeng.autoshow.Config;
import com.xiaopeng.autoshow.eventtracking.EventBean;
import com.xiaopeng.autoshow.utils.CarVersionUtil;
import com.xiaopeng.autoshow.utils.ContentResolverUtil;
import com.xiaopeng.autoshow.utils.LogUtils;
import com.xiaopeng.datalog.DataLogModuleEntry;
import com.xiaopeng.lib.framework.module.Module;
import com.xiaopeng.lib.framework.moduleinterface.datalogmodule.IDataLog;
import com.xiaopeng.lib.framework.moduleinterface.datalogmodule.IMoleEvent;
import com.xiaopeng.lib.framework.moduleinterface.datalogmodule.IMoleEventBuilder;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class EventTrackingHelper {
    public static final int EVENT_AIR = 2;
    public static final int EVENT_LIMIT_GEAR = 6;
    public static final int EVENT_LIMIT_SOUND = 4;
    public static final int EVENT_LLU = 3;
    public static final int EVENT_MAIN_SETTING = 1;
    public static final int EVENT_WELCOME = 5;
    private static final String TAG = "EventTrackingHelper";
    private static final String TRACKING_MODULE_NAME = "autoshow";

    public static void sendMolecast(int i, Object... objArr) {
        int carType = CarVersionUtil.getCarType();
        int intValue = ContentResolverUtil.getIntValue(Config.AUTO_SHOW_MODEL_SETTING_CONFIG_KEY);
        if (carType == 5) {
            sendMolecast(getPageEnumByCar(), getEventEnum(i), objArr);
        } else if (carType != 7 || intValue == 1) {
        } else {
            sendMolecast(getPageEnumByCar(), getEventEnum(i), objArr);
        }
    }

    private static EventEnum getEventEnum(int i) {
        int carType = CarVersionUtil.getCarType();
        if (carType != 5) {
            if (carType != 7) {
                return null;
            }
            return getE28EventEnumByType(i);
        }
        return getD21BEventEnumByType(i);
    }

    private static EventEnum getE28EventEnumByType(int i) {
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i != 5) {
                        if (i != 6) {
                            return null;
                        }
                        return EventEnum.E28_LIMIT_GEAR_SETTING;
                    }
                    return EventEnum.E28_WELCOME_SETTING;
                }
                return EventEnum.E28_LLU_SETTING;
            }
            return EventEnum.E28_AIR_SETTING;
        }
        return EventEnum.E28_MAIN_SETTING;
    }

    private static EventEnum getD21BEventEnumByType(int i) {
        if (i != 1) {
            if (i != 2) {
                if (i != 4) {
                    if (i != 6) {
                        return null;
                    }
                    return EventEnum.D21B_LIMIT_GEAR_SETTING;
                }
                return EventEnum.D21B_LIMIT_SOUND_SETTING;
            }
            return EventEnum.D21B_AIR_SETTING;
        }
        return EventEnum.D21B_MAIN_SETTING;
    }

    private static PagesEnum getPageEnumByCar() {
        int carType = CarVersionUtil.getCarType();
        if (carType != 5) {
            if (carType == 7) {
                return PagesEnum.E28_AUTO_SHOW_PAGE;
            }
            return PagesEnum.E28_AUTO_SHOW_PAGE;
        }
        return PagesEnum.D21B_AUTO_SHOW_PAGE;
    }

    private static void sendMolecast(PagesEnum pagesEnum, EventEnum eventEnum, Object... objArr) {
        HashMap hashMap = new HashMap();
        if (eventEnum == null) {
            return;
        }
        EventTrackingParamKeys[] paramsIndex = eventEnum.getParamsIndex();
        if (paramsIndex != null && objArr != null) {
            if (paramsIndex.length != objArr.length) {
                LogUtils.w(TAG, " params key and value number not match!");
                return;
            }
            for (int i = 0; i < objArr.length; i++) {
                Object obj = objArr[i];
                boolean z = obj instanceof Boolean;
                if (!z && !(obj instanceof Character) && !(obj instanceof Number) && !(obj instanceof String)) {
                    LogUtils.w(TAG, "args type must be Boolean, Character, Number, String");
                    return;
                }
                if (z) {
                    hashMap.put(paramsIndex[i].getParamKey(), Integer.valueOf(((Boolean) obj).booleanValue() ? 1 : 0));
                } else {
                    hashMap.put(paramsIndex[i].getParamKey(), obj);
                }
            }
        }
        EventBean.Builder buttonId = new EventBean.Builder().pageId(pagesEnum.getPageId()).pageName(pagesEnum.getPageName()).buttonId(eventEnum.getEventId());
        if (hashMap.size() > 0) {
            buttonId.setParams(hashMap);
        }
        sendMolecast(buttonId.build());
    }

    private static IDataLog getDataLog() {
        return (IDataLog) Module.get(DataLogModuleEntry.class).get(IDataLog.class);
    }

    private static void sendMolecast(EventBean eventBean) {
        LogUtils.d(TAG, "send molecast event bean  = " + eventBean.toString());
        IDataLog dataLog = getDataLog();
        IMoleEventBuilder buttonId = dataLog.buildMoleEvent().setModule("autoshow").setPageId(eventBean.getPageId()).setButtonId(eventBean.getButtonId());
        if (eventBean.getParams() != null) {
            for (Map.Entry<String, Object> entry : eventBean.getParams().entrySet()) {
                if (entry.getValue() instanceof String) {
                    buttonId.setProperty(entry.getKey(), (String) entry.getValue());
                } else if (entry.getValue() instanceof Number) {
                    buttonId.setProperty(entry.getKey(), (Number) entry.getValue());
                } else if (entry.getValue() instanceof Character) {
                    buttonId.setProperty(entry.getKey(), ((Character) entry.getValue()).charValue());
                } else if (entry.getValue() instanceof Boolean) {
                    buttonId.setProperty(entry.getKey(), ((Boolean) entry.getValue()).booleanValue());
                } else {
                    LogUtils.w(TAG, " params type invalid");
                }
            }
        }
        buttonId.setProperty("level", Integer.valueOf(eventBean.getLevel()));
        if (dataLog != null) {
            IMoleEvent build = buttonId.build();
            LogUtils.d(TAG, "send molecast mole event = " + build.toJson());
            dataLog.sendStatData(build);
        }
    }
}
