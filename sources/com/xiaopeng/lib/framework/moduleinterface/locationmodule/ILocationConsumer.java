package com.xiaopeng.lib.framework.moduleinterface.locationmodule;

import android.content.Context;
import com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation;
/* loaded from: classes.dex */
public interface ILocationConsumer {
    boolean connected();

    ILocation getLocation() throws ILocationServiceException;

    void init(Context context) throws ILocationServiceException;

    void init(Context context, String remoteAppPackage) throws ILocationServiceException;

    void subscribe(ILocation.Category category) throws ILocationServiceException;

    void unsubscribe() throws ILocationServiceException;
}
