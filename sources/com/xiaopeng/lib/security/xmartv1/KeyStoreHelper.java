package com.xiaopeng.lib.security.xmartv1;

import android.os.Build;
import android.security.keystore.KeyProtection;
import android.text.TextUtils;
import android.util.Log;
import com.xiaopeng.lib.HttpInitEventListener;
import com.xiaopeng.lib.InitEventHolder;
import com.xiaopeng.lib.utils.FileUtils;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.lib.utils.MD5Utils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/* loaded from: classes.dex */
public final class KeyStoreHelper {
    private static String TAG = "KeyStoreHelper";
    private static KeyStore sKeyStore;
    private static boolean sUsedDefaultKeyStore;
    private static String PASSWORD = MD5Utils.getMD5(XmartV1Constants.KEY_MANAGER_PASSWORD);
    private static String KEY_STORE_PATH = "/private/sec/XpengAndroidKeyStore";
    private static HashMap<String, SecretKey> sSecretKeyMap = new HashMap<>();

    public static boolean init() {
        if (sKeyStore != null) {
            return true;
        }
        boolean loadWithXmartKeyStore = loadWithXmartKeyStore();
        if (!loadWithXmartKeyStore) {
            InitEventHolder.get().onInitException(6001, "failed to init xmart keystore");
            loadWithXmartKeyStore = loadWithDefaultKeyStore();
            if (!loadWithXmartKeyStore) {
                InitEventHolder.get().onInitException(6002, "failed to init default keystore");
            }
        }
        return loadWithXmartKeyStore;
    }

    public static boolean hasValidIndividualKeys() {
        boolean z = false;
        if (sKeyStore == null) {
            LogUtils.d(TAG, "KeyStore was not initialized!");
            return false;
        }
        int i = 0;
        while (true) {
            try {
                if (i >= XmartV1Constants.LOCAL_KEYS_NUM) {
                    z = true;
                    break;
                }
                if (!sKeyStore.containsAlias(XmartV1Constants.LOCAL_KEYS_PREFIX + i)) {
                    break;
                }
                i++;
            } catch (Exception e) {
                LogUtils.d(TAG, e);
            }
        }
        if (!z) {
            InitEventHolder.get().onInitException(HttpInitEventListener.CODE_NOT_INDIV, "no valid individual keys");
        }
        return z;
    }

    public static boolean cleanIndividualKeys() throws KeyStoreException {
        if (sKeyStore == null) {
            LogUtils.d(TAG, "KeyStore was not initialized!");
            return false;
        }
        for (int i = 0; i < XmartV1Constants.LOCAL_KEYS_NUM; i++) {
            try {
                KeyStore keyStore = sKeyStore;
                if (keyStore.containsAlias(XmartV1Constants.LOCAL_KEYS_PREFIX + i)) {
                    KeyStore keyStore2 = sKeyStore;
                    keyStore2.deleteEntry(XmartV1Constants.LOCAL_KEYS_PREFIX + i);
                }
            } catch (Exception e) {
                LogUtils.d(TAG, e);
                throw e;
            }
        }
        return true;
    }

    public static void writeSecretData(Map<String, byte[]> map) throws Exception {
        if (sKeyStore == null) {
            init();
        }
        if (map.size() != XmartV1Constants.LOCAL_KEYS_NUM) {
            throw new RuntimeException("Illegal security keys!!!!");
        }
        KeyProtection build = Build.VERSION.SDK_INT >= 23 ? new KeyProtection.Builder(3).setBlockModes("GCM").setEncryptionPaddings("NoPadding").setRandomizedEncryptionRequired(false).build() : null;
        for (String str : map.keySet()) {
            sKeyStore.setEntry(str, new KeyStore.SecretKeyEntry(new SecretKeySpec(map.get(str), "AES")), build);
        }
    }

    public static byte[] decryptString(byte[] bArr, String str, byte[] bArr2) {
        if (bArr == null || bArr.length == 0 || TextUtils.isEmpty(str)) {
            return null;
        }
        return decryptWithStream(new ByteArrayInputStream(bArr), str, bArr2);
    }

    public static byte[] decryptStringWithKey(byte[] bArr, String str, byte[] bArr2) {
        if (bArr == null || bArr.length == 0 || bArr2 == null || bArr2.length == 0) {
            return null;
        }
        return decryptWithStreamAndKey(new ByteArrayInputStream(bArr), new SecretKeySpec(str.getBytes(), "AES"), bArr2);
    }

