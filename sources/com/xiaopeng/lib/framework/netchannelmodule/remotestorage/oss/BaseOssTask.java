package com.xiaopeng.lib.framework.netchannelmodule.remotestorage.oss;

import android.app.Application;
import android.text.TextUtils;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.remotestorage.Callback;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.remotestorage.StorageException;
import com.xiaopeng.lib.framework.netchannelmodule.R;
import com.xiaopeng.lib.framework.netchannelmodule.common.util.EncryptionUtil;
import com.xiaopeng.lib.framework.netchannelmodule.remotestorage.exception.StorageExceptionImpl;
import com.xiaopeng.lib.framework.netchannelmodule.remotestorage.statistic.StorageCounter;
import com.xiaopeng.lib.framework.netchannelmodule.remotestorage.token.TokenRetriever;
import com.xiaopeng.lib.security.xmartv1.XmartV1Constants;
import com.xiaopeng.lib.utils.LogUtils;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import org.apache.commons.compress.archivers.tar.TarConstants;
/* loaded from: classes.dex */
public abstract class BaseOssTask implements Runnable {
    private static final int CONNECTION_TIMEOUT = 60000;
    private static final int ERROR_FORBIDDEN = 403;
    private static final int MAX_CONCURRENT_REQUEST = 2;
    private static final int MAX_ERROR_RETRY = 3;
    private static final int SOCKET_TIMEOUT = 60000;
    private static final String TAG = "NetChannel-BaseOssTask";
    private static final String XIAOPENG_PRIVATE_DOMAIN = "oss.xiaopeng.com";
    private static OSS sOssClient;
    private Application mApplication;
    private final Bucket mBucket;
    private Callback mCallback;
    protected Map<String, String> mCallbackParams;
    protected boolean mCertified;
    protected String mLocalFilePath;
    protected long mLocalFileSize;
    private String mModuleName;
    private String mRemoteFolder;
    protected String mRemoteObjectKey;
    protected String mRemoteUrl;
    private static final byte[] KEY = {123, TarConstants.LF_FIFO, 90, 56, 23, 74, 89, 71, TarConstants.LF_CONTIG, 78, 65, 40, 100};
    private static final byte[] ENCRYPT_PASSWORD = {3, 95, 59, 87, TarConstants.LF_PAX_GLOBAL_EXTENDED_HEADER, 47, TarConstants.LF_CONTIG, 32, TarConstants.LF_PAX_EXTENDED_HEADER_UC, 61, TarConstants.LF_SYMLINK};

    abstract void performRealTask();

    public BaseOssTask(Bucket bucket) {
        this.mBucket = bucket;
    }

    public BaseOssTask application(Application application) {
        this.mApplication = application;
        return this;
    }

    public BaseOssTask module(String str) throws IllegalArgumentException {
        if (!TextUtils.isEmpty(this.mRemoteFolder)) {
            throw new IllegalArgumentException("Remote folder has been assigned.");
        }
        this.mModuleName = str;
        return this;
    }

    public BaseOssTask remoteFolder(String str) throws IllegalArgumentException {
        if (!TextUtils.isEmpty(this.mModuleName)) {
            throw new IllegalArgumentException("Module name has been assigned.");
        }
        this.mRemoteFolder = str;
        return this;
    }

    public BaseOssTask filePath(String str) {
        this.mLocalFilePath = str;
        return this;
    }

    public BaseOssTask callback(Callback callback) {
        this.mCallback = callback;
        return this;
    }

    public BaseOssTask remoteCallbackParams(Map<String, String> map) {
        this.mCallbackParams = map;
        return this;
    }

    public BaseOssTask needCertified(boolean z) {
        this.mCertified = z;
        return this;
    }

