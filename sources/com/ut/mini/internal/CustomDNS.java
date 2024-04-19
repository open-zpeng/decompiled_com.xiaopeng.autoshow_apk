package com.ut.mini.internal;
/* loaded from: classes.dex */
public class CustomDNS {
    private IDnsResolver a;

    /* loaded from: classes.dex */
    public interface IDnsResolver {
        String[] resolveUrl(String str);
    }

    public static CustomDNS instance() {
        return a.a;
    }

    private CustomDNS() {
        this.a = null;
    }

    public void setDnsResolver(IDnsResolver iDnsResolver) {
        this.a = iDnsResolver;
    }

    public String[] resolveUrl(String str) {
        IDnsResolver iDnsResolver = this.a;
        if (iDnsResolver != null) {
            return iDnsResolver.resolveUrl(str);
        }
        return null;
    }

    /* loaded from: classes.dex */
    private static class a {
        private static final CustomDNS a = new CustomDNS();
    }
}
