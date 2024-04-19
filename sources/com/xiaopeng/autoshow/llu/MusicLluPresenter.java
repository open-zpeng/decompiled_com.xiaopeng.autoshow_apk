package com.xiaopeng.autoshow.llu;

import com.xiaopeng.autoshow.utils.LogUtils;
/* loaded from: classes.dex */
public class MusicLluPresenter implements IMusicLluListener {
    private static final String TAG = MusicLluPresenter.class.getSimpleName();

    public void init() {
        LogUtils.d(TAG, "init MusicLluPresenter...");
        LluManager.getInstance().setIMusicLluListener(this);
        LluManager.getInstance().connectApi();
    }

    public void release() {
        LogUtils.d(TAG, "release MusicLluPresenter...");
        setMusicLluPause();
        LluManager.getInstance().setIMusicLluListener(null);
        LluManager.getInstance().release();
    }

    @Override // com.xiaopeng.autoshow.llu.IMusicLluListener
    public void onStartMusicLlu() {
        LogUtils.d(TAG, "onStartMusicLlu...");
        LluManager.getInstance().setMusicLlu(true);
    }

    public void setMusicLluPause() {
        LogUtils.d(TAG, "setMusicLluPause:");
        LluManager.getInstance().setMusicLlu(false);
    }
}
