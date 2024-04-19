package com.xiaopeng.autoshow.utils;

import android.car.Car;
import android.util.Log;
import com.xiaopeng.autoshow.CarConstant;
/* loaded from: classes.dex */
public class CarVersionUtil {
    public static final boolean IS_DEVELOPING = false;
    private static final String TAG = "VersionUtils";

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static int getCarType() {
        char c;
        String hardwareCarType = getHardwareCarType();
        LogUtils.d("cjf", hardwareCarType);
        switch (hardwareCarType.hashCode()) {
            case 66915:
                if (hardwareCarType.equals(CarConstant.CAR_TYPE_D10)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 66946:
                if (hardwareCarType.equals(CarConstant.CAR_TYPE_D20)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 66947:
                if (hardwareCarType.equals(CarConstant.CAR_TYPE_D21)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 66948:
                if (hardwareCarType.equals(CarConstant.CAR_TYPE_D22)) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 66951:
                if (hardwareCarType.equals(CarConstant.CAR_TYPE_D25)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 67044:
                if (hardwareCarType.equals("D55")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 67915:
                if (hardwareCarType.equals(CarConstant.CAR_TYPE_E28)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 67944:
                if (hardwareCarType.equals(CarConstant.CAR_TYPE_E36)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 67946:
                if (hardwareCarType.equals(CarConstant.CAR_TYPE_E38)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 2078429:
                if (hardwareCarType.equals(CarConstant.CAR_TYPE_D55A)) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 2371978:
                if (hardwareCarType.equals(CarConstant.CAR_TYPE_MOCK)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return 1;
            case 1:
            case 2:
                return 5;
            case 3:
                return 6;
            case 4:
                return 7;
            case 5:
                return 11;
            case 6:
                return 8;
            case 7:
                return CarConstant.CAR_MOCK;
            case '\b':
                return 9;
            case '\t':
            case '\n':
                return 10;
            default:
                return 0;
        }
    }

    private static int judgeD20Type() {
        return getXpCduType().equals(CarConstant.CDU_TYPE_D20P) ? 3 : 2;
    }

    private static int judgeD21Type() {
        if (getXpCduType().equals(CarConstant.CDU_TYPE_D21_A)) {
            return 4;
        }
        return getXpCduType().equals(CarConstant.CDU_TYPE_D21_B) ? 5 : 0;
    }

    private static String getHardwareCarType() {
        String str;
        try {
            str = Car.getHardwareCarType();
        } catch (Exception e) {
            e = e;
            str = "UNKNOWN";
        }
        try {
            Log.d(TAG, "getHardwareCarType, the type is " + str);
        } catch (Exception e2) {
            e = e2;
            Log.e(TAG, "getHardwareCarType is not ready:" + e.getMessage());
            return str;
        }
        return str;
    }

    public static String getXpCduType() {
        String str;
        try {
            str = Car.getXpCduType();
        } catch (Exception e) {
            e = e;
            str = "UNKNOWN";
        }
        try {
            Log.d(TAG, "getXpCduType, the Cdu Type is " + str);
        } catch (Exception e2) {
            e = e2;
            Log.e(TAG, "getXpCduType is not ready:" + e.getMessage());
            return str;
        }
        return str;
    }
}
