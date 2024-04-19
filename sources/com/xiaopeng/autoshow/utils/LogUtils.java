package com.xiaopeng.autoshow.utils;

import android.text.TextUtils;
import android.util.Log;
import java.util.HashMap;
import java.util.Locale;
/* loaded from: classes.dex */
public final class LogUtils {
    private static String APP_TAG = "AutoShow";
    private static boolean isDebug = true;
    private static HashMap<String, String> sCachedTag = new HashMap<>();

    public static void isDebug(boolean z) {
        isDebug = z;
    }

    private LogUtils() {
    }

    public static void i(String str) {
        if (isDebug) {
            Log.i(buildTag(APP_TAG), buildMessage(str));
        }
    }

    public static void d(String str) {
        if (isDebug) {
            Log.d(buildTag(APP_TAG), buildMessage(str));
        }
    }

    public static void w(String str) {
        if (isDebug) {
            Log.w(buildTag(APP_TAG), buildMessage(str));
        }
    }

    public static void e(String str) {
        if (isDebug) {
            Log.e(buildTag(APP_TAG), buildMessage(str));
        }
    }

    public static void v(String str) {
        if (isDebug) {
            Log.v(buildTag(APP_TAG), buildMessage(str));
        }
    }

    public static void wtf(String str) {
        if (isDebug) {
            Log.wtf(buildTag(APP_TAG), buildMessage(str));
        }
    }

    public static void json(String str) {
        if (isDebug) {
            Log.v(buildTag(APP_TAG), buildMessage(str));
        }
    }

    public static void i(String str, String str2) {
        if (isDebug) {
            Log.i(buildTag(str), buildMessage(str2));
        }
    }

    public static void d(String str, String str2) {
        if (isDebug) {
            Log.i(buildTag(str), buildMessage(str2));
        }
    }

    public static void w(String str, String str2) {
        if (isDebug) {
            Log.w(buildTag(str), buildMessage(str2));
        }
    }

    public static void e(String str, String str2) {
        if (isDebug) {
            Log.e(buildTag(str), buildMessage(str2));
        }
    }

    public static void v(String str, String str2) {
        if (isDebug) {
            Log.i(buildTag(str), buildMessage(str2));
        }
    }

    public static void wtf(String str, String str2) {
        if (isDebug) {
            Log.wtf(buildTag(str), buildMessage(str2));
        }
    }

    private static synchronized String buildTag(String str) {
        String str2;
        synchronized (LogUtils.class) {
            if (!sCachedTag.containsKey(str)) {
                if (APP_TAG.equals(str)) {
                    sCachedTag.put(str, APP_TAG);
                } else {
                    HashMap<String, String> hashMap = sCachedTag;
                    hashMap.put(str, APP_TAG + "_" + str);
                }
            }
            str2 = sCachedTag.get(str);
        }
        return str2;
    }

    private static String buildMessage(String str) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace == null || stackTrace.length < 4) {
            return str;
        }
        StackTraceElement stackTraceElement = stackTrace[4];
        String fileName = stackTraceElement.getFileName();
        if (TextUtils.isEmpty(fileName)) {
            return str;
        }
        return String.format(Locale.US, "(%s:%d) %s", fileName, Integer.valueOf(stackTraceElement.getLineNumber()), str);
    }
}
