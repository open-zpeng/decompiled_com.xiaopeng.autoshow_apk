package com.xiaopeng.lib.bughunter;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.xiaopeng.lib.bughunter.anr.Collector;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
/* loaded from: classes.dex */
public class StackTraceCollector implements Collector {
    private static final String CATON_STACK_INFO = "caton_stack_info";
    private static final int COLLECT_SPACE_TIME = 3000;
    private static final int MIN_COLLECT_COUNT = 3;
    private static final int MSG_BEGIN_WATCH = 54;
    private static final int MSG_COLLECT_CONTINUE = 55;
    private static final String TAG = "StackTraceCollector";
    private static final String THREAD_TAG = "-----";
    private long mCollectInterval;
    private volatile CollectorHandler mCollectorHandler;
    private volatile boolean mIsWatching;
    private StackTraceElement[] mLastStackTrace;
    private int mLimitLength;
    private Thread mMainThread;
    private int[] mRepeatTimes;
    private ArrayList<StackTraceElement[]> mStackQueue;

    public StackTraceCollector(long j) {
        this.mCollectInterval = j;
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.setPriority(10);
        handlerThread.start();
        this.mCollectorHandler = new CollectorHandler(handlerThread.getLooper());
        this.mLimitLength = 3;
        this.mStackQueue = new ArrayList<>(this.mLimitLength);
        this.mRepeatTimes = new int[this.mLimitLength];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reset() {
        synchronized (this.mStackQueue) {
            if (!this.mStackQueue.isEmpty()) {
                this.mLastStackTrace = null;
                this.mStackQueue.clear();
                Arrays.fill(this.mRepeatTimes, 0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void increaseRepeatTimes() {
        synchronized (this.mStackQueue) {
            int size = this.mStackQueue.size() - 1;
            this.mRepeatTimes[size] = this.mRepeatTimes[size] + 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void trigger(int i) {
        Message obtainMessage = this.mCollectorHandler.obtainMessage();
        obtainMessage.obj = this;
        obtainMessage.what = i;
        this.mCollectorHandler.sendMessageDelayed(obtainMessage, this.mCollectInterval);
    }

    public boolean isWatching() {
        return this.mIsWatching;
    }

    @Override // com.xiaopeng.lib.bughunter.anr.Collector
    public void start() {
        this.mIsWatching = true;
        trigger(54);
    }

    @Override // com.xiaopeng.lib.bughunter.anr.Collector
    public void stop() {
        this.mIsWatching = false;
        this.mCollectorHandler.removeMessages(54);
        this.mCollectorHandler.removeMessages(55);
    }

    @Override // com.xiaopeng.lib.bughunter.anr.Collector
    public int[] getStackTraceRepeats() {
        int[] copyOf;
        synchronized (this.mStackQueue) {
            copyOf = Arrays.copyOf(this.mRepeatTimes, this.mRepeatTimes.length);
        }
        return copyOf;
    }

    @Override // com.xiaopeng.lib.bughunter.anr.Collector
    public StackTraceElement[][] getStackTraceInfo() {
        StackTraceElement[][] stackTraceElementArr;
        synchronized (this.mStackQueue) {
            stackTraceElementArr = (StackTraceElement[][]) this.mStackQueue.toArray((StackTraceElement[][]) Array.newInstance(StackTraceElement.class, 0, 0));
        }
        return stackTraceElementArr;
    }

    @Override // com.xiaopeng.lib.bughunter.anr.Collector
    public void add(StackTraceElement[] stackTraceElementArr) {
        synchronized (this.mStackQueue) {
            this.mLastStackTrace = stackTraceElementArr;
            if (this.mStackQueue.size() >= this.mLimitLength) {
                int i = this.mLimitLength - 1;
                int i2 = this.mRepeatTimes[i];
                int i3 = i;
                for (int i4 = i - 1; i4 >= 1; i4--) {
                    if (i2 > this.mRepeatTimes[i4]) {
                        i2 = this.mRepeatTimes[i4];
                        i3 = i4;
                    }
                }
                this.mStackQueue.remove(i3);
                while (i3 < i) {
                    int i5 = i3 + 1;
                    this.mRepeatTimes[i3] = this.mRepeatTimes[i5];
                    i3 = i5;
                }
                this.mRepeatTimes[i] = 0;
            }
            this.mStackQueue.add(stackTraceElementArr);
            this.mRepeatTimes[this.mStackQueue.size() - 1] = 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class CollectorHandler extends Handler {
        public CollectorHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 54 || message.what == 55) {
                if (message.what == 54) {
                    StackTraceCollector.this.reset();
                }
                StackTraceElement[] mainThreadStackInfo = StackTraceCollector.this.getMainThreadStackInfo();
                if (StackTraceCollector.isEqualsAndNotNull(mainThreadStackInfo, StackTraceCollector.this.mLastStackTrace)) {
                    StackTraceCollector.this.increaseRepeatTimes();
                } else {
                    StackTraceCollector.this.add(mainThreadStackInfo);
                }
                if (StackTraceCollector.this.isWatching()) {
                    StackTraceCollector.this.trigger(55);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StackTraceElement[] getMainThreadStackInfo() {
        if (this.mMainThread == null) {
            this.mMainThread = Looper.getMainLooper().getThread();
        }
        return this.mMainThread.getStackTrace();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isEqualsAndNotNull(StackTraceElement[] stackTraceElementArr, StackTraceElement[] stackTraceElementArr2) {
        int length;
        if (stackTraceElementArr == null || stackTraceElementArr2 == null || (length = stackTraceElementArr.length) != stackTraceElementArr2.length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (!stackTraceElementArr[i].equals(stackTraceElementArr2[i])) {
                return false;
            }
        }
        return true;
    }
}
