package com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.websocket;

import io.reactivex.Observable;
import okio.ByteString;
/* loaded from: classes.dex */
public interface IRxWebSocket {
    void close(String url);

    IWebSocketConfig config();

    Observable<IWebSocketInfo> get(String url);

    Observable<IWebSocketInfo> get(String url, long timeout);

    IRxWebSocket header(String key, String value);

    void send(String url, String msg) throws Exception;

    void send(String url, ByteString byteString) throws Exception;
}
