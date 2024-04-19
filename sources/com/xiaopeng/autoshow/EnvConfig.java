package com.xiaopeng.autoshow;

import com.xiaopeng.lib.utils.SharedPreferencesUtils;
import com.xiaopeng.lib.utils.info.BuildInfoUtils;
/* loaded from: classes.dex */
public class EnvConfig {
    private static final String ENV_CONFIG = "env";
    public static final int ENV_PRODUCT = 2;
    public static final int ENV_TEST = 1;

    public static int getEnv() {
        if (BuildInfoUtils.isDebuggableVersion()) {
            return SharedPreferencesUtils.getInstance(AutoShowApp.getContext()).getInt(ENV_CONFIG, 2);
        }
        return 2;
    }

    public static void switchTest() {
        SharedPreferencesUtils.getInstance(AutoShowApp.getContext()).putInt(ENV_CONFIG, 1);
    }

    public static void switchProduct() {
        SharedPreferencesUtils.getInstance(AutoShowApp.getContext()).putInt(ENV_CONFIG, 2);
    }
}
