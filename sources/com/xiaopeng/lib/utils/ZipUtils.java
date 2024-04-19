package com.xiaopeng.lib.utils;

import android.text.TextUtils;
import android.util.Base64;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.eclipse.paho.client.mqttv3.MqttTopic;
/* loaded from: classes.dex */
public class ZipUtils {
    private static byte[] ZIP_HEADER_1 = {80, TarConstants.LF_GNUTYPE_LONGLINK, 3, 4};
    private static byte[] ZIP_HEADER_2 = {80, TarConstants.LF_GNUTYPE_LONGLINK, 5, 6};
    private static int ZIP_HELADER_LENGTH = 4;

    /* JADX WARN: Code restructure failed: missing block: B:15:0x002d, code lost:
        if (java.util.Arrays.equals(com.xiaopeng.lib.utils.ZipUtils.ZIP_HEADER_2, r4) != false) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x003c, code lost:
        if (r2 == null) goto L24;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean isArchiveFile(java.io.File r4) {
        /*
            r0 = 0
            if (r4 != 0) goto L4
            return r0
        L4:
            boolean r1 = r4.isDirectory()
            if (r1 == 0) goto Lb
            return r0
        Lb:
            r1 = 0
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L37
            r2.<init>(r4)     // Catch: java.lang.Throwable -> L37
            int r4 = com.xiaopeng.lib.utils.ZipUtils.ZIP_HELADER_LENGTH     // Catch: java.lang.Throwable -> L35
            byte[] r4 = new byte[r4]     // Catch: java.lang.Throwable -> L35
            int r1 = com.xiaopeng.lib.utils.ZipUtils.ZIP_HELADER_LENGTH     // Catch: java.lang.Throwable -> L35
            int r1 = r2.read(r4, r0, r1)     // Catch: java.lang.Throwable -> L35
            int r3 = com.xiaopeng.lib.utils.ZipUtils.ZIP_HELADER_LENGTH     // Catch: java.lang.Throwable -> L35
            if (r1 != r3) goto L31
            byte[] r1 = com.xiaopeng.lib.utils.ZipUtils.ZIP_HEADER_1     // Catch: java.lang.Throwable -> L35
            boolean r1 = java.util.Arrays.equals(r1, r4)     // Catch: java.lang.Throwable -> L35
            if (r1 != 0) goto L2f
            byte[] r1 = com.xiaopeng.lib.utils.ZipUtils.ZIP_HEADER_2     // Catch: java.lang.Throwable -> L35
            boolean r4 = java.util.Arrays.equals(r1, r4)     // Catch: java.lang.Throwable -> L35
            if (r4 == 0) goto L31
        L2f:
            r4 = 1
            r0 = r4
        L31:
            r2.close()     // Catch: java.io.IOException -> L3f
            goto L3f
        L35:
            r4 = move-exception
            goto L39
        L37:
            r4 = move-exception
            r2 = r1
        L39:
            r4.printStackTrace()     // Catch: java.lang.Throwable -> L40
            if (r2 == 0) goto L3f
            goto L31
        L3f:
            return r0
        L40:
            r4 = move-exception
            if (r2 == 0) goto L46
            r2.close()     // Catch: java.io.IOException -> L46
        L46:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.lib.utils.ZipUtils.isArchiveFile(java.io.File):boolean");
    }

    public static String compressForGzip(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(str.getBytes());
            gZIPOutputStream.close();
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            return compressForBase64(byteArray);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String decompressForGzip(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        byte[] decompressForBase64 = decompressForBase64(str);
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decompressForBase64);
            GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = gZIPInputStream.read(bArr, 0, bArr.length);
                if (read > 0) {
                    byteArrayOutputStream.write(bArr, 0, read);
                } else {
                    gZIPInputStream.close();
                    byteArrayInputStream.close();
                    byteArrayOutputStream.close();
                    return byteArrayOutputStream.toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String compressForBase64(byte[] bArr) {
        return compressForBase64(bArr, 0);
    }

    public static String compressForBase64(byte[] bArr, int i) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        return Base64.encodeToString(bArr, i);
    }

    public static byte[] decompressForBase64(String str) {
        return decompressForBase64(str, 0);
    }

    public static byte[] decompressForBase64(String str, int i) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return Base64.decode(str, i);
    }

    public static byte[] compressForUpload(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(str.getBytes());
            gZIPOutputStream.close();
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            return byteArray;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String decompressForUpload(byte[] bArr) {
        if (bArr != null && bArr.length != 0) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
                GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
                byte[] bArr2 = new byte[1024];
                while (true) {
                    int read = gZIPInputStream.read(bArr2, 0, bArr2.length);
                    if (read > 0) {
                        byteArrayOutputStream.write(bArr2, 0, read);
                    } else {
                        gZIPInputStream.close();
                        byteArrayInputStream.close();
                        byteArrayOutputStream.close();
                        return byteArrayOutputStream.toString();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean zip(String str, String str2) throws IOException {
        File file;
        ZipOutputStream zipOutputStream;
        ZipOutputStream zipOutputStream2 = null;
        try {
            try {
                File file2 = new File(str2);
                file = new File(str);
                zipOutputStream = new ZipOutputStream(new FileOutputStream(file2));
            } catch (Throwable unused) {
            }
        } catch (Exception e) {
            e = e;
        }
        try {
            if (file.isFile()) {
                zipFileOrDirectory(zipOutputStream, file, "");
            } else {
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    for (File file3 : listFiles) {
                        zipFileOrDirectory(zipOutputStream, file3, "");
                    }
                }
            }
            zipOutputStream.flush();
            FileUtils.closeQuietly(zipOutputStream);
            return true;
        } catch (Exception e2) {
            e = e2;
            zipOutputStream2 = zipOutputStream;
            e.printStackTrace();
            FileUtils.closeQuietly(zipOutputStream2);
            return false;
        } catch (Throwable unused2) {
            zipOutputStream2 = zipOutputStream;
            FileUtils.closeQuietly(zipOutputStream2);
            return false;
        }
    }

    public static void zipFileOrDirectory(ZipOutputStream zipOutputStream, File file, String str) throws IOException {
        zipFileOrDirectory(zipOutputStream, file, str, true);
    }

    public static void zipFileOrDirectory(ZipOutputStream zipOutputStream, File file, String str, boolean z) throws IOException {
        FileInputStream fileInputStream;
        BufferedInputStream bufferedInputStream;
        BufferedInputStream bufferedInputStream2 = null;
        try {
            if (!file.isDirectory()) {
                byte[] bArr = new byte[4096];
                fileInputStream = new FileInputStream(file);
                try {
                    try {
                        bufferedInputStream = new BufferedInputStream(fileInputStream);
                    } catch (Exception e) {
                        e = e;
                    }
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    zipOutputStream.putNextEntry(new ZipEntry(str + file.getName()));
                    while (true) {
                        int read = bufferedInputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        zipOutputStream.write(bArr, 0, read);
                    }
                    zipOutputStream.flush();
                    zipOutputStream.closeEntry();
                    bufferedInputStream2 = bufferedInputStream;
                } catch (Exception e2) {
                    e = e2;
                    bufferedInputStream2 = bufferedInputStream;
                    e.printStackTrace();
                    FileUtils.closeQuietly(bufferedInputStream2);
                    FileUtils.closeQuietly(fileInputStream);
                } catch (Throwable th2) {
                    th = th2;
                    bufferedInputStream2 = bufferedInputStream;
                    FileUtils.closeQuietly(bufferedInputStream2);
                    FileUtils.closeQuietly(fileInputStream);
                    throw th;
                }
            } else {
                File[] listFiles = file.listFiles();
                if (listFiles != null && listFiles.length > 0) {
                    for (File file2 : listFiles) {
                        zipFileOrDirectory(zipOutputStream, file2, str + file.getName() + MqttTopic.TOPIC_LEVEL_SEPARATOR, z);
                    }
                } else if (!z) {
                    zipOutputStream.putNextEntry(new ZipEntry(file.getAbsolutePath() + MqttTopic.TOPIC_LEVEL_SEPARATOR));
                    zipOutputStream.closeEntry();
                }
                fileInputStream = null;
            }
        } catch (Exception e3) {
            e = e3;
            fileInputStream = null;
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
        }
        FileUtils.closeQuietly(bufferedInputStream2);
        FileUtils.closeQuietly(fileInputStream);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v1 */
    /* JADX WARN: Type inference failed for: r11v3 */
    /* JADX WARN: Type inference failed for: r11v4, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r11v6 */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v3, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v8 */
    /* JADX WARN: Type inference failed for: r3v9, types: [java.io.BufferedInputStream, java.io.Closeable] */
    public static File zipFile(String str, String str2) {
        ZipOutputStream zipOutputStream;
        FileInputStream fileInputStream;
        BufferedOutputStream bufferedOutputStream;
        FileInputStream fileInputStream2;
        ?? r3;
        File file = new File(str2);
        BufferedOutputStream bufferedOutputStream2 = 0;
        bufferedOutputStream2 = 0;
        try {
            try {
                zipOutputStream = new ZipOutputStream(new FileOutputStream(file));
            } catch (Throwable th) {
                bufferedOutputStream2 = str;
                th = th;
            }
        } catch (Exception e) {
            e = e;
            bufferedOutputStream = null;
            zipOutputStream = null;
            fileInputStream = null;
        } catch (Throwable th2) {
            th = th2;
            zipOutputStream = null;
            fileInputStream = null;
        }
        try {
            fileInputStream = new FileInputStream(str);
            try {
                zipOutputStream.putNextEntry(new ZipEntry(new File(str).getName()));
                bufferedOutputStream = new BufferedOutputStream(zipOutputStream);
            } catch (Exception e2) {
                e = e2;
                bufferedOutputStream = null;
                r3 = 0;
            } catch (Throwable th3) {
                th = th3;
                fileInputStream2 = null;
            }
        } catch (Exception e3) {
            e = e3;
            bufferedOutputStream = null;
            fileInputStream = null;
            r3 = fileInputStream;
            e.printStackTrace();
            FileUtils.closeQuietly(zipOutputStream);
            FileUtils.closeQuietly(fileInputStream);
            FileUtils.closeQuietly(bufferedOutputStream);
            FileUtils.closeQuietly(r3);
            return null;
        } catch (Throwable th4) {
            th = th4;
            fileInputStream = null;
            fileInputStream2 = fileInputStream;
            FileUtils.closeQuietly(zipOutputStream);
            FileUtils.closeQuietly(fileInputStream);
            FileUtils.closeQuietly(bufferedOutputStream2);
            FileUtils.closeQuietly(fileInputStream2);
            throw th;
        }
        try {
            r3 = new BufferedInputStream(fileInputStream);
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = r3.read(bArr, 0, 1024);
                    if (read != -1) {
                        bufferedOutputStream.write(bArr, 0, read);
                    } else {
                        bufferedOutputStream.flush();
                        zipOutputStream.flush();
                        FileUtils.closeQuietly(zipOutputStream);
                        FileUtils.closeQuietly(fileInputStream);
                        FileUtils.closeQuietly(bufferedOutputStream);
                        FileUtils.closeQuietly(r3);
                        return file;
                    }
                }
            } catch (Exception e4) {
                e = e4;
                e.printStackTrace();
                FileUtils.closeQuietly(zipOutputStream);
                FileUtils.closeQuietly(fileInputStream);
                FileUtils.closeQuietly(bufferedOutputStream);
                FileUtils.closeQuietly(r3);
                return null;
            }
        } catch (Exception e5) {
            e = e5;
            r3 = 0;
        } catch (Throwable th5) {
            fileInputStream2 = null;
            bufferedOutputStream2 = bufferedOutputStream;
            th = th5;
            FileUtils.closeQuietly(zipOutputStream);
            FileUtils.closeQuietly(fileInputStream);
            FileUtils.closeQuietly(bufferedOutputStream2);
            FileUtils.closeQuietly(fileInputStream2);
            throw th;
        }
    }

    public static File zipMultiFiles(String str, List<String> list) throws IOException {
        return zipMultiFiles(str, list, true);
    }

    public static File zipMultiFiles(String str, List<String> list, boolean z) throws IOException {
        try {
            return zipMultiFilesWithThrow(str, list, z);
        } catch (Exception e) {
            e.printStackTrace();
            return new File(str);
        }
    }

    public static File zipMultiFilesWithThrow(String str, List<String> list) throws IOException {
        return zipMultiFilesWithThrow(str, list, true);
    }

    public static File zipMultiFilesWithThrow(String str, List<String> list, boolean z) throws IOException {
        FileOutputStream fileOutputStream;
        ZipOutputStream zipOutputStream;
        File file = new File(str);
        if (file.exists()) {
            file.delete();
        }
        try {
            fileOutputStream = new FileOutputStream(new File(str));
            try {
                zipOutputStream = new ZipOutputStream(fileOutputStream);
                try {
                    for (String str2 : list) {
                        File file2 = new File(str2);
                        String parent = file2.getParent();
                        if (parent == null) {
                            parent = "";
                        }
                        zipFileOrDirectory(zipOutputStream, file2, parent + MqttTopic.TOPIC_LEVEL_SEPARATOR, z);
                    }
                    zipOutputStream.flush();
                    zipOutputStream.closeEntry();
                    FileUtils.closeQuietly(zipOutputStream);
                    FileUtils.closeQuietly(fileOutputStream);
                    return new File(str);
                } catch (Throwable th) {
                    th = th;
                    FileUtils.closeQuietly(zipOutputStream);
                    FileUtils.closeQuietly(fileOutputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                zipOutputStream = null;
            }
        } catch (Throwable th3) {
            th = th3;
            fileOutputStream = null;
            zipOutputStream = null;
        }
    }

    public static String compressForGzipAndBase64NoWrap(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(str.getBytes());
            gZIPOutputStream.close();
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            return compressForBase64(byteArray, 2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String decompressForGzipAndBase64NoWrap(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        byte[] decompressForBase64 = decompressForBase64(str, 2);
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decompressForBase64);
            GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = gZIPInputStream.read(bArr, 0, bArr.length);
                if (read > 0) {
                    byteArrayOutputStream.write(bArr, 0, read);
                } else {
                    gZIPInputStream.close();
                    byteArrayInputStream.close();
                    byteArrayOutputStream.close();
                    return byteArrayOutputStream.toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
