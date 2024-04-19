package com.xiaopeng.lib.utils.info;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Process;
import com.xiaopeng.lib.utils.FileUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
/* loaded from: classes.dex */
public class AppInfoUtils {
    private static String sProcessName;

    public static PackageInfo getPackageInfo(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 128);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ApplicationInfo getApplicationInfo(Context context, String str) {
        PackageManager packageManager = context.getPackageManager();
        try {
            if (context.getPackageName().equals(str)) {
                return context.getApplicationInfo();
            }
            return packageManager.getApplicationInfo(str, 128);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getVersionName(Context context, String str) {
        PackageInfo packageInfo = getPackageInfo(context, str);
        return packageInfo == null ? "" : packageInfo.versionName;
    }

    public static int getVersionCode(Context context, String str) {
        PackageInfo packageInfo = getPackageInfo(context, str);
        if (packageInfo == null) {
            return -1;
        }
        return packageInfo.versionCode;
    }

    public static Drawable getApplicationIcon(Context context, String str) {
        try {
            return context.getPackageManager().getApplicationIcon(str);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static CharSequence getApplicationLabel(Context context, String str) {
        ApplicationInfo applicationInfo = getApplicationInfo(context, str);
        return applicationInfo == null ? "" : applicationInfo.loadLabel(context.getPackageManager());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.String] */
    public static String getProcessName() {
        BufferedReader bufferedReader;
        String str = sProcessName;
        if (str != null) {
            return str;
        }
        BufferedReader bufferedReader2 = 0;
        BufferedReader bufferedReader3 = null;
        try {
            try {
                bufferedReader = new BufferedReader(new FileReader(new File("/proc/" + Process.myPid() + "/cmdline")));
            } catch (Throwable th) {
                th = th;
            }
        } catch (Exception e) {
            e = e;
        }
        try {
            sProcessName = bufferedReader.readLine().trim();
            FileUtils.closeQuietly(bufferedReader);
        } catch (Exception e2) {
            e = e2;
            bufferedReader3 = bufferedReader;
            e.printStackTrace();
            sProcessName = "";
            FileUtils.closeQuietly(bufferedReader3);
            bufferedReader2 = sProcessName;
            return bufferedReader2;
        } catch (Throwable th2) {
            th = th2;
            bufferedReader2 = bufferedReader;
            FileUtils.closeQuietly(bufferedReader2);
            throw th;
        }
        bufferedReader2 = sProcessName;
        return bufferedReader2;
    }
}
