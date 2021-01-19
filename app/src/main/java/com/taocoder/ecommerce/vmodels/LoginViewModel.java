package com.taocoder.ecommerce.vmodels;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.taocoder.ecommerce.R;
import com.taocoder.ecommerce.models.LoginFormState;
import com.taocoder.ecommerce.models.Result;
import com.taocoder.ecommerce.models.ServerResponse;
import com.taocoder.ecommerce.models.User;
import com.taocoder.ecommerce.repositories.LoginRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<ServerResponse<User>> loginResult;
    private MutableLiveData<LoginFormState> loginFormState;
    private MutableLiveData<ServerResponse<User>> verificationResult;
    private MutableLiveData<ServerResponse<User>> resendResult;

    private LoginRepository loginRepository;

    public LoginViewModel() {
        this.loginResult = new MutableLiveData<>();
        this.loginFormState = new MutableLiveData<>();
        this.verificationResult = new MutableLiveData<>();
        this.resendResult = new MutableLiveData<>();
        this.loginRepository = new LoginRepository();
    }

    public MutableLiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<ServerResponse<User>> getLoginResult() {
        return loginResult;
    }

    public MutableLiveData<ServerResponse<User>> getResendResult() {
        return resendResult;
    }

    public MutableLiveData<ServerResponse<User>> getVerificationResult() {
        return verificationResult;
    }

    public void login(String username, String password) {

        // can be launched in a separate asynchronous job
        loginRepository.login(username, password).enqueue(new Callback<ServerResponse<User>>() {
            @Override
            public void onResponse(Call<ServerResponse<User>> call, Response<ServerResponse<User>> response) {
                if (response.isSuccessful()) {
                    loginResult.setValue(response.body());
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

    public void verifyOTP(String otp, String phone) {
        Map<String, String> map = new HashMap<>();
        map.put("otp", otp);
        map.put("mobile", phone);

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

    public void loginDataChanged(String phone, String password) {
        if (phone.isEmpty()) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (password.isEmpty()) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}