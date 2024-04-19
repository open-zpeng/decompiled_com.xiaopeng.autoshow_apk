package com.xiaopeng.autoshow.utils;

import com.xiaopeng.autoshow.AutoShowApp;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public class FileUtil {
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.io.InputStream] */
    public static String loadFromAssets(String str) {
        Throwable th;
        InputStream inputStream;
        ?? r0 = 0;
        try {
        } catch (Throwable th2) {
            r0 = str;
            th = th2;
        }
        try {
            try {
                inputStream = AutoShowApp.getContext().getAssets().open(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e2) {
            e = e2;
            inputStream = null;
        } catch (Throwable th3) {
            th = th3;
            if (r0 != 0) {
                try {
                    r0.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            throw th;
        }
        if (inputStream == null) {
            if (inputStream != null) {
                inputStream.close();
            }
            return null;
        }
        try {
            String inputStream2String = inputStream2String(inputStream);
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            return inputStream2String;
        } catch (IOException e5) {
            e = e5;
            e.printStackTrace();
            if (inputStream != null) {
                inputStream.close();
            }
            return null;
        }
    }

    private static String inputStream2String(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (inputStream == null) {
            return sb.toString();
        }
        byte[] bArr = new byte[4096];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                sb.append(new String(bArr, 0, read));
            } else {
                return sb.toString();
            }
        }
    }
}
