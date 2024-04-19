package com.xiaopeng.autoshow.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
/* loaded from: classes.dex */
public final class GsonUtil {
    private static final String TAG = GsonUtil.class.getSimpleName();
    private static final Gson GSON = createGson(true);
    private static final Gson GSON_NO_NULLS = createGson(false);

    private GsonUtil() {
        throw new UnsupportedOperationException("can't instantiate Gson ...");
    }

    private static Gson createGson(boolean z) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (z) {
            gsonBuilder.serializeNulls();
        }
        return gsonBuilder.create();
    }

    public static <T> T fromJson(String str, Class<T> cls) {
        return (T) GSON.fromJson(str, (Class<Object>) cls);
    }

    public static <T> T fromJson(String str, Type type) {
        return (T) GSON.fromJson(str, type);
    }
}
