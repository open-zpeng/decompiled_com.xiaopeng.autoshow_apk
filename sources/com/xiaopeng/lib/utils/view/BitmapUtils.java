package com.xiaopeng.lib.utils.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import androidx.core.view.MotionEventCompat;
import com.xiaopeng.autoshow.Config;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
/* loaded from: classes.dex */
public class BitmapUtils {
    public static void saveAsPicture(int i, int i2, byte[] bArr, String str) {
        int[] iArr = new int[i * i2];
        decodeYUV420SP(iArr, bArr, i, i2);
        Bitmap createBitmap = Bitmap.createBitmap(iArr, i, i2, Bitmap.Config.ARGB_8888);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            createBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap byte2Bmp(int i, int i2, byte[] bArr, Bitmap.Config config) {
        int[] iArr = new int[i * i2];
        decodeYUV420SP(iArr, bArr, i, i2);
        return Bitmap.createBitmap(iArr, i, i2, config);
    }

    public static Bitmap byte2Bmp(int i, int i2, byte[] bArr, int i3) {
        YuvImage yuvImage = new YuvImage(bArr, 17, i, i2, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, i, i2), i3, byteArrayOutputStream);
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size());
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return decodeByteArray;
    }

    private static void decodeYUV420SP(int[] iArr, byte[] bArr, int i, int i2) {
        int i3 = i * i2;
        int i4 = 0;
        int i5 = 0;
        while (i4 < i2) {
            int i6 = 0;
            int i7 = 0;
            int i8 = ((i4 >> 1) * i) + i3;
            int i9 = i5;
            int i10 = 0;
            while (i10 < i) {
                int i11 = (bArr[i9] & 255) - 16;
                if (i11 < 0) {
                    i11 = 0;
                }
                if ((i10 & 1) == 0) {
                    int i12 = i8 + 1;
                    int i13 = i12 + 1;
                    i6 = (bArr[i12] & 255) - 128;
                    i7 = (bArr[i8] & 255) - 128;
                    i8 = i13;
                }
                int i14 = i11 * 1192;
                int i15 = (i7 * 1634) + i14;
                int i16 = (i14 - (i7 * 833)) - (i6 * Config.ERROR_CODE);
                int i17 = i14 + (i6 * 2066);
                int i18 = 262143;
                if (i15 < 0) {
                    i15 = 0;
                } else if (i15 > 262143) {
                    i15 = 262143;
                }
                if (i16 < 0) {
                    i16 = 0;
                } else if (i16 > 262143) {
                    i16 = 262143;
                }
                if (i17 < 0) {
                    i18 = 0;
                } else if (i17 <= 262143) {
                    i18 = i17;
                }
                iArr[i9] = (-16777216) | ((i15 << 6) & 16711680) | ((i16 >> 2) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | ((i18 >> 10) & 255);
                i10++;
                i9++;
            }
            i4++;
            i5 = i9;
        }
    }
}