    public BaseOssTask build() throws StorageException {
        StringBuilder sb;
        String url;
        if (TextUtils.isEmpty(this.mRemoteFolder) && TextUtils.isEmpty(this.mModuleName)) {
            throw new StorageExceptionImpl(3);
        }
        String str = this.mRemoteFolder;
        if (str == null) {
            this.mRemoteObjectKey = this.mBucket.generateObjectKey(this.mModuleName);
        } else {
            this.mRemoteObjectKey = str;
        }
        if (this.mCertified) {
            sb = new StringBuilder();
            url = this.mBucket.getPrivateUrl();
        } else {
            sb = new StringBuilder();
            url = this.mBucket.getUrl();
        }
        sb.append(url);
        sb.append(this.mRemoteObjectKey);
        this.mRemoteUrl = sb.toString();
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void doSuccess() {
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onSuccess(this.mRemoteUrl, this.mLocalFilePath);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void doFailure(StorageException storageException) {
        LogUtils.d(TAG, "Failed! Reason is-->" + storageException.getMessage());
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onFailure(this.mRemoteUrl, this.mLocalFilePath, storageException);
        }
        if (storageException.getReasonCode() == 403) {
            TokenRetriever.getInstance().clearToken();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String bucketRootName() {
        return this.mBucket.getRootName();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long bucketMaxObjectSize() {
        return this.mBucket.getMaxObjectSize();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized OSS createOssClient(String str, String str2, String str3) {
        OSSStsTokenCredentialProvider oSSStsTokenCredentialProvider = new OSSStsTokenCredentialProvider(str, str2, str3);
        if (sOssClient == null) {
            ClientConfiguration clientConfiguration = new ClientConfiguration();
            clientConfiguration.setConnectionTimeout(60000);
            clientConfiguration.setSocketTimeout(60000);
            clientConfiguration.setMaxConcurrentRequest(2);
            clientConfiguration.setMaxErrorRetry(3);
            try {
                try {
                    try {
                        try {
                            if (this.mCertified) {
                                clientConfiguration.setOkHttpClient(getCustomOkHttpClient());
                                sOssClient = new OSSClient(this.mApplication, this.mBucket.getPrivateUrl(), oSSStsTokenCredentialProvider, clientConfiguration);
                            } else {
                                sOssClient = new OSSClient(this.mApplication, Bucket.END_POINT, oSSStsTokenCredentialProvider, clientConfiguration);
                            }
                        } catch (NoSuchAlgorithmException e) {
                            LogUtils.e(TAG, "createOssClient failed, NoSuchAlgorithmException:" + e.getMessage());
                        }
                    } catch (KeyManagementException e2) {
                        LogUtils.e(TAG, "createOssClient failed, KeyManagementException:" + e2.getMessage());
                    }
                } catch (KeyStoreException e3) {
                    LogUtils.e(TAG, "createOssClient failed, KeyStoreException:" + e3.getMessage());
                } catch (CertificateException e4) {
                    LogUtils.e(TAG, "createOssClient failed, CertificateException:" + e4.getMessage());
                }
            } catch (IOException e5) {
                LogUtils.e(TAG, "createOssClient failed, IOException:" + e5.getMessage());
            } catch (UnrecoverableKeyException e6) {
                LogUtils.e(TAG, "createOssClient failed, UnrecoverableKeyException:" + e6.getMessage());
            }
        } else {
            sOssClient.updateCredentialProvider(oSSStsTokenCredentialProvider);
        }
        return sOssClient;
    }

    private OkHttpClient getCustomOkHttpClient() throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException, UnrecoverableKeyException, KeyManagementException {
        String str = new String(EncryptionUtil.decrypt(ENCRYPT_PASSWORD, KEY), StandardCharsets.UTF_8);
        InputStream openRawResource = this.mApplication.getResources().openRawResource(R.raw.client);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(openRawResource, str.toCharArray());
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, str.toCharArray());
        Collection<? extends Certificate> generateCertificates = CertificateFactory.getInstance("X.509").generateCertificates(new BufferedInputStream(this.mApplication.getResources().openRawResource(R.raw.ca)));
        if (generateCertificates.isEmpty()) {
            throw new IllegalArgumentException("The CA certificates are empty");
        }
        KeyStore keyStore2 = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore2.load(null, null);
        int i = 0;
        for (Certificate certificate : generateCertificates) {
            keyStore2.setCertificateEntry(Integer.toString(i), certificate);
            i++;
        }
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore2);
        SSLContext sSLContext = SSLContext.getInstance(XmartV1Constants.TLS_REVISION_1_2);
        sSLContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        return new OkHttpClient.Builder().sslSocketFactory(sSLContext.getSocketFactory(), (X509TrustManager) trustManagerFactory.getTrustManagers()[0]).hostnameVerifier(new HostnameVerifier() { // from class: com.xiaopeng.lib.framework.netchannelmodule.remotestorage.oss.BaseOssTask.1
            @Override // javax.net.ssl.HostnameVerifier
            public boolean verify(String str2, SSLSession sSLSession) {
                return !TextUtils.isEmpty(str2) && str2.endsWith(BaseOssTask.XIAOPENG_PRIVATE_DOMAIN);
            }
        }).build();
    }

    @Override // java.lang.Runnable
    public void run() {
        this.mCallback.onStart(this.mRemoteUrl, this.mLocalFilePath);
        performRealTask();
        StorageCounter.getInstance().increaseRequestCount();
    }
}
