package com.xiaopeng.autoshow.dialog;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.irdeto.securesdk.core.SSUtils;
import com.xiaopeng.autoshow.AutoShowApp;
import com.xiaopeng.autoshow.CodeCountDownTimer;
import com.xiaopeng.autoshow.Config;
import com.xiaopeng.autoshow.ITimer;
import com.xiaopeng.autoshow.R;
import com.xiaopeng.autoshow.utils.LogUtils;
import com.xiaopeng.lib.framework.module.Module;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IHttp;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IRequest;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IResponse;
import com.xiaopeng.lib.framework.netchannelmodule.NetworkChannelsEntry;
import com.xiaopeng.lib.framework.netchannelmodule.common.TrafficeStaFlagInterceptor;
import com.xiaopeng.lib.utils.NetUtils;
import com.xiaopeng.lib.utils.SystemPropertyUtil;
import com.xiaopeng.xui.app.XDialogSystemType;
import com.xiaopeng.xui.app.XToast;
import com.xiaopeng.xui.widget.XButton;
import com.xiaopeng.xui.widget.XTextFields;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
/* loaded from: classes.dex */
public class VerityDialog extends Dialog implements ITimer, ComponentCallbacks, DialogInterface.OnDismissListener {
    private static final String TAG = "VerityDialog";
    private static final long TOTAL_COUNT_TIME = 60000;
    private boolean isCountDown;
    private boolean isPassed;
    private XButton mBtn;
    private XButton mBtnClose;
    private IVerityCallback mCallback;
    private CodeCountDownTimer mCodeCountDownTimer;
    private XTextFields mInputNumber;
    private PmBroadcastReceiver mPmBroadcastReceiver;
    private XButton mTvSendCode;
    private XTextFields mVerityNumber;

    @Override // android.content.ComponentCallbacks
    public void onLowMemory() {
    }

    public VerityDialog(Context context) {
        this(context, null);
    }

    public VerityDialog(Context context, IVerityCallback iVerityCallback) {
        super(context, 2131755379);
        this.isPassed = false;
        this.isCountDown = false;
        setContentView(R.layout.dialog_vertify);
        initDialog();
        setListener();
        this.mCallback = iVerityCallback;
    }

    private void setListener() {
        setInputNumberListener();
        setInputVerifyListener();
        setSendCodeListener();
        setBtnListener();
        setBtnCloseListener();
        setCanceledOnTouchOutside(false);
    }

