package com.xiaopeng.autoshow.utils;

import android.car.Car;
import com.google.gson.Gson;
import com.irdeto.securesdk.core.SSUtils;
import com.xiaopeng.autoshow.AutoShowApp;
import com.xiaopeng.autoshow.EnvConfig;
import com.xiaopeng.autoshow.R;
import com.xiaopeng.autoshow.eventtracking.EventTrackingHelper;
import com.xiaopeng.autoshow.model.FeedBackBean;
import com.xiaopeng.lib.framework.module.Module;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IHttp;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IRequest;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IResponse;
import com.xiaopeng.lib.framework.netchannelmodule.NetworkChannelsEntry;
import com.xiaopeng.lib.framework.netchannelmodule.common.TrafficeStaFlagInterceptor;
import com.xiaopeng.lib.utils.SystemPropertyUtil;
import com.xiaopeng.lib.utils.ThreadUtils;
import com.xiaopeng.lib.utils.info.BuildInfoUtils;
import com.xiaopeng.libconfig.ipc.AccountConfig;
import com.xiaopeng.xui.app.XToast;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
/* loaded from: classes.dex */
public class FeedbackUtil {
    private static final String CHANNEL_ADVICE = "0";
    private static final String CHANNEL_LIMIT_GEAR = "1";
    private static final String TAG = "FeedbackUtil";
    private static final String TYPE_ADVICE = "1";
    private static final String TYPE_FAULT = "0";
    private static final String TYPE_OTHER = "2";
    private static final String accessKey = "199f696b-e17d-44ae-b6e2-a669f588a36f";
    private static final String holly = "lockE28";

    public static void sendLimitGear(String str) {
        sendLimitGear(str, null);
    }

    public static void sendLimitGear(String str, OnGearLimitListener onGearLimitListener) {
        sendRequest(str, AutoShowApp.getInstance().getResources().getString(R.string.apply_limit_gear), onGearLimitListener);
        EventTrackingHelper.sendMolecast(6, str, false);
    }

    public static void sendRelieveGear(String str) {
        sendRequest(str, AutoShowApp.getInstance().getResources().getString(R.string.apply_relieve_gear), null);
        EventTrackingHelper.sendMolecast(6, str, true);
    }

    private static void sendRequest(final String str, final String str2, final OnGearLimitListener onGearLimitListener) {
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.autoshow.utils.-$$Lambda$FeedbackUtil$uzJ2MvvlaiV-UxizC0yN_VGmLpE
            @Override // java.lang.Runnable
            public final void run() {
                FeedbackUtil.lambda$sendRequest$2(str, str2, onGearLimitListener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$sendRequest$2(String str, final String str2, OnGearLimitListener onGearLimitListener) {
        HashMap hashMap = new HashMap();
        hashMap.put("requestId", UUID.randomUUID() + "");
        hashMap.put("requestTime", System.currentTimeMillis() + "");
        hashMap.put(SSUtils.O0000Ooo, SystemPropertyUtil.getVIN());
        hashMap.put("content", String.format(AutoShowApp.getInstance().getResources().getString(R.string.apply_content), str, str2));
        hashMap.put("channel", "1");
        hashMap.put("type", "2");
        try {
            hashMap.put("carType", Car.getHardwareCarType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        IHttp http = getHttp();
        String url = getUrl();
        IRequest build = http.bizHelper().post(url, new Gson().toJson(hashMap)).needAuthorizationInfo().enableSecurityEncoding().build();
        build.headers("holly", holly);
        String sign = getSign(accessKey, hashMap);
        LogUtils.d(TAG, "sign:  " + sign);
        build.headers(AccountConfig.KEY_ACCESS_TOKEN, sign);
        try {
            LogUtils.i(TAG, "doPostSync uri:" + url + ",params:" + hashMap + ",time:" + System.currentTimeMillis());
            IResponse execute = build.execute();
            String body = execute.body();
            LogUtils.i(TAG, "response code:" + execute.code() + ",response body:" + body + ",response message:" + execute.message());
            if (body != null) {
                if (((FeedBackBean) GsonUtil.fromJson(body, (Class<Object>) FeedBackBean.class)).getCode() == 0) {
                    ThreadUtils.postMainThread(new Runnable() { // from class: com.xiaopeng.autoshow.utils.-$$Lambda$FeedbackUtil$oFjyE2v_V_t7ZbVTxNPnsb4Gz7c
                        @Override // java.lang.Runnable
                        public final void run() {
                            XToast.showShort(str2 + "已提交，正在处理,请稍后");
                        }
                    });
                    if (onGearLimitListener != null) {
                        onGearLimitListener.success();
                    }
                } else {
                    ThreadUtils.postMainThread(new Runnable() { // from class: com.xiaopeng.autoshow.utils.-$$Lambda$FeedbackUtil$7BlVD9wWCM-03rhiumPc80n83WE
                        @Override // java.lang.Runnable
                        public final void run() {
                            XToast.showShort(str2 + "提交失败，请重新发送申请");
                        }
                    });
                    if (onGearLimitListener != null) {
                        onGearLimitListener.error();
                    }
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private static String getUrl() {
        return (BuildInfoUtils.isDebuggableVersion() && EnvConfig.getEnv() == 1) ? "http://test-cs-api-ext.xiaopeng.com/is-center/feedback/create" : "https://cs-api-ext.xiaopeng.com/is-center/feedback/create";
    }

    private static IHttp getHttp() {
        IHttp iHttp = (IHttp) Module.get(NetworkChannelsEntry.class).get(IHttp.class);
        iHttp.config().applicationContext(AutoShowApp.getInstance()).addInterceptor(new TrafficeStaFlagInterceptor()).apply();
        return iHttp;
    }

    private static String getSign(String str, Map<String, String> map) {
        String str2;
        ArrayList arrayList = new ArrayList();
        for (String str3 : map.keySet()) {
            arrayList.add(str3);
        }
        Collections.sort(arrayList);
        Iterator it = arrayList.iterator();
        String str4 = "";
        while (it.hasNext()) {
            str4 = str4 + ((String) it.next()) + map.get(str2);
        }
        return getValUtf(str + str4);
    }

    private static String getValUtf(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] digest = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer("");
            for (int i = 0; i < digest.length; i++) {
                int i2 = digest[i];
                if (i2 < 0) {
                    i2 += 256;
                }
                if (i2 < 16) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(Integer.toHexString(i2));
            }
            System.out.println(stringBuffer.toString().toUpperCase());
            return stringBuffer.toString().toUpperCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            return null;
        }
    }
}
