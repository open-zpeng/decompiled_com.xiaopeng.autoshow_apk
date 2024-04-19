package com.xiaopeng.lib.bughunter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/* loaded from: classes.dex */
class MD5Utils {
    MD5Utils() {
    }

    public static String getMD5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] digest = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                if (Integer.toHexString(digest[i] & 255).length() == 1) {
                    stringBuffer.append("0");
                    stringBuffer.append(Integer.toHexString(digest[i] & 255));
                } else {
                    stringBuffer.append(Integer.toHexString(digest[i] & 255));
                }
            }
            return stringBuffer.toString();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException unused) {
            return "";
        }
    }

    public static String getFileMd5(File file) throws FileNotFoundException {
        RandomAccessFile randomAccessFile;
        RandomAccessFile randomAccessFile2 = null;
        try {
            try {
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                    if (file != null && file.exists()) {
                        randomAccessFile = new RandomAccessFile(file, "r");
                        try {
                            byte[] bArr = new byte[10485760];
                            while (true) {
                                int read = randomAccessFile.read(bArr);
                                if (read == -1) {
                                    break;
                                }
                                messageDigest.update(bArr, 0, read);
                            }
                            String bigInteger = new BigInteger(1, messageDigest.digest()).toString(16);
                            while (bigInteger.length() < 32) {
                                bigInteger = "0" + bigInteger;
                            }
                            try {
                                randomAccessFile.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return bigInteger;
                        } catch (FileNotFoundException e2) {
                            e = e2;
                            randomAccessFile2 = randomAccessFile;
                            e.printStackTrace();
                            if (randomAccessFile2 != null) {
                                randomAccessFile2.close();
                            }
                            return "";
                        } catch (IOException e3) {
                            e = e3;
                            randomAccessFile2 = randomAccessFile;
                            e.printStackTrace();
                            if (randomAccessFile2 != null) {
                                randomAccessFile2.close();
                            }
                            return "";
                        } catch (NoSuchAlgorithmException e4) {
                            e = e4;
                            randomAccessFile2 = randomAccessFile;
                            e.printStackTrace();
                            if (randomAccessFile2 != null) {
                                randomAccessFile2.close();
                            }
                            return "";
                        } catch (Throwable th) {
                            th = th;
                            if (randomAccessFile != null) {
                                try {
                                    randomAccessFile.close();
                                } catch (IOException e5) {
                                    e5.printStackTrace();
                                }
                            }
                            throw th;
                        }
                    }
                    return "";
                } catch (IOException e6) {
                    e6.printStackTrace();
                    return "";
                }
            } catch (FileNotFoundException e7) {
                e = e7;
            } catch (IOException e8) {
                e = e8;
            } catch (NoSuchAlgorithmException e9) {
                e = e9;
            }
        } catch (Throwable th2) {
            th = th2;
            randomAccessFile = null;
        }
    }
}