    public static byte[] decryptWithByteBuffer(ByteBuffer byteBuffer, String str, byte[] bArr) {
        if (byteBuffer == null || TextUtils.isEmpty(str)) {
            return null;
        }
        return decryptWithStream(new ByteBufferInputStream(byteBuffer), str, bArr);
    }

    private static byte[] decryptWithStream(InputStream inputStream, String str, byte[] bArr) {
        if (inputStream == null || TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            SecretKey secretKey = getSecretKey(str);
            if (secretKey == null) {
                return null;
            }
            return decryptWithStreamAndKey(inputStream, secretKey, bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] decryptGeneral(byte[] bArr, String str) {
        try {
            SecretKey secretKey = getSecretKey(str);
            if (secretKey == null) {
                return null;
            }
            return decryptInternal(bArr, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] decryptInternal(byte[] bArr, SecretKey secretKey) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(XmartV1Constants.AES_GCM_NO_PADDING);
            cipher.init(2, secretKey, getParamsSpec(XmartV1Constants.FIXED_IV));
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            LogUtils.d(TAG, e);
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] decryptWithStreamAndKey(InputStream inputStream, SecretKey secretKey, byte[] bArr) {
        byte[] bArr2 = null;
        if (inputStream != null && bArr != null && bArr.length != 0) {
            try {
                Cipher cipher = Cipher.getInstance(XmartV1Constants.AES_GCM_NO_PADDING);
                cipher.init(2, secretKey, getParamsSpec(bArr));
                CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
                ArrayList arrayList = new ArrayList();
                while (true) {
                    int read = cipherInputStream.read();
                    if (read == -1) {
                        break;
                    }
                    arrayList.add(Byte.valueOf((byte) read));
                }
                bArr2 = new byte[arrayList.size()];
                for (int i = 0; i < bArr2.length; i++) {
                    bArr2[i] = ((Byte) arrayList.get(i)).byteValue();
                }
            } catch (Exception e) {
                String str = TAG;
                LogUtils.d(str, "Exception:" + e);
            }
        }
        return bArr2;
    }

    public static byte[] encryptString(byte[] bArr, String str, byte[] bArr2) {
        if (bArr == null || bArr.length == 0 || TextUtils.isEmpty(str) || bArr2 == null || bArr2.length == 0) {
            return null;
        }
        try {
            SecretKey secretKey = getSecretKey(str);
            if (secretKey == null) {
                return null;
            }
            return encryptInternal(bArr, secretKey, bArr2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] encryptWithKey(byte[] bArr, String str, byte[] bArr2) {
        return encryptInternal(bArr, new SecretKeySpec(str.getBytes(), "AES"), bArr2);
    }

    public static byte[] encryptGeneral(byte[] bArr, String str) {
        try {
            SecretKey secretKey = getSecretKey(str);
            if (secretKey == null) {
                return null;
            }
            return encryptInternal(bArr, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void store() {
        if (sUsedDefaultKeyStore) {
            saveKeyStoreToFile();
        }
    }

    private static byte[] encryptInternal(byte[] bArr, SecretKey secretKey, byte[] bArr2) {
        byte[] bArr3 = null;
        if (bArr == null || bArr.length == 0 || bArr2 == null || bArr2.length == 0) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(XmartV1Constants.AES_GCM_NO_PADDING);
            cipher.init(1, secretKey, getParamsSpec(bArr2));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(byteArrayOutputStream, cipher);
            cipherOutputStream.write(bArr);
            cipherOutputStream.close();
            bArr3 = byteArrayOutputStream.toByteArray();
            FileUtils.closeQuietly(byteArrayOutputStream);
            return bArr3;
        } catch (Exception e) {
            LogUtils.d(TAG, e);
            e.printStackTrace();
            return bArr3;
        }
    }

    private static byte[] encryptInternal(byte[] bArr, SecretKey secretKey) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(XmartV1Constants.AES_GCM_NO_PADDING);
            cipher.init(1, secretKey, getParamsSpec(XmartV1Constants.FIXED_IV));
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            LogUtils.d(TAG, e);
            e.printStackTrace();
            return null;
        }
    }

    private static SecretKey getSecretKey(String str) throws Exception {
        if (!sKeyStore.containsAlias(str)) {
            String str2 = TAG;
            LogUtils.d(str2, "Not found the key " + str);
            return null;
        }
        long currentTimeMillis = System.currentTimeMillis();
        SecretKey secretKey = sSecretKeyMap.get(str);
        if (secretKey == null) {
            secretKey = (SecretKey) sKeyStore.getKey(str, null);
            sSecretKeyMap.put(str, secretKey);
        }
        if (secretKey == null) {
            String str3 = TAG;
            LogUtils.d(str3, "Failed to load secret key:" + str);
            return null;
        }
        String str4 = TAG;
        Log.i(str4, "cost time:\t" + (System.currentTimeMillis() - currentTimeMillis));
        return secretKey;
    }

    /* loaded from: classes.dex */
    private static final class ByteBufferInputStream extends InputStream {
        ByteBuffer buf;

        public ByteBufferInputStream(ByteBuffer byteBuffer) {
            this.buf = byteBuffer;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (this.buf.hasRemaining()) {
                return this.buf.get() & 255;
            }
            return -1;
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i, int i2) throws IOException {
            if (this.buf.hasRemaining()) {
                int min = Math.min(i2, this.buf.remaining());
                this.buf.get(bArr, i, min);
                return min;
            }
            return -1;
        }
    }

    private static boolean loadWithXmartKeyStore() {
        boolean z = false;
        try {
            sKeyStore = KeyStore.getInstance(XmartV1Constants.KEY_STORE_TYPE);
            sKeyStore.load(null);
        } catch (Exception e) {
            e = e;
        }
        try {
            sUsedDefaultKeyStore = false;
            return true;
        } catch (Exception e2) {
            e = e2;
            z = true;
            LogUtils.d(TAG, "Failed to load key store, reason:" + e);
            return z;
        }
    }

    private static boolean loadWithDefaultKeyStore() {
        FileInputStream fileInputStream = null;
        boolean z = false;
        try {
            try {
                sKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                if (new File(KEY_STORE_PATH).exists()) {
                    FileInputStream fileInputStream2 = new FileInputStream(KEY_STORE_PATH);
                    try {
                        sKeyStore.load(fileInputStream2, PASSWORD.toCharArray());
                        fileInputStream = fileInputStream2;
                    } catch (Exception e) {
                        e = e;
                        fileInputStream = fileInputStream2;
                        LogUtils.d(TAG, "Failed to load default key store, reason:" + e);
                        FileUtils.closeQuietly(fileInputStream);
                        return z;
                    } catch (Throwable th) {
                        th = th;
                        fileInputStream = fileInputStream2;
                        FileUtils.closeQuietly(fileInputStream);
                        throw th;
                    }
                } else {
                    sKeyStore.load(null);
                }
                try {
                    sUsedDefaultKeyStore = true;
                    FileUtils.closeQuietly(fileInputStream);
                    return true;
                } catch (Exception e2) {
                    z = true;
                    e = e2;
                    LogUtils.d(TAG, "Failed to load default key store, reason:" + e);
                    FileUtils.closeQuietly(fileInputStream);
                    return z;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e3) {
            e = e3;
        }
    }

    private static boolean saveKeyStoreToFile() {
        FileOutputStream fileOutputStream;
        Throwable th;
        try {
            fileOutputStream = new FileOutputStream(KEY_STORE_PATH);
            try {
                sKeyStore.store(fileOutputStream, PASSWORD.toCharArray());
                FileUtils.closeQuietly(fileOutputStream);
                return true;
            } catch (Exception unused) {
                FileUtils.closeQuietly(fileOutputStream);
                return false;
            } catch (Throwable th2) {
                th = th2;
                FileUtils.closeQuietly(fileOutputStream);
                throw th;
            }
        } catch (Exception unused2) {
            fileOutputStream = null;
        } catch (Throwable th3) {
            fileOutputStream = null;
            th = th3;
        }
    }

    private static AlgorithmParameterSpec getParamsSpec(byte[] bArr) {
        if (Build.VERSION.SDK_INT < 21) {
            return new IvParameterSpec(bArr, 0, bArr.length);
        }
        return new GCMParameterSpec(128, bArr);
    }
}
