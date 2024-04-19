package com.xiaopeng.lib.framework.netchannelmodule.http.xmart;

import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.convert.Converter;
import com.xiaopeng.autoshow.Config;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class ServerConverter implements Converter<ServerBean> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lzy.okgo.convert.Converter
    public ServerBean convertResponse(Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) {
            throw new IllegalStateException("null");
        }
        ServerBean serverBean = new ServerBean();
        JSONObject jSONObject = new JSONObject(body.string());
        serverBean.code(jSONObject.getInt(Config.CODE_KEY));
        try {
            serverBean.data(jSONObject.getString(CacheEntity.DATA));
        } catch (Throwable unused) {
        }
        try {
            serverBean.message(jSONObject.getString("msg"));
        } catch (Throwable unused2) {
        }
        return serverBean;
    }
}
