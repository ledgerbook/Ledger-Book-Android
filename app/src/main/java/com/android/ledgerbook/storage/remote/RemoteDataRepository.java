package com.android.ledgerbook.storage.remote;

import com.android.ledgerbook.models.CreateBookRequest;
import com.android.ledgerbook.models.CreateBookResponse;
import com.android.ledgerbook.models.GenericResponse;
import com.android.ledgerbook.models.LoginRequest;
import com.android.ledgerbook.models.OtpRequest;
import com.android.ledgerbook.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RemoteDataRepository {
    @GET("/users/profile")
    Call<User> getUser();

    @POST("/users/otp")
    Call<GenericResponse> sendOtp(@Body OtpRequest request);

    @POST("/books")
    Call<CreateBookResponse> createBook(@Body CreateBookRequest request);

    @POST("/users")
    Call<User> login(@Body LoginRequest request);
}
