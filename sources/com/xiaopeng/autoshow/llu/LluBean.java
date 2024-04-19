package com.xiaopeng.autoshow.llu;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class LluBean implements Parcelable {
    public static final Parcelable.Creator<LluBean> CREATOR = new Parcelable.Creator<LluBean>() { // from class: com.xiaopeng.autoshow.llu.LluBean.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LluBean createFromParcel(Parcel parcel) {
            return new LluBean(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LluBean[] newArray(int i) {
            return new LluBean[i];
        }
    };
    @SerializedName("effect_name")
    private String mExpoModeName;
    @SerializedName("loop_count")
    private int mLoopCount;
    private int mOrder;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public LluBean() {
    }

    protected LluBean(Parcel parcel) {
        this.mExpoModeName = parcel.readStringNoHelper();
        this.mOrder = parcel.readInt();
        this.mLoopCount = parcel.readInt();
    }

    public String getExpoModeName() {
        return this.mExpoModeName;
    }

    public void setExpoModeName(String str) {
        this.mExpoModeName = str;
    }

    public int getOrder() {
        return this.mOrder;
    }

    public void setOrder(int i) {
        this.mOrder = i;
    }

    public int getLoopCount() {
        return this.mLoopCount;
    }

    public void setLoopCount(int i) {
        this.mLoopCount = i;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringNoHelper(this.mExpoModeName);
        parcel.writeInt(this.mOrder);
        parcel.writeInt(this.mLoopCount);
    }
}
