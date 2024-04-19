package com.xiaopeng.lib.framework.netchannelmodule.common;

import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;
import com.xiaopeng.lib.utils.LogUtils;
import java.io.File;
/* loaded from: classes.dex */
public class SharedPrefsUtil {
    private static final String FILE_EXTENSION = ".xml";
    private static final String SHARED_PREFS_PATH = "/data/data/%s/shared_prefs";
    private static final String TAG = "NetChannel-SharedPrefsUtil";

    public static void clearNetChannelSharedPrefs(Context context) {
        String str;
        if (context == null) {
            LogUtils.i(TAG, "Clear local SharedPrefs file failed!");
            return;
        }
        if (Build.VERSION.SDK_INT >= 24) {
            str = PreferenceManager.getDefaultSharedPreferencesName(context) + FILE_EXTENSION;
        } else {
            str = context.getPackageName() + "_preferences" + FILE_EXTENSION;
        }
        File file = new File(String.format(SHARED_PREFS_PATH, context.getPackageName()), str);
        if (file.exists()) {
            long length = file.length();
            if (!file.delete()) {
                LogUtils.i(TAG, "Clear local SharedPrefs file failed!");
                return;
            }
            LogUtils.i(TAG, "Clear local SharedPrefs file succeed, file size =" + length);
        }
    }
}
