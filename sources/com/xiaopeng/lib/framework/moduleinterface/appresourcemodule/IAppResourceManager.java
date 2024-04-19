package com.xiaopeng.lib.framework.moduleinterface.appresourcemodule;

import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import io.reactivex.Observable;
/* loaded from: classes.dex */
public interface IAppResourceManager {

    /* loaded from: classes.dex */
    public interface IResourceObserver {
        void onChange(String uriPath, AppResourceResponse response);
    }

    boolean addResource(AppResourceRequest request);

    Observable<AppResourceResponse> addResourceObSource(AppResourceRequest request);

    void checkUpdate(String uriPath);

    Observable<AppResourceResponse> checkUpdateObSource(String uriPath);

    Observable<AppResourceResponse> clearResourceObSource();

    void clearResources();

    boolean deleteResource(String uriPath);

    Observable<AppResourceResponse> deleteResourceObSource(String uriPath);

    AssetFileDescriptor getFileDescriptor(String uriPath);

    Observable<AssetFileDescriptor> getFileDescriptorObSource(String uriPath);

    Uri getFullUri(String uriPath);

    Observable<Uri> getFullUrlObSource(String uriPath);

    Status getStatus(String uriPath);

    Observable<Status> getStatusObSource(String uriPath);

    void subscribeChanges(IResourceObserver observer);

    void unSubscribeChanges(IResourceObserver observer);

    /* loaded from: classes.dex */
    public enum UpdatePolicy {
        LOCAL(0),
        SYNC(1),
        ASYNC(2);
        
        final int mId;

        public int id() {
            return this.mId;
        }

        UpdatePolicy(int id) {
            this.mId = id;
        }
    }

    /* loaded from: classes.dex */
    public enum Status {
        NOT_REGISTER(0),
        NOT_AUTH(1),
        NOT_AVAILABLE(2),
        OK(3),
        ERROR(4);
        
        final int mId;

        public int id() {
            return this.mId;
        }

        Status(int id) {
            this.mId = id;
        }
    }
}