    private void setBtnCloseListener() {
        this.mBtnClose.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.autoshow.dialog.VerityDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VerityDialog.this.dismiss();
            }
        });
    }

    private void setBtnListener() {
        this.mBtn.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.autoshow.dialog.VerityDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VerityDialog verityDialog = VerityDialog.this;
                verityDialog.submitAuthorization(verityDialog.mInputNumber.getText(), VerityDialog.this.mVerityNumber.getText());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void submitAuthorization(final String str, final String str2) {
        Observable.create(new ObservableOnSubscribe<IResponse>() { // from class: com.xiaopeng.autoshow.dialog.VerityDialog.4
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(ObservableEmitter<IResponse> observableEmitter) throws Exception {
                observableEmitter.onNext(VerityDialog.this.doCheckVerifyCodeSync(str, str2));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<IResponse>() { // from class: com.xiaopeng.autoshow.dialog.VerityDialog.3
            @Override // io.reactivex.Observer
            public void onComplete() {
            }

            @Override // io.reactivex.Observer
            public void onSubscribe(Disposable disposable) {
            }

            @Override // io.reactivex.Observer
            public void onNext(IResponse iResponse) {
                if (iResponse == null) {
                    if (VerityDialog.this.mCallback != null) {
                        VerityDialog.this.mCallback.onFail();
                    }
                    VerityDialog.this.isPassed = false;
                    XToast.showShort("验证失败");
                    LogUtils.d(VerityDialog.TAG, "验证失败");
                    return;
                }
                String body = iResponse.body();
                if (TextUtils.isEmpty(body)) {
                    return;
                }
                JsonObject asJsonObject = new JsonParser().parse(body).getAsJsonObject();
                int asInt = asJsonObject.get(Config.CODE_KEY).getAsInt();
                String asString = asJsonObject.get("msg").getAsString();
                if (asInt != 200) {
                    VerityDialog.this.isPassed = false;
                    if (VerityDialog.this.mCallback != null) {
                        VerityDialog.this.mCallback.onFail();
                    }
                    XToast.showShort("验证失败:" + asString);
                    LogUtils.d(VerityDialog.TAG, "验证失败--->" + asString);
                    return;
                }
                LogUtils.d(VerityDialog.TAG, "验证成功--->" + asString);
                VerityDialog.this.isPassed = true;
                if (VerityDialog.this.mCallback != null) {
                    VerityDialog.this.mCallback.onSuccess(str);
                }
                VerityDialog.this.dismiss();
            }

            @Override // io.reactivex.Observer
            public void onError(Throwable th) {
                th.printStackTrace();
                LogUtils.d(VerityDialog.TAG, "onError");
            }
        });
    }

    private void clearTimer() {
        this.mInputNumber.setClickable(true);
        this.mVerityNumber.setEnabled(true);
        this.mTvSendCode.setText(R.string.send_vertify_code);
        CodeCountDownTimer codeCountDownTimer = this.mCodeCountDownTimer;
        if (codeCountDownTimer != null) {
            codeCountDownTimer.cancel();
            this.mCodeCountDownTimer = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IResponse doCheckVerifyCodeSync(String str, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put(Config.CODE_KEY, str2);
        hashMap.put(SSUtils.O0000Ooo, SystemPropertyUtil.getVIN());
        hashMap.put("mobile", str);
        IRequest build = getHttp().bizHelper().post(Config.CHECK_CODE, new Gson().toJson(hashMap)).needAuthorizationInfo().enableSecurityEncoding().build();
        try {
            LogUtils.d(TAG, "doPostSync uri:" + Config.CHECK_CODE + ",params:" + hashMap + ",time:" + System.currentTimeMillis());
            return build.execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendVerifyCode(final String str) {
        Observable.create(new ObservableOnSubscribe<IResponse>() { // from class: com.xiaopeng.autoshow.dialog.VerityDialog.6
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(ObservableEmitter<IResponse> observableEmitter) throws Exception {
                observableEmitter.onNext(VerityDialog.this.doSendVerifyCodeSync(str));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<IResponse>() { // from class: com.xiaopeng.autoshow.dialog.VerityDialog.5
            @Override // io.reactivex.Observer
            public void onComplete() {
            }

            @Override // io.reactivex.Observer
            public void onSubscribe(Disposable disposable) {
            }

            @Override // io.reactivex.Observer
            public void onNext(IResponse iResponse) {
                if (iResponse == null) {
                    VerityDialog.this.sendVerifyCodeError(AutoShowApp.getInstance().getString(R.string.send_code_error));
                    return;
                }
                String body = iResponse.body();
                if (TextUtils.isEmpty(body)) {
                    return;
                }
                JsonObject asJsonObject = new JsonParser().parse(body).getAsJsonObject();
                int asInt = asJsonObject.get(Config.CODE_KEY).getAsInt();
                String asString = asJsonObject.get("msg").getAsString();
                if (asInt != 200) {
                    VerityDialog.this.sendVerifyCodeError(asString);
                } else {
                    XToast.showShort((int) R.string.get_code_success);
                }
            }

            @Override // io.reactivex.Observer
            public void onError(Throwable th) {
                th.printStackTrace();
                LogUtils.d(VerityDialog.TAG, "onError");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendVerifyCodeError(String str) {
        if (!TextUtils.isEmpty(str)) {
            XToast.showShort(str);
        }
        if (this.isCountDown) {
            onFinish();
            CodeCountDownTimer codeCountDownTimer = this.mCodeCountDownTimer;
            if (codeCountDownTimer != null) {
                codeCountDownTimer.cancel();
                this.mCodeCountDownTimer = null;
            }
        }
    }

    private IHttp getHttp() {
        IHttp iHttp = (IHttp) Module.get(NetworkChannelsEntry.class).get(IHttp.class);
        iHttp.config().applicationContext(AutoShowApp.getInstance()).addInterceptor(new TrafficeStaFlagInterceptor()).apply();
        return iHttp;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IResponse doSendVerifyCodeSync(String str) {
        IHttp http = getHttp();
        HashMap hashMap = new HashMap();
        hashMap.put(SSUtils.O0000Ooo, SystemPropertyUtil.getVIN());
        hashMap.put("mobile", str);
        IRequest build = http.bizHelper().post(Config.GET_CODE, new Gson().toJson(hashMap)).needAuthorizationInfo().enableSecurityEncoding().build();
        try {
            LogUtils.d(TAG, "doPostSync uri:" + Config.GET_CODE + ",params:" + hashMap + ",time:" + System.currentTimeMillis());
            return build.execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setSendCodeListener() {
        this.mTvSendCode.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.autoshow.dialog.VerityDialog.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (NetUtils.isNetworkAvailable(AutoShowApp.getInstance())) {
                    String trim = VerityDialog.this.mInputNumber.getText().trim();
                    VerityDialog verityDialog = VerityDialog.this;
                    verityDialog.mCodeCountDownTimer = new CodeCountDownTimer(60000L, 1000L, verityDialog);
                    VerityDialog.this.isCountDown = true;
                    VerityDialog.this.mCodeCountDownTimer.start();
                    VerityDialog.this.sendVerifyCode(trim);
                    return;
                }
                XToast.showShort((int) R.string.net_error);
            }
        });
    }

    private void setInputVerifyListener() {
        this.mVerityNumber.addTextChangedListener(new TextWatcher() { // from class: com.xiaopeng.autoshow.dialog.VerityDialog.8
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                VerityDialog verityDialog = VerityDialog.this;
                verityDialog.setSubBtnEnable(verityDialog.mInputNumber.getText(), VerityDialog.this.mVerityNumber.getText());
            }
        });
    }

    private void setInputNumberListener() {
        this.mInputNumber.addTextChangedListener(new TextWatcher() { // from class: com.xiaopeng.autoshow.dialog.VerityDialog.9
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                VerityDialog verityDialog = VerityDialog.this;
                verityDialog.setSubBtnEnable(verityDialog.mInputNumber.getText(), VerityDialog.this.mVerityNumber.getText());
                String obj = editable.toString();
                if (TextUtils.isEmpty(obj)) {
                    VerityDialog.this.mTvSendCode.setEnabled(false);
                    VerityDialog.this.mInputNumber.setErrorEnable(false);
                } else if (!obj.matches("(13|14|15|16|17|18|19)[0-9]{9}")) {
                    VerityDialog.this.mTvSendCode.setEnabled(false);
                    VerityDialog.this.mInputNumber.setErrorEnable(true);
                    VerityDialog.this.mInputNumber.setErrorMsg(AutoShowApp.getInstance().getString(R.string.invalid_phone_format));
                } else {
                    VerityDialog.this.mInputNumber.setErrorEnable(false);
                    if (VerityDialog.this.isCountDown) {
                        return;
                    }
                    VerityDialog.this.mTvSendCode.setEnabled(true);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSubBtnEnable(String str, String str2) {
        if (str.matches("(13|14|15|16|17|18|19)[0-9]{9}") && str2.length() > 3) {
            this.mBtn.setEnabled(true);
        } else {
            this.mBtn.setEnabled(false);
        }
    }

    private void initDialog() {
        ((Window) Objects.requireNonNull(getWindow())).setType(XDialogSystemType.TYPE_SYSTEM_DIALOG);
        this.mInputNumber = (XTextFields) findViewById(R.id.input_phone_number);
        this.mVerityNumber = (XTextFields) findViewById(R.id.input_vertify_code);
        this.mTvSendCode = (XButton) findViewById(R.id.send_vertify);
        this.mBtn = (XButton) findViewById(R.id.btn);
        this.mBtnClose = (XButton) findViewById(R.id.btn_close);
        this.mPmBroadcastReceiver = new PmBroadcastReceiver();
        getContext().registerReceiver(this.mPmBroadcastReceiver, new IntentFilter("com.xiaopeng.broadcast.ACTION_PM_STATUS_CHANGE"));
        setOnDismissListener(this);
    }

    @Override // android.app.Dialog
    public void show() {
        getContext().registerComponentCallbacks(this);
        super.show();
    }

    @Override // com.xiaopeng.autoshow.ITimer
    public void onFinish() {
        this.isCountDown = false;
        this.mTvSendCode.setText(AutoShowApp.getInstance().getString(R.string.get_code_again));
        this.mTvSendCode.setClickable(true);
        this.mTvSendCode.setEnabled(true);
    }

    @Override // com.xiaopeng.autoshow.ITimer
    public void onTick(long j) {
        this.mTvSendCode.setClickable(false);
        this.mTvSendCode.setEnabled(false);
        this.mTvSendCode.setText(String.format("%d秒后重新获取", Long.valueOf(j / 1000)));
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
    }

    @Override // android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.x_bg_dialog);
        }
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        IVerityCallback iVerityCallback;
        if (!this.isPassed && (iVerityCallback = this.mCallback) != null) {
            iVerityCallback.onFail();
        }
        this.mCallback = null;
        clearTimer();
        if (this.mPmBroadcastReceiver != null) {
            getContext().unregisterReceiver(this.mPmBroadcastReceiver);
            getContext().unregisterComponentCallbacks(this);
            this.mPmBroadcastReceiver = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class PmBroadcastReceiver extends BroadcastReceiver {
        private PmBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra("status", -1) == 1 && VerityDialog.this.isShowing()) {
                VerityDialog.this.dismiss();
                LogUtils.d(VerityDialog.TAG, "收到下电广播，dimiss dialog");
            }
        }
    }
}
