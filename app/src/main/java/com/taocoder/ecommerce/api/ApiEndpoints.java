package com.taocoder.ecommerce.api;

import com.taocoder.ecommerce.models.ServerResponse;
import com.taocoder.ecommerce.models.User;
import com.taocoder.ecommerce.utils.Constants;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiEndpoints {

    @Headers({
            "APIKEY: " + Constants.CLIENT_KEY,
            "Content-Type: application/json"
    })
    @POST("/authuser/login")
    Call<ServerResponse<User>> login(@Body Map<String, String> body);

    @Headers({
            "APIKEY: " + Constants.CLIENT_KEY,
            "Content-Type: application/json"
    })
    @POST("/authuser/register")
    Call<ServerResponse<User>> register(@Body Map<String, String> body);

    @Headers({
            "APIKEY: " + Constants.CLIENT_KEY,
            "Content-Type: application/json"
    })
    @POST("/authuser/resendotp")
    Call<ServerResponse<User>> resendOTP(@Body Map<String, String> body);

    @Headers({
            "APIKEY: " + Constants.CLIENT_KEY,
            "Content-Type: application/json"
    })
    @POST("/authuser/otpverify-login")
    Call<ServerResponse<User>> verifyOTP(@Body Map<String, String> body);
}