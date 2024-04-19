package com.xiaopeng.lib.bughunter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.lzy.okgo.OkGo;
import com.xiaopeng.datalog.DataLogModuleEntry;
import com.xiaopeng.lib.bughunter.anr.Caton;
import com.xiaopeng.lib.bughunter.anr.Config;
import com.xiaopeng.lib.bughunter.utils.BugHunterUtils;
import com.xiaopeng.lib.framework.module.Module;
import com.xiaopeng.lib.framework.moduleinterface.datalogmodule.IDataLog;
import com.xiaopeng.lib.utils.info.BuildInfoUtils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class BugHunter {
    private static final String CATON_EVENT = "perf_caton";
    private static final String KEY_ANR_FLAG = "anr";
    private static final String KEY_APP_NAME = "appName";
    private static final String KEY_APP_VER = "appVer";
    private static final String KEY_EVENT = "_event";
    private static final String KEY_EVENT_TIME = "_time";
    private static final String KEY_MCU_VER = "_mcuver";
    private static final String KEY_MEM_INFO = "memInfo";
    private static final String KEY_MODULE = "_module";
    private static final String KEY_MODULE_VER = "_module_version";
    private static final String KEY_NETWORK = "_network";
    private static final String KEY_STACK_INFO = "stackInfo";
    private static final String KEY_STACK_MD5 = "md5";
    private static final String KEY_STUCK_TIME = "elapseTime";
    private static final String KEY_SYSTEM_BOOT_TIME = "_st_time";
    private static final String SEPARATOR = "#";
    private static final String TAG = "libBugHunter";
    private static boolean dumpToSdCardFlag;

    public static void init(Context context) {
        init(context, false, true, false);
    }

    public static void init(Context context, boolean z, boolean z2, boolean z3) {
        try {
            initCaton(context.getApplicationContext(), z, z2, z3);
            Module.register(DataLogModuleEntry.class, new DataLogModuleEntry(context));
            Module.get(DataLogModuleEntry.class).get(IDataLog.class);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private static void initCaton(final Context context, boolean z, boolean z2, boolean z3) {
        long j = 400;
        long j2 = 100;
        if (BuildInfoUtils.isUserVersion()) {
            j = OkGo.DEFAULT_MILLISECONDS;
            j2 = 1000;
        } else if (z || new File("/sdcard/Log/catonflag").exists()) {
            j = 100;
        } else {
            j2 = 400;
        }
        if (z3) {
            dumpToSdCardFlag = true;
        } else if (new File("/sdcard/Log/catondumpflag").exists()) {
            dumpToSdCardFlag = true;
        }
        Caton.initialize(new Caton.Builder(context.getApplicationContext()).monitorMode(Caton.MonitorMode.LOOPER).loggingEnabled(z2).collectInterval(j).thresholdTime(j2).callback(new Caton.Callback() { // from class: com.xiaopeng.lib.bughunter.BugHunter.1
            /* JADX WARN: Removed duplicated region for block: B:22:0x0082  */
            /* JADX WARN: Removed duplicated region for block: B:25:0x00df  */
            /* JADX WARN: Removed duplicated region for block: B:28:0x00ea  */
            /* JADX WARN: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
            @Override // com.xiaopeng.lib.bughunter.anr.Caton.Callback
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onBlockOccurs(java.lang.String[] r16, boolean r17, long... r18) {
                /*
                    Method dump skipped, instructions count: 267
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.lib.bughunter.BugHunter.AnonymousClass1.onBlockOccurs(java.lang.String[], boolean, long[]):void");
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String calcStackTraceMd5(String str) {
        int indexOf = str.indexOf("\n");
        if (indexOf > 0) {
            str = str.substring(indexOf + 1);
        }
        return MD5Utils.getMD5(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getJsonStuckLog(Context context, String str, String str2, long j, String str3, String str4) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt("_event", CATON_EVENT);
            jSONObject.putOpt("_module", "perf");
            jSONObject.putOpt("_mcuver", BugHunterUtils.getMCUVer());
            jSONObject.putOpt("_module_version", str2);
            jSONObject.putOpt("_st_time", Long.valueOf(SystemClock.uptimeMillis() / 1000));
            jSONObject.putOpt("_time", Long.valueOf(System.currentTimeMillis()));
            jSONObject.putOpt("_network", BugHunterUtils.getNetworkType(context));
            jSONObject.putOpt(KEY_APP_NAME, str);
            jSONObject.putOpt(KEY_APP_VER, str2);
            jSONObject.putOpt(KEY_ANR_FLAG, false);
            jSONObject.putOpt(KEY_STUCK_TIME, Long.valueOf(j));
            jSONObject.putOpt(KEY_STACK_MD5, str3);
            jSONObject.putOpt(KEY_STACK_INFO, str4);
            return jSONObject.toString();
        } catch (Throwable th) {
            Log.e(TAG, "error in function getJsonCatonLog, " + th.getMessage());
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String printStackTrace(String str, Context context, String str2, String[] strArr, long j, long j2) {
        long j3;
        long j4;
        JSONObject jSONObject = new JSONObject();
        StringBuffer stringBuffer = new StringBuffer("");
        long j5 = 0;
        if (context != null) {
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
            long j6 = memoryInfo.availMem / 1048576;
            j4 = memoryInfo.totalMem / 1048576;
            j3 = memoryInfo.threshold / 1048576;
            stringBuffer.append("availMem:");
            stringBuffer.append(j6);
            stringBuffer.append("totalMem:");
            stringBuffer.append(j4);
            stringBuffer.append("threshold:");
            stringBuffer.append(j3);
            j5 = j6;
        } else {
            j3 = 0;
            j4 = 0;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n----------------caton log [ ");
        if (!TextUtils.isEmpty(str2)) {
            sb.append(str2);
        }
        sb.append(" ]");
        for (int length = strArr.length - 1; length >= 0; length--) {
            String str3 = strArr[length];
            sb.append("\n");
            sb.append(str3);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
        try {
            jSONObject.put(KEY_STACK_MD5, str);
            jSONObject.put("pkgName", str2);
            jSONObject.put("time", simpleDateFormat.format(Calendar.getInstance().getTime()));
            jSONObject.put("ElapseTime", j);
            jSONObject.put("threadElapseTime", j2);
            jSONObject.put("availMem", j5);
            jSONObject.put("totalMem", j4);
            jSONObject.put("threshold", j3);
            jSONObject.put("catonLog", sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Config.log(TAG, jSONObject.toString());
        return stringBuffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00a0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void dumpCatonInfo(java.lang.String r7, java.lang.String r8, byte[] r9) {
        /*
            java.io.File r0 = new java.io.File
            r0.<init>(r7)
            boolean r1 = r0.exists()
            if (r1 != 0) goto L4b
            boolean r1 = r0.mkdirs()
            java.lang.String r2 = "libBugHunter"
            if (r1 == 0) goto L46
            r1 = 0
            r3 = 1
            boolean r4 = r0.setReadable(r3, r1)
            boolean r5 = r0.setWritable(r3, r1)
            boolean r0 = r0.setExecutable(r3, r1)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "caton LogDir setReadable: "
            r1.append(r3)
            r1.append(r4)
            java.lang.String r3 = "; setWritable: "
            r1.append(r3)
            r1.append(r5)
            java.lang.String r3 = "; setExecutable: "
            r1.append(r3)
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            com.xiaopeng.lib.utils.LogUtils.d(r2, r0)
            goto L4b
        L46:
            java.lang.String r0 = "make caton LogDir failed"
            com.xiaopeng.lib.utils.LogUtils.w(r2, r0)
        L4b:
            java.io.File r0 = new java.io.File
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r7)
            java.lang.String r7 = "/"
            r1.append(r7)
            r1.append(r8)
            java.lang.String r7 = ".log"
            r1.append(r7)
            java.lang.String r7 = r1.toString()
            r0.<init>(r7)
            r7 = 0
            java.io.RandomAccessFile r8 = new java.io.RandomAccessFile     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L92
            java.lang.String r1 = "rw"
            r8.<init>(r0, r1)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L92
            long r0 = r8.length()     // Catch: java.lang.Exception -> L8b java.lang.Throwable -> L9d
            r8.seek(r0)     // Catch: java.lang.Exception -> L8b java.lang.Throwable -> L9d
            r8.write(r9)     // Catch: java.lang.Exception -> L8b java.lang.Throwable -> L9d
            java.lang.String r7 = "\n\n"
            r8.writeBytes(r7)     // Catch: java.lang.Exception -> L8b java.lang.Throwable -> L9d
            java.io.FileDescriptor r7 = r8.getFD()     // Catch: java.lang.Exception -> L8b java.lang.Throwable -> L9d
            r7.sync()     // Catch: java.lang.Exception -> L8b java.lang.Throwable -> L9d
        L87:
            r8.close()     // Catch: java.lang.Exception -> L9c
            goto L9c
        L8b:
            r7 = move-exception
            goto L96
        L8d:
            r8 = move-exception
            r6 = r8
            r8 = r7
            r7 = r6
            goto L9e
        L92:
            r8 = move-exception
            r6 = r8
            r8 = r7
            r7 = r6
        L96:
            r7.printStackTrace()     // Catch: java.lang.Throwable -> L9d
            if (r8 == 0) goto L9c
            goto L87
        L9c:
            return
        L9d:
            r7 = move-exception
        L9e:
            if (r8 == 0) goto La3
            r8.close()     // Catch: java.lang.Exception -> La3
        La3:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.lib.bughunter.BugHunter.dumpCatonInfo(java.lang.String, java.lang.String, byte[]):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getVersionName(Context context, String str) {
        PackageInfo packageInfo = getPackageInfo(context, str);
        return packageInfo == null ? "" : packageInfo.versionName;
    }

    private static PackageInfo getPackageInfo(Context context, String str) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (str == null) {
                str = context.getPackageName();
            }
            return packageManager.getPackageInfo(str, 16384);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
