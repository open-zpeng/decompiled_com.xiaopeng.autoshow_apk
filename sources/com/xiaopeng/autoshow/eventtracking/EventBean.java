package com.xiaopeng.autoshow.eventtracking;

import com.xiaopeng.autoshow.utils.LogUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes.dex */
class EventBean {
    private String mButtonId;
    private String mButtonName;
    private int mLevel;
    private String mPageId;
    private String mPageName;
    private Map<String, Object> mParams;

    private EventBean(Builder builder) {
        this.mPageId = builder.mPageId;
        this.mPageName = builder.mPageName;
        this.mButtonId = builder.mButtonId;
        this.mButtonName = builder.mButtonName;
        this.mLevel = builder.mLevel;
        this.mParams = builder.mParams;
    }

    public String toString() {
        return "EventBean{mPageId='" + this.mPageId + "', mPageName='" + this.mPageName + "', mButtonId='" + this.mButtonId + "', mButtonName='" + this.mButtonName + "', mLevel=" + this.mLevel + ", mParams=" + this.mParams + '}';
    }

    public String getPageId() {
        return this.mPageId;
    }

    public String getButtonId() {
        return this.mButtonId;
    }

    public int getLevel() {
        return this.mLevel;
    }

    public Map<String, Object> getParams() {
        return this.mParams;
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private static final String TAG = Builder.class.getSimpleName();
        private String mButtonId;
        private String mButtonName;
        private int mLevel;
        private String mPageId;
        private String mPageName;
        private Map<String, Object> mParams = new ConcurrentHashMap();

        public Builder pageId(String str) {
            this.mPageId = str;
            return this;
        }

        public Builder pageName(String str) {
            this.mPageName = str;
            return this;
        }

        public Builder buttonId(String str) {
            this.mButtonId = str;
            return this;
        }

        public Builder buttonName(String str) {
            this.mButtonName = str;
            return this;
        }

        public Builder level(int i) {
            this.mLevel = i;
            return this;
        }

        public Builder putParam(String str, String str2) {
            this.mParams.put(str, str2);
            return this;
        }

        public Builder putParam(String str, Boolean bool) {
            this.mParams.put(str, bool);
            return this;
        }

        public Builder putParam(String str, Character ch) {
            this.mParams.put(str, ch);
            return this;
        }

        public Builder putParam(String str, Number number) {
            this.mParams.put(str, number);
            return this;
        }

        public Builder setParams(Map<String, Object> map) {
            if (map != null) {
                this.mParams = map;
            } else {
                LogUtils.e("setParams value can not be null");
            }
            return this;
        }

        public EventBean build() {
            return new EventBean(this);
        }
    }
}
