package com.android.ledgerbook.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Business implements Parcelable {
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public Business() {
    }

    protected Business(Parcel in) {
    }

    public static final Parcelable.Creator<Business> CREATOR = new Parcelable.Creator<Business>() {
        @Override
        public Business createFromParcel(Parcel source) {
            return new Business(source);
        }

        @Override
        public Business[] newArray(int size) {
            return new Business[size];
        }
    };
}
