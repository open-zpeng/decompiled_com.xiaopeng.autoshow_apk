package com.xiaopeng.autoshow.llu;

import com.google.gson.reflect.TypeToken;
import com.xiaopeng.autoshow.utils.FileUtil;
import com.xiaopeng.autoshow.utils.GsonUtil;
import com.xiaopeng.autoshow.utils.LogUtils;
import com.xiaopeng.libconfig.ipc.IpcConfig;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class CycleLluPresenter implements ICycleLluListener {
    private static final String ASSETS_LLU_EFFECT_DATA = "llu_effect.json";
    private Disposable mDisposable;
    private Observable<Long> mObservable;
    private Deque<LluBean> mPlayDeque;
    private final int INTERVAL_TIME = IpcConfig.BughunterConfig.IPC_COMMAND_ID_ICM_CRASH;
    private final int DEFAULT_TIME = 0;
    private final String TAG = CycleLluPresenter.class.getSimpleName();
    private List<LluBean> mCacheLluBean = new ArrayList();
    private boolean hasConnectApi = false;

    public CycleLluPresenter() {
        init();
    }

    private void init() {
        LogUtils.d(this.TAG, "init CycleLluPresenter...");
        loadData();
        connectApi();
        LluManager.getInstance().setIcyclelluListener(this);
    }

    public synchronized void connectApi() {
        if (!this.hasConnectApi) {
            LluManager.getInstance().connectApi();
            this.hasConnectApi = true;
        }
    }

    private void loadData() {
        if (this.mPlayDeque == null) {
            this.mPlayDeque = new ArrayDeque();
        }
        if (this.mCacheLluBean == null) {
            this.mCacheLluBean = new ArrayList();
        }
        if (this.mCacheLluBean.size() == 0) {
            List list = null;
            try {
                list = (List) GsonUtil.fromJson(FileUtil.loadFromAssets(ASSETS_LLU_EFFECT_DATA), new TypeToken<List<LluBean>>() { // from class: com.xiaopeng.autoshow.llu.CycleLluPresenter.1
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.d(this.TAG, "loadFromAssets...");
            }
            if (list != null) {
                this.mCacheLluBean.addAll(list);
            }
        }
        this.mPlayDeque.addAll(this.mCacheLluBean);
    }

    private void startPlay(boolean z) {
        if (this.mPlayDeque == null) {
            LogUtils.i(this.TAG, "PlayDeque is null");
        }
        final LluBean pollFirst = this.mPlayDeque.pollFirst();
        if (pollFirst == null) {
            loadData();
            pollFirst = this.mPlayDeque.pollFirst();
        }
        if (pollFirst == null) {
            LogUtils.i(this.TAG, "bean is null");
        } else if (!z) {
            String str = this.TAG;
            LogUtils.d(str, "startPlay=>effect name :" + pollFirst.getExpoModeName());
            LluManager.getInstance().startPlayLlu(pollFirst.getExpoModeName());
        } else {
            Disposable disposable = this.mDisposable;
            if (disposable == null || disposable.isDisposed()) {
                this.mObservable = Observable.timer(4000L, TimeUnit.MILLISECONDS);
            }
            this.mObservable.subscribe(new Observer<Long>() { // from class: com.xiaopeng.autoshow.llu.CycleLluPresenter.2
                @Override // io.reactivex.Observer
                public void onSubscribe(Disposable disposable2) {
                    CycleLluPresenter.this.mDisposable = disposable2;
                }

                @Override // io.reactivex.Observer
                public void onNext(Long l) {
                    String str2 = CycleLluPresenter.this.TAG;
                    LogUtils.d(str2, "startPlay delay=>aLong，" + l + "effect name :" + pollFirst.getExpoModeName());
                    LluManager.getInstance().startPlayLlu(pollFirst.getExpoModeName());
                }

                @Override // io.reactivex.Observer
                public void onError(Throwable th) {
                    th.printStackTrace();
                    LogUtils.d(CycleLluPresenter.this.TAG, "onError");
                }

                @Override // io.reactivex.Observer
                public void onComplete() {
                    LogUtils.d(CycleLluPresenter.this.TAG, "onComplete:");
                }
            });
        }
    }

    @Override // com.xiaopeng.autoshow.llu.ICycleLluListener
    public void onStartLlu(boolean z) {
        LogUtils.i(this.TAG, "onStartLlu... ");
        if (z) {
            startPlay(false);
        }
    }

    @Override // com.xiaopeng.autoshow.llu.ICycleLluListener
    public void onStopLlu() {
        LogUtils.i(this.TAG, "onStopLlu... ");
    }

    @Override // com.xiaopeng.autoshow.llu.ICycleLluListener
    public void onInterruptLlu(boolean z) {
        LogUtils.i(this.TAG, "onInterruptLlu... ");
        if (z) {
            LluManager.getInstance().stopPlayLlu();
        }
        destroyDisposable();
    }

    @Override // com.xiaopeng.autoshow.llu.ICycleLluListener
    public void onContinueLlu() {
        LogUtils.i(this.TAG, "onContinueLlu... ");
        startPlay(true);
    }

    public void destroy() {
        LogUtils.d(this.TAG, "destroy...");
        destroyDisposable();
        LluManager.getInstance().setIcyclelluListener(null);
        LluManager.getInstance().stopPlayLlu();
        LluManager.getInstance().release();
    }

    private void destroyDisposable() {
        Disposable disposable = this.mDisposable;
        if (disposable != null) {
            disposable.dispose();
            this.mDisposable = null;
        }
        if (this.mObservable != null) {
            this.mObservable = null;
        }
    }
}
