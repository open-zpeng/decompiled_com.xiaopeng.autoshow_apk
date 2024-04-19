package com.xiaopeng.lib.framework.netchannelmodule.common.util;
/* loaded from: classes.dex */
public class EncryptionUtil {
    private static final String ALGORITHM = "EncryptionUtil-XOR";

    public static byte[] encrypt(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr3[i] = (byte) (bArr[i] ^ bArr2[i % bArr2.length]);
        }
        return bArr3;
    }

    public static byte[] decrypt(byte[] bArr, byte[] bArr2) {
        return encrypt(bArr, bArr2);
    }
}
