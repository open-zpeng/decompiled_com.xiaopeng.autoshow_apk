package com.xiaopeng.lib.framework.netchannelmodule.http.xmart;

import com.google.gson.annotations.SerializedName;
import com.lzy.okgo.cache.CacheEntity;
import com.xiaopeng.autoshow.Config;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.xmart.IServerBean;
/* loaded from: classes.dex */
public class ServerBean implements IServerBean {
    @SerializedName(Config.CODE_KEY)
    private int mCode;
    @SerializedName(CacheEntity.DATA)
    private String mData;
    @SerializedName("msg")
    private String mMsg;

    @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.xmart.IServerBean
    public int code() {
        return this.mCode;
    }

    public void code(int i) {
        this.mCode = i;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.xmart.IServerBean
    public String data() {
        return this.mData;
    }

    public void data(String str) {
        this.mData = str;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.xmart.IServerBean
    public String message() {
        return this.mMsg;
    }

    public void message(String str) {
        this.mMsg = str;
    }
}
