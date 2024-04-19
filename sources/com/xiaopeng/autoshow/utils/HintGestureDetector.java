package com.xiaopeng.autoshow.utils;

import android.util.Log;
import android.view.MotionEvent;
/* loaded from: classes.dex */
public class HintGestureDetector {
    private static final long INTERVAL = 300;
    private static final String TAG = "HintGestureDetector";
    private int mClickThreshold;
    private OnHintGestureListener mListener;
    private long mLastDownTime = 0;
    private int mClickCount = 0;

    /* loaded from: classes.dex */
    public interface OnHintGestureListener {
        boolean isHintValid(MotionEvent motionEvent);

        void onTrig();
    }

    /* loaded from: classes.dex */
    public static abstract class SimpleHintGestureListener implements OnHintGestureListener {
        @Override // com.xiaopeng.autoshow.utils.HintGestureDetector.OnHintGestureListener
        public boolean isHintValid(MotionEvent motionEvent) {
            return true;
        }

        @Override // com.xiaopeng.autoshow.utils.HintGestureDetector.OnHintGestureListener
        public abstract void onTrig();
    }

    public HintGestureDetector(int i, OnHintGestureListener onHintGestureListener) {
        this.mClickThreshold = 9;
        this.mClickThreshold = i;
        this.mListener = onHintGestureListener;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - this.mLastDownTime < INTERVAL) {
                this.mClickCount++;
                Log.d(TAG, "onTouchEvent clickCount=" + this.mClickCount);
                if (this.mClickCount >= this.mClickThreshold) {
                    OnHintGestureListener onHintGestureListener = this.mListener;
                    if (onHintGestureListener != null) {
                        onHintGestureListener.onTrig();
                    }
                    this.mClickCount = 0;
                    this.mLastDownTime = currentTimeMillis;
                    return true;
                }
            } else {
                this.mClickCount = 0;
            }
            this.mLastDownTime = currentTimeMillis;
        }
        return false;
    }
}
