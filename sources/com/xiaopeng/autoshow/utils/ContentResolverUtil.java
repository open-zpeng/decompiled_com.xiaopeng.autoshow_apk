package com.xiaopeng.autoshow.utils;

import android.provider.Settings;
import com.xiaopeng.autoshow.AutoShowApp;
/* loaded from: classes.dex */
public class ContentResolverUtil {
    private static final int BOOL_FALSE = 0;
    private static final int BOOL_TRUE = 1;

    public static boolean putBooleanValue(String str, boolean z) {
        return Settings.System.putInt(AutoShowApp.getInstance().getContentResolver(), str, z ? 1 : 0);
    }

    public static boolean getBooleanValue(String str) {
        return Settings.System.getInt(AutoShowApp.getInstance().getContentResolver(), str, 0) == 1;
    }

    public static boolean putIntValue(String str, int i) {
        return Settings.System.putInt(AutoShowApp.getInstance().getContentResolver(), str, i);
    }

    public static int getIntValue(String str) {
        return Settings.System.getInt(AutoShowApp.getInstance().getContentResolver(), str, 0);
    }
}
