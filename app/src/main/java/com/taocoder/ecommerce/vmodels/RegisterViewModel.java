package com.taocoder.ecommerce.vmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.taocoder.ecommerce.models.ServerResponse;
import com.taocoder.ecommerce.models.User;
import com.taocoder.ecommerce.repositories.LoginRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {

    private LoginRepository loginRepository;
    MutableLiveData<ServerResponse<User>> registerResult;
    private MutableLiveData<ServerResponse<User>> verificationResult;
    private MutableLiveData<ServerResponse<User>> resendResult;


    public RegisterViewModel() {
        registerResult = new MutableLiveData<>();
        this.verificationResult = new MutableLiveData<>();
        this.resendResult = new MutableLiveData<>();
        loginRepository = new LoginRepository();
    }

    public MutableLiveData<ServerResponse<User>> getRegisterResult() {
        return registerResult;
    }

    public MutableLiveData<ServerResponse<User>> getVerificationResult() {
        return verificationResult;
    }

    public MutableLiveData<ServerResponse<User>> getResendResult() {
        return resendResult;
    }

    public void register(Map<String, String> map) {
        loginRepository.register(map).enqueue(new Callback<ServerResponse<User>>() {
            @Override
            public void onResponse(Call<ServerResponse<User>> call, Response<ServerResponse<User>> response) {
                if (response.isSuccessful()) {
                    registerResult.setValue(response.body());
                }
                else {
                    try {
                        Log.i("ERROR LOG", response.errorBody().string());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<User>> call, Throwable t) {

            }
        });
    }

    public void verifyOTP(String code, String mobile) {
        Map<String, String> map = new HashMap<>();
        map.put("otp", code);
        map.put("mobile", mobile);

        loginRepository.verifyOTP(map).enqueue(new Callback<ServerResponse<User>>() {
            @Override
            public void onResponse(Call<ServerResponse<User>> call, Response<ServerResponse<User>> response) {
                if (response.isSuccessful()) {
                    verificationResult.setValue(response.body());
                }
                else {
                    try {
                        Log.i("ERROR LOG", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<User>> call, Throwable t) {

            }
        });
    }

    public void resendOTP(String phone) {

        Map<String, String> map = new HashMap<>();
        map.put("mobile", phone);
        loginRepository.resendOTP(map).enqueue(new Callback<ServerResponse<User>>() {
            @Override
            public void onResponse(Call<ServerResponse<User>> call, Response<ServerResponse<User>> response) {
                if (response.isSuccessful()) {
                    resendResult.setValue(response.body());
                }
                else {
                    try {
                        Log.i("ERROR LOG", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
