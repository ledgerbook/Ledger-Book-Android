package com.android.ledgerbook.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.ledgerbook.utils.Saveable;

import com.google.gson.annotations.SerializedName;

public class User extends BaseResponse implements Saveable, Parcelable {
    private static User instance;
    private static final String SAVEABLE_USER = "saveableUser";

    @SerializedName("id")
    private int id;

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static void resetInstance() {
        instance = null;
    }

    @Override
    public void saveInstanceState(Bundle bundle) {
        bundle.putParcelable(SAVEABLE_USER, instance);
    }

    @Override
    public void restoreInstanceState(Bundle bundle) {
        instance = bundle.getParcelable(SAVEABLE_USER);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public User() {
    }

    protected User(Parcel in) {
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
