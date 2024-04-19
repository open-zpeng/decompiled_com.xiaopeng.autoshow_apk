package com.xiaopeng.lib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;
/* loaded from: classes.dex */
public class NetUtils {
    private static final String GOBINET_PROPERTY_KEY = "persist.sys.ril.gobinet";
    private static final String GOBINET_STATE_FILE = "/sys/class/net/xpusb0/operstate";
    private static final String GOBINET_VALUE_ON = "on";
    public static final int NETWORK_TYPE_2G = 1;
    public static final int NETWORK_TYPE_3G = 2;
    public static final int NETWORK_TYPE_4G = 3;
    public static final int NETWORK_TYPE_NONE = 0;
    public static final int NETWORK_TYPE_WIFI = 10;
    public static final String TRAFFIC_STATUS_CHAGNE_ACTION = "com.xiaopeng.action.TRAFFIC_STATUS_CHANGE";
    public static final int TRAFFIC_STATUS_TYPE_APN_ERROR = 2;
    public static final int TRAFFIC_STATUS_TYPE_AVAILABLE = 3;
    public static final String TRAFFIC_STATUS_TYPE_KEY = "persist.sys.xp.4g.st";
    public static final int TRAFFIC_STATUS_TYPE_RUNOUT = 1;

    public static boolean checkNetState(Context context) {
        NetworkInfo[] allNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || (allNetworkInfo = connectivityManager.getAllNetworkInfo()) == null) {
            return false;
        }
        for (NetworkInfo networkInfo : allNetworkInfo) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWifiEnabled(Context context) {
        NetworkInfo networkInfo;
        if (context == null) {
            throw new NullPointerException("Global context is null");
        }
        if (((WifiManager) context.getSystemService("wifi")).getWifiState() != 3 || (networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1)) == null) {
            return false;
        }
        return networkInfo.isConnected();
    }

    public static boolean isMobileNetwork(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 0;
    }

    public static int getNetworkType(Context context) {
        NetworkInfo activeNetworkInfo;
        if (context != null && (activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo()) != null) {
            if (activeNetworkInfo.getType() == 0) {
                switch (activeNetworkInfo.getSubtype()) {
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                        return 1;
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 14:
                    case 15:
                        return 2;
                    case 13:
                        return 3;
                }
            } else if (activeNetworkInfo.getType() == 1) {
                return 10;
            }
        }
        return 0;
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        return connectivityManager != null && (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
    }

    public static String getMacAddress(Context context) {
        WifiInfo connectionInfo = ((WifiManager) context.getApplicationContext().getSystemService("wifi")).getConnectionInfo();
        return connectionInfo == null ? "" : connectionInfo.getMacAddress();
    }

    public static String getIMEI(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
    }

    public static String getNetworkOperatorName(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getNetworkOperatorName();
    }

    public static int getSimState(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getSimState();
    }

    public static boolean isTrafficRunOut() {
        return getTrafficStatus() == 1;
    }

    public static int getTrafficStatus() {
        return SystemProperties.getInt(TRAFFIC_STATUS_TYPE_KEY, 3);
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x003c, code lost:
        if ("unknown".equals(r0) != false) goto L21;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v11, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v8, types: [boolean] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean isSystemApnReady() {
        /*
            java.lang.String r0 = "persist.sys.ril.gobinet"
            java.lang.String r0 = android.os.SystemProperties.get(r0)
            java.lang.String r1 = "on"
            boolean r0 = r1.equals(r0)
            r1 = 1
            if (r0 != 0) goto L10
            return r1
        L10:
            java.io.File r0 = new java.io.File
            java.lang.String r2 = "/sys/class/net/xpusb0/operstate"
            r0.<init>(r2)
            boolean r2 = r0.exists()
            if (r2 != 0) goto L1e
            return r1
        L1e:
            r2 = 0
            r3 = 0
            java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L49 java.io.IOException -> L4b
            java.io.FileReader r5 = new java.io.FileReader     // Catch: java.lang.Throwable -> L49 java.io.IOException -> L4b
            r5.<init>(r0)     // Catch: java.lang.Throwable -> L49 java.io.IOException -> L4b
            r4.<init>(r5)     // Catch: java.lang.Throwable -> L49 java.io.IOException -> L4b
            java.lang.String r0 = r4.readLine()     // Catch: java.lang.Throwable -> L43 java.io.IOException -> L46
            java.lang.String r3 = "up"
            boolean r3 = r3.equals(r0)     // Catch: java.lang.Throwable -> L43 java.io.IOException -> L46
            if (r3 != 0) goto L3e
            java.lang.String r3 = "unknown"
            boolean r0 = r3.equals(r0)     // Catch: java.lang.Throwable -> L43 java.io.IOException -> L46
            if (r0 == 0) goto L3f
        L3e:
            r2 = r1
        L3f:
            com.xiaopeng.lib.utils.FileUtils.closeQuietly(r4)
            goto L52
        L43:
            r0 = move-exception
            r3 = r4
            goto L53
        L46:
            r0 = move-exception
            r3 = r4
            goto L4c
        L49:
            r0 = move-exception
            goto L53
        L4b:
            r0 = move-exception
        L4c:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L49
            com.xiaopeng.lib.utils.FileUtils.closeQuietly(r3)
        L52:
            return r2
        L53:
            com.xiaopeng.lib.utils.FileUtils.closeQuietly(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.lib.utils.NetUtils.isSystemApnReady():boolean");
    }
}
