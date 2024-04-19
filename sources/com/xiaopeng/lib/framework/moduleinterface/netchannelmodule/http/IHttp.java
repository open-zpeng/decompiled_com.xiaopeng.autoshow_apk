package com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http;
/* loaded from: classes.dex */
public interface IHttp {
    IBizHelper bizHelper();

    void cancelTag(Object tag);

    IConfig config();

    IRequest get(String url);

    IRequest head(String url);

    IRequest post(String url);

    IRequest requestXmartBiz(String url);
}
