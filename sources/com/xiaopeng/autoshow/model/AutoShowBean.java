package com.xiaopeng.autoshow.model;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class AutoShowBean implements Parcelable {
    public static final Parcelable.Creator<AutoShowBean> CREATOR = new Parcelable.Creator<AutoShowBean>() { // from class: com.xiaopeng.autoshow.model.AutoShowBean.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoShowBean createFromParcel(Parcel parcel) {
            return new AutoShowBean(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoShowBean[] newArray(int i) {
            return new AutoShowBean[i];
        }
    };
    private String mCarType;
    private String mShowName;
    private String mViewType;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    protected AutoShowBean(Parcel parcel) {
        this.mCarType = parcel.readStringNoHelper();
        this.mViewType = parcel.readStringNoHelper();
        this.mShowName = parcel.readStringNoHelper();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringNoHelper(this.mCarType);
        parcel.writeStringNoHelper(this.mViewType);
        parcel.writeStringNoHelper(this.mShowName);
    }
}
