package com.xiaopeng.autoshow;

import android.os.CountDownTimer;
/* loaded from: classes.dex */
public class CodeCountDownTimer extends CountDownTimer {
    ITimer mITimer;

    public CodeCountDownTimer(long j, long j2, ITimer iTimer) {
        super(j, j2);
        this.mITimer = iTimer;
    }

    @Override // android.os.CountDownTimer
    public void onTick(long j) {
        this.mITimer.onTick(j);
    }

    @Override // android.os.CountDownTimer
    public void onFinish() {
        this.mITimer.onFinish();
    }
}
