package com.xiaopeng.autoshow.dialog.model;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.irdeto.securesdk.core.SSUtils;
import com.xiaopeng.autoshow.AutoShowApp;
import com.xiaopeng.autoshow.Config;
import com.xiaopeng.autoshow.EnvConfig;
import com.xiaopeng.autoshow.model.AutoShowModel;
import com.xiaopeng.autoshow.utils.ContentResolverUtil;
import com.xiaopeng.autoshow.utils.LogUtils;
import com.xiaopeng.autoshow.vcu.manager.XpVcuManager;
import com.xiaopeng.lib.framework.module.Module;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IHttp;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IRequest;
import com.xiaopeng.lib.framework.netchannelmodule.NetworkChannelsEntry;
import com.xiaopeng.lib.framework.netchannelmodule.common.TrafficeStaFlagInterceptor;
import com.xiaopeng.lib.utils.SharedPreferencesUtils;
import com.xiaopeng.lib.utils.SystemPropertyUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class DialogModel implements IDialogModel {
    protected final String TAG = getClass().getSimpleName();

    @Override // com.xiaopeng.autoshow.dialog.model.IDialogModel
    public void onCheckedChanged(int i, boolean z) {
        String str = i == 2 ? Config.AUTO_SHOW_AIRCONDITION_CONFIG_KEY : i == 4 ? Config.AUTO_SHOW_VOLUME_CONFIG_KEY : i == 5 ? Config.AUTO_SHOW_SETTING_WELCOME_KEY : "";
        if (!TextUtils.isEmpty(str)) {
            boolean putBooleanValue = ContentResolverUtil.putBooleanValue(str, z);
            String str2 = this.TAG;
            LogUtils.i(str2, "event:" + i + " & result:" + putBooleanValue + " & isChecked:" + z);
        }
        if (i == 4) {
            AutoShowModel.getInstance().decreaseVolume(z);
        }
    }

    @Override // com.xiaopeng.autoshow.dialog.model.IDialogModel
    public void onTabChanged(int i) {
        boolean putIntValue = ContentResolverUtil.putIntValue(Config.AUTO_SHOW_SETTING_LIGHTING_LANGUAGE_KEY, i);
        String str = this.TAG;
        LogUtils.i(str, "设置灯语效果为：" + i + " ; 是否设置成功" + putIntValue);
        AutoShowModel.getInstance().doLlu(i);
    }

    @Override // com.xiaopeng.autoshow.dialog.model.IDialogModel
    public int getLluTabIndex() {
        return ContentResolverUtil.getIntValue(Config.AUTO_SHOW_SETTING_LIGHTING_LANGUAGE_KEY);
    }

    @Override // com.xiaopeng.autoshow.dialog.model.IDialogModel
    public boolean getConfig(int i) {
        return ContentResolverUtil.getBooleanValue(i == 2 ? Config.AUTO_SHOW_AIRCONDITION_CONFIG_KEY : i == 4 ? Config.AUTO_SHOW_VOLUME_CONFIG_KEY : i == 5 ? Config.AUTO_SHOW_SETTING_WELCOME_KEY : "");
    }

    @Override // com.xiaopeng.autoshow.dialog.model.IDialogModel
    public void onModelChanged(int i) {
        boolean putIntValue = ContentResolverUtil.putIntValue(Config.AUTO_SHOW_MODEL_SETTING_CONFIG_KEY, i);
        String str = this.TAG;
        LogUtils.i(str, "model:" + i + " ; result:" + putIntValue);
    }

    @Override // com.xiaopeng.autoshow.dialog.model.IDialogModel
    public int getCurrentModel() {
        return ContentResolverUtil.getIntValue(Config.AUTO_SHOW_MODEL_SETTING_CONFIG_KEY);
    }

    @Override // com.xiaopeng.autoshow.dialog.model.IDialogModel
    public boolean checkGearLimit() {
        return XpVcuManager.get().checkGearLimit();
    }

    @Override // com.xiaopeng.autoshow.dialog.model.IDialogModel
    public void saveOperatorNo(String str) {
        SharedPreferencesUtils.getInstance(AutoShowApp.getContext()).putString(Config.AUTO_SHOW_USER_PHONE_NUM, str);
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:14:0x00d0 -> B:19:0x00d8). Please submit an issue!!! */
    @Override // com.xiaopeng.autoshow.dialog.model.IDialogModel
    public void uploadGearState(String str) {
        HashMap hashMap = new HashMap();
        String vin = SystemPropertyUtil.getVIN();
        hashMap.put(SSUtils.O0000Ooo, vin);
        hashMap.put("status", str);
        hashMap.put("operate_time", System.currentTimeMillis() + "");
        String str2 = this.TAG;
        LogUtils.i(str2, "vin:" + vin + " & status:" + str);
        IRequest request = getRequest(getHttp(), getUrl(), hashMap);
        if (request == null) {
            LogUtils.i(this.TAG, "request == null");
            return;
        }
        try {
            String body = request.execute().body();
            String str3 = this.TAG;
            LogUtils.i(str3, "body:" + body);
            if (!TextUtils.isEmpty(body)) {
                try {
                    JSONObject jSONObject = new JSONObject(body);
                    int i = jSONObject.getInt(Config.CODE_KEY);
                    String str4 = this.TAG;
                    LogUtils.i(str4, "code:" + i);
                    if (i == 200) {
                        LogUtils.i(this.TAG, "Gear Limit Upload Success!!");
                    } else {
                        String string = jSONObject.getString("msg");
                        String str5 = this.TAG;
                        LogUtils.i(str5, "error:" + string);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private IRequest getRequest(IHttp iHttp, String str, Map<String, String> map) {
        String str2 = this.TAG;
        LogUtils.i(str2, "url:" + str);
        if (iHttp != null) {
            return iHttp.bizHelper().post(str, new Gson().toJson(map)).needAuthorizationInfo().enableSecurityEncoding().build();
        }
        return null;
    }

    private String getUrl() {
        String str = EnvConfig.getEnv() == 1 ? Config.GEAR_UPLOAD_TEST_HOST : Config.GEAR_UPLOAD_PRODUCT_HOST;
        return str + Config.GEAR_UPLOAD_URL;
    }

    private IHttp getHttp() {
        IHttp iHttp = (IHttp) Module.get(NetworkChannelsEntry.class).get(IHttp.class);
        iHttp.config().applicationContext(AutoShowApp.getInstance()).addInterceptor(new TrafficeStaFlagInterceptor()).apply();
        return iHttp;
    }
}
