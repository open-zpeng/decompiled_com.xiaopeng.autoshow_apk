package com.ta.utdid2.c.a;

import com.ta.utdid2.c.a.b;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParserException;
/* compiled from: TransactionXMLFile.java */
/* loaded from: classes.dex */
public class d {
    private static final Object c = new Object();
    private File a;
    private final Object b = new Object();

    /* renamed from: a  reason: collision with other field name */
    private HashMap<File, a> f193a = new HashMap<>();

    public d(String str) {
        if (str != null && str.length() > 0) {
            this.a = new File(str);
            return;
        }
        throw new RuntimeException("Directory can not be empty");
    }

    private File a(File file, String str) {
        if (str.indexOf(File.separatorChar) < 0) {
            return new File(file, str);
        }
        throw new IllegalArgumentException("File " + str + " contains a path separator");
    }

    private File a() {
        File file;
        synchronized (this.b) {
            file = this.a;
        }
        return file;
    }

    private File b(String str) {
        File a2 = a();
        return a(a2, String.valueOf(str) + ".xml");
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x006c, code lost:
        if (r2 == null) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0080, code lost:
        if (r2 == null) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x00b7, code lost:
        if (r3 == null) goto L68;
     */
    /* JADX WARN: Removed duplicated region for block: B:108:0x00c4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:112:0x00d2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x00bc A[Catch: all -> 0x00ce, TRY_ENTER, TRY_LEAVE, TryCatch #11 {all -> 0x00ce, blocks: (B:23:0x004c, B:73:0x00bc), top: B:107:0x003e }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.ta.utdid2.c.a.b a(java.lang.String r10, int r11) {
        /*
            Method dump skipped, instructions count: 244
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.utdid2.c.a.d.a(java.lang.String, int):com.ta.utdid2.c.a.b");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File a(File file) {
        return new File(String.valueOf(file.getPath()) + ".bak");
    }

    /* compiled from: TransactionXMLFile.java */
    /* loaded from: classes.dex */
    private static final class a implements b {
        private static final Object d = new Object();
        private WeakHashMap<b.InterfaceC0022b, Object> a;
        private final File b;
        private final int c;

        /* renamed from: c  reason: collision with other field name */
        private final File f194c;

        /* renamed from: c  reason: collision with other field name */
        private Map f195c;
        private boolean k = false;

        a(File file, int i, Map map) {
            this.b = file;
            this.f194c = d.a(file);
            this.c = i;
            this.f195c = map == null ? new HashMap() : map;
            this.a = new WeakHashMap<>();
        }

        @Override // com.ta.utdid2.c.a.b
        /* renamed from: a */
        public boolean mo89a() {
            File file = this.b;
            return file != null && new File(file.getAbsolutePath()).exists();
        }

        public void a(boolean z) {
            synchronized (this) {
                this.k = z;
            }
        }

        public boolean c() {
            boolean z;
            synchronized (this) {
                z = this.k;
            }
            return z;
        }

        public void a(Map map) {
            if (map != null) {
                synchronized (this) {
                    this.f195c = map;
                }
            }
        }

        @Override // com.ta.utdid2.c.a.b
        public Map<String, ?> getAll() {
            HashMap hashMap;
            synchronized (this) {
                hashMap = new HashMap(this.f195c);
            }
            return hashMap;
        }

        @Override // com.ta.utdid2.c.a.b
        public String getString(String str, String str2) {
            String str3;
            synchronized (this) {
                str3 = (String) this.f195c.get(str);
                if (str3 == null) {
                    str3 = str2;
                }
            }
            return str3;
        }

        @Override // com.ta.utdid2.c.a.b
        public long getLong(String str, long j) {
            synchronized (this) {
                Long l = (Long) this.f195c.get(str);
                if (l != null) {
                    j = l.longValue();
                }
            }
            return j;
        }

        /* compiled from: TransactionXMLFile.java */
        /* renamed from: com.ta.utdid2.c.a.d$a$a  reason: collision with other inner class name */
        /* loaded from: classes.dex */
        public final class C0023a implements b.a {
            private final Map<String, Object> d = new HashMap();
            private boolean l = false;

            public C0023a() {
            }

            @Override // com.ta.utdid2.c.a.b.a
            public b.a a(String str, String str2) {
                synchronized (this) {
                    this.d.put(str, str2);
                }
                return this;
            }

            @Override // com.ta.utdid2.c.a.b.a
            public b.a a(String str, int i) {
                synchronized (this) {
                    this.d.put(str, Integer.valueOf(i));
                }
                return this;
            }

            @Override // com.ta.utdid2.c.a.b.a
            public b.a a(String str, long j) {
                synchronized (this) {
                    this.d.put(str, Long.valueOf(j));
                }
                return this;
            }

            @Override // com.ta.utdid2.c.a.b.a
            public b.a a(String str, float f) {
                synchronized (this) {
                    this.d.put(str, Float.valueOf(f));
                }
                return this;
            }

            @Override // com.ta.utdid2.c.a.b.a
            public b.a a(String str, boolean z) {
                synchronized (this) {
                    this.d.put(str, Boolean.valueOf(z));
                }
                return this;
            }

            @Override // com.ta.utdid2.c.a.b.a
            public b.a a(String str) {
                synchronized (this) {
                    this.d.put(str, this);
                }
                return this;
            }

            @Override // com.ta.utdid2.c.a.b.a
            public b.a b() {
                synchronized (this) {
                    this.l = true;
                }
                return this;
            }

            @Override // com.ta.utdid2.c.a.b.a
            public boolean commit() {
                boolean z;
                ArrayList arrayList;
                HashSet<b.InterfaceC0022b> hashSet;
                boolean d;
                synchronized (d.c) {
                    z = a.this.a.size() > 0;
                    arrayList = null;
                    if (z) {
                        arrayList = new ArrayList();
                        hashSet = new HashSet(a.this.a.keySet());
                    } else {
                        hashSet = null;
                    }
                    synchronized (this) {
                        if (this.l) {
                            a.this.f195c.clear();
                            this.l = false;
                        }
                        for (Map.Entry<String, Object> entry : this.d.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if (value == this) {
                                a.this.f195c.remove(key);
                            } else {
                                a.this.f195c.put(key, value);
                            }
                            if (z) {
                                arrayList.add(key);
                            }
                        }
                        this.d.clear();
                    }
                    d = a.this.d();
                    if (d) {
                        a.this.a(true);
                    }
                }
                if (z) {
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        String str = (String) arrayList.get(size);
                        for (b.InterfaceC0022b interfaceC0022b : hashSet) {
                            if (interfaceC0022b != null) {
                                interfaceC0022b.a(a.this, str);
                            }
                        }
                    }
                }
                return d;
            }
        }

        @Override // com.ta.utdid2.c.a.b
        public b.a a() {
            return new C0023a();
        }

        private FileOutputStream a(File file) {
            try {
                return new FileOutputStream(file);
            } catch (FileNotFoundException unused) {
                if (file.getParentFile().mkdir()) {
                    try {
                        return new FileOutputStream(file);
                    } catch (FileNotFoundException unused2) {
                        return null;
                    }
                }
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean d() {
            if (this.b.exists()) {
                if (!this.f194c.exists()) {
                    if (!this.b.renameTo(this.f194c)) {
                        return false;
                    }
                } else {
                    this.b.delete();
                }
            }
            try {
                FileOutputStream a = a(this.b);
                if (a == null) {
                    return false;
                }
                e.a(this.f195c, a);
                a.close();
                this.f194c.delete();
                return true;
            } catch (IOException | XmlPullParserException unused) {
                if (this.b.exists()) {
                    this.b.delete();
                }
                return false;
            }
        }
    }
}
