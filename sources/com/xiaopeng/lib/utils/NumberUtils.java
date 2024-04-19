package com.xiaopeng.lib.utils;

import android.util.Log;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
/* loaded from: classes.dex */
public class NumberUtils {
    private static final String TAG = "com.xiaopeng.lib.utils.NumberUtils";
    private static DecimalFormatSymbols sSymbols = new DecimalFormatSymbols();
    private static String sPattern = "#,###.00";
    private static DecimalFormat sDecimalFormat = new DecimalFormat(sPattern, sSymbols);

    public static String getCountryNumberFormat(Locale locale, double d) {
        char c;
        String country = locale.getCountry();
        int hashCode = country.hashCode();
        if (hashCode == 2099) {
            if (country.equals("AT")) {
                c = 6;
            }
            c = 65535;
        } else if (hashCode == 2115) {
            if (country.equals("BE")) {
                c = 5;
            }
            c = 65535;
        } else if (hashCode == 2149) {
            if (country.equals("CH")) {
                c = '\b';
            }
            c = 65535;
        } else if (hashCode == 2177) {
            if (country.equals("DE")) {
                c = 7;
            }
            c = 65535;
        } else if (hashCode == 2183) {
            if (country.equals("DK")) {
                c = 4;
            }
            c = 65535;
        } else if (hashCode == 2252) {
            if (country.equals("FR")) {
                c = 2;
            }
            c = 65535;
        } else if (hashCode == 2494) {
            if (country.equals("NL")) {
                c = 3;
            }
            c = 65535;
        } else if (hashCode != 2497) {
            if (hashCode == 2642 && country.equals("SE")) {
                c = 1;
            }
            c = 65535;
        } else {
            if (country.equals("NO")) {
                c = 0;
            }
            c = 65535;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
                sSymbols.setGroupingSeparator(' ');
                sSymbols.setDecimalSeparator(',');
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                sSymbols.setGroupingSeparator('.');
                sSymbols.setDecimalSeparator(',');
                break;
            case '\b':
                sSymbols.setGroupingSeparator('\'');
                sSymbols.setDecimalSeparator('.');
                break;
            default:
                Log.d(TAG, "The country is not considered");
                sSymbols.setGroupingSeparator(' ');
                sSymbols.setDecimalSeparator(',');
                break;
        }
        sDecimalFormat.setDecimalFormatSymbols(sSymbols);
        return sDecimalFormat.format(d);
    }
}
