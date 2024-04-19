package com.ta.utdid2.a;

import android.content.Context;
import android.util.Log;
import com.ta.utdid2.b.a.f;
import com.ta.utdid2.b.a.i;
import com.ta.utdid2.b.a.j;
/* compiled from: AidManager.java */
/* loaded from: classes.dex */
public class a {
    private static final String TAG = a.class.getName();
    private static a a;
    private Context mContext;

    public static synchronized a a(Context context) {
        a aVar;
        synchronized (a.class) {
            if (a == null) {
                a = new a(context);
            }
            aVar = a;
        }
        return aVar;
    }

    private a(Context context) {
        this.mContext = context;
    }

    public void a(String str, String str2, String str3, com.ut.device.a aVar) {
        if (aVar == null) {
            Log.e(TAG, "callback is null!");
        } else if (this.mContext == null || i.m88a(str) || i.m88a(str2)) {
            String str4 = TAG;
            StringBuilder sb = new StringBuilder("mContext:");
            sb.append(this.mContext);
            sb.append("; callback:");
            sb.append(aVar);
            sb.append("; has appName:");
            sb.append(!i.m88a(str));
            sb.append("; has token:");
            sb.append(!i.m88a(str2));
            Log.e(str4, sb.toString());
            aVar.a(1002, "");
        } else {
            String m85a = c.m85a(this.mContext, str, str2);
            if (!i.m88a(m85a) && j.a(c.a(this.mContext, str, str2), 1)) {
                aVar.a(1001, m85a);
            } else if (f.m87a(this.mContext)) {
                b.a(this.mContext).a(str, str2, str3, m85a, aVar);
            } else {
                aVar.a(1003, m85a);
            }
        }
    }

    public String a(String str, String str2, String str3) {
        if (this.mContext == null || i.m88a(str) || i.m88a(str2)) {
            String str4 = TAG;
            StringBuilder sb = new StringBuilder("mContext:");
            sb.append(this.mContext);
            sb.append("; has appName:");
            sb.append(!i.m88a(str));
            sb.append("; has token:");
            sb.append(!i.m88a(str2));
            Log.e(str4, sb.toString());
            return "";
        }
        String m85a = c.m85a(this.mContext, str, str2);
        return ((i.m88a(m85a) || !j.a(c.a(this.mContext, str, str2), 1)) && f.m87a(this.mContext)) ? b(str, str2, str3) : m85a;
    }

    private synchronized String b(String str, String str2, String str3) {
        if (this.mContext == null) {
            Log.e(TAG, "no context!");
            return "";
        }
        String a2 = f.m87a(this.mContext) ? b.a(this.mContext).a(str, str2, str3, c.m85a(this.mContext, str, str2)) : "";
        c.a(this.mContext, str, a2, str2);
        return a2;
    }
}
