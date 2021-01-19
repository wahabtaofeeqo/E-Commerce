package com.taocoder.ecommerce.repositories;


import com.taocoder.ecommerce.api.ApiEndpoints;
import com.taocoder.ecommerce.api.RetrofitClient;
import com.taocoder.ecommerce.models.ServerResponse;
import com.taocoder.ecommerce.models.User;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class LoginRepository {
    public Call<ServerResponse<User>> login(String phone, String password) {
        ApiEndpoints apiEndpoints = RetrofitClient.getInstance().create(ApiEndpoints.class);
        Map<String, String> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("password", password);
        return apiEndpoints.login(map);
    }

    public Call<ServerResponse<User>> register(Map<String, String> map) {
        ApiEndpoints apiEndpoints = RetrofitClient.getInstance().create(ApiEndpoints.class);
        return apiEndpoints.register(map);
    }

    public Call<ServerResponse<User>> resendOTP(Map<String, String> map) {
        ApiEndpoints apiEndpoints = RetrofitClient.getInstance().create(ApiEndpoints.class);
        return apiEndpoints.resendOTP(map);
    }

    public Call<ServerResponse<User>> verifyOTP(Map<String, String> map) {
        ApiEndpoints apiEndpoints = RetrofitClient.getInstance().create(ApiEndpoints.class);
        return apiEndpoints.verifyOTP(map);
    }
}