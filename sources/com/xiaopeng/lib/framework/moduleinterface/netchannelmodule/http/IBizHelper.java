package com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http;

import java.util.Map;
/* loaded from: classes.dex */
public interface IBizHelper {
    IBizHelper appId(String value);

    IRequest build();

    IRequest buildWithSecretKey(String secretKey);

    IBizHelper customTokensForAuth(String[] tokens);

    @Deprecated
    IBizHelper enableIrdetoEncoding();

    IBizHelper enableSecurityEncoding();

    IBizHelper extendBizHeader(String header, String value);

    IBizHelper get(String url);

    IBizHelper needAuthorizationInfo();

    IBizHelper needAuthorizationInfo(Map<String, String> extParams);

    IBizHelper post(String url, String jsonBody);

    IBizHelper uid(String value);
}
