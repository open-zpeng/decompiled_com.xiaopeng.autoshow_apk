package com.ut.mini.core.sign;

import com.alibaba.mtl.log.d.i;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
/* loaded from: classes.dex */
public class UTSecuritySDKRequestAuthentication implements IUTRequestAuthentication {
    private String ad;
    private String g;
    private Object b = null;
    private Object c = null;
    private Class a = null;

    /* renamed from: a  reason: collision with other field name */
    private Field f204a = null;

    /* renamed from: b  reason: collision with other field name */
    private Field f206b = null;

    /* renamed from: c  reason: collision with other field name */
    private Field f207c = null;

    /* renamed from: a  reason: collision with other field name */
    private Method f205a = null;
    private int z = 1;
    private boolean F = false;

    @Override // com.ut.mini.core.sign.IUTRequestAuthentication
    public String getAppkey() {
        return this.g;
    }

    public UTSecuritySDKRequestAuthentication(String str, String str2) {
        this.g = null;
        this.g = str;
        this.ad = str2;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0050 A[Catch: all -> 0x00d3, TRY_ENTER, TRY_LEAVE, TryCatch #1 {, blocks: (B:3:0x0001, B:39:0x00dd, B:15:0x0045, B:17:0x0050, B:23:0x008c, B:35:0x00b7, B:28:0x00a3, B:21:0x0080), top: B:45:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized void D() {
        /*
            Method dump skipped, instructions count: 228
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.core.sign.UTSecuritySDKRequestAuthentication.D():void");
    }

    @Override // com.ut.mini.core.sign.IUTRequestAuthentication
    public String getSign(String str) {
        Class cls;
        if (!this.F) {
            D();
        }
        if (this.g == null) {
            i.a("UTSecuritySDKRequestAuthentication:getSign", "There is no appkey,please check it!");
            return null;
        } else if (str == null) {
            return null;
        } else {
            if (this.b != null && (cls = this.a) != null && this.f204a != null && this.f206b != null && this.f207c != null && this.f205a != null && this.c != null) {
                try {
                    Object newInstance = cls.newInstance();
                    this.f204a.set(newInstance, this.g);
                    ((Map) this.f206b.get(newInstance)).put("INPUT", str);
                    this.f207c.set(newInstance, Integer.valueOf(this.z));
                    return (String) this.f205a.invoke(this.c, newInstance, this.ad);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                } catch (InstantiationException e3) {
                    e3.printStackTrace();
                } catch (InvocationTargetException e4) {
                    e4.printStackTrace();
                }
            }
            return null;
        }
    }

    public String getAuthCode() {
        return this.ad;
    }
}
