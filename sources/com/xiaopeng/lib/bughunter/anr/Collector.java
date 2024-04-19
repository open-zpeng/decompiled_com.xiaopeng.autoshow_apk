package com.xiaopeng.lib.bughunter.anr;
/* loaded from: classes.dex */
public interface Collector {
    void add(StackTraceElement[] stackTraceElementArr);

    StackTraceElement[][] getStackTraceInfo();

    int[] getStackTraceRepeats();

    void start();

    void stop();
}
