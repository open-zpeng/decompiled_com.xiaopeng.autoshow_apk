package com.xiaopeng.lib.http.server;

import com.google.gson.annotations.SerializedName;
import com.lzy.okgo.cache.CacheEntity;
import com.xiaopeng.autoshow.Config;
/* loaded from: classes.dex */
public class ServerBean {
    @SerializedName(Config.CODE_KEY)
    private int mCode;
    @SerializedName(CacheEntity.DATA)
    private String mData;
    @SerializedName("msg")
    private String mMsg;

    public int getCode() {
        return this.mCode;
    }

    public void setCode(int i) {
        this.mCode = i;
    }

    public String getData() {
        return this.mData;
    }

    public void setData(String str) {
        this.mData = str;
    }

    public String getMsg() {
        return this.mMsg;
    }

    public void setMsg(String str) {
        this.mMsg = str;
    }
}
