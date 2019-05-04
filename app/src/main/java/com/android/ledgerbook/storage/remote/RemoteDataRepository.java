package com.android.ledgerbook.storage.remote;

import com.android.ledgerbook.models.User;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RemoteDataRepository {
    @GET("/users/profile")
    Call<User> getUser();
}
