package com.taocoder.ecommerce.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.taocoder.ecommerce.OnFragmentChangeListener;
import com.taocoder.ecommerce.R;
import com.taocoder.ecommerce.activities.MainActivity;
import com.taocoder.ecommerce.data.SessionManager;
import com.taocoder.ecommerce.models.LoginFormState;
import com.taocoder.ecommerce.models.ServerResponse;
import com.taocoder.ecommerce.models.User;
import com.taocoder.ecommerce.vmodels.LoginViewModel;

import java.util.List;

public class LoginFragment extends Fragment implements Validator.ValidationListener, View.OnClickListener {

    private LoginViewModel loginViewModel;

    @NotEmpty
    private TextInputEditText phone;

    @NotEmpty
    @Password(scheme = Password.Scheme.ANY)
    private TextInputEditText password;

    private ProgressBar progressBar;
    private MaterialButton register;
    Validator validator;

    private TextInputEditText otp;

    private OnFragmentChangeListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        phone = view.findViewById(R.id.phone);
        password = view.findViewById(R.id.password);
        otp = view.findViewById(R.id.otp);
        register = view.findViewById(R.id.register);
        final Button loginButton = view.findViewById(R.id.login);
        final MaterialButton resendButton = view.findViewById(R.id.resend);
        final MaterialButton verify = view.findViewById(R.id.veriy);
        final LinearLayout loginForm = view.findViewById(R.id.loginForm);
        final LinearLayout otpForm = view.findViewById(R.id.otpForm);
        final MaterialButton forgot = view.findViewById(R.id.forgotPassword);
        progressBar = view.findViewById(R.id.loading);

        verify.setOnClickListener(this);
        register.setOnClickListener(this);
        resendButton.setOnClickListener(this);

        loginViewModel.getLoginFormState().observe(requireActivity(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    phone.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    password.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(requireActivity(), new Observer<ServerResponse<User>>() {
            @Override
            public void onChanged(ServerResponse<User> response) {
                if (response == null) {
                    return;
                }

                progressBar.setVisibility(View.GONE);

                if (!response.getStatus().equalsIgnoreCase("failed")) {
                    loginForm.setVisibility(View.GONE);
                    otpForm.setVisibility(View.VISIBLE);
                }

                showMessage(response.getMessage());
            }
        });

        loginViewModel.getVerificationResult().observe(requireActivity(), new Observer<ServerResponse<User>>() {
            @Override
            public void onChanged(ServerResponse<User> response) {

                if (response == null) {
                    return;
                }

                progressBar.setVisibility(View.GONE);

                if (!response.getStatus().equalsIgnoreCase("failed")) {
                    SessionManager sessionManager = SessionManager.getInstance(getContext());
                    sessionManager.setToken(response.getToken());
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
                }

                showMessage(response.getMessage());
            }
        });

        loginViewModel.getResendResult().observe(requireActivity(), new Observer<ServerResponse<User>>() {
            @Override
            public void onChanged(ServerResponse<User> response) {

                if (response == null) {
                    return;
                }

                progressBar.setVisibility(View.GONE);

                showMessage(response.getMessage());
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(phone.getText().toString(),
                        password.getText().toString());
            }
        };

        phone.addTextChangedListener(afterTextChangedListener);
        password.addTextChangedListener(afterTextChangedListener);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(phone.getText().toString(),
                            password.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentChangeListener)
            listener = (OnFragmentChangeListener) getActivity();
    }

    private void showMessage(String message) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    message,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onValidationSucceeded() {
        progressBar.setVisibility(View.VISIBLE);
        loginViewModel.login(phone.getText().toString(), password.getText().toString());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        showMessage("Provide valid Login Data");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.resend:
                progressBar.setVisibility(View.VISIBLE);
                loginViewModel.resendOTP(phone.getText().toString());
                break;

            case R.id.veriy:
                String code = otp.getText().toString();
                if (!code.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    loginViewModel.verifyOTP(code, phone.getText().toString());
                }
                break;

            case R.id.register:
                listener.onFragmentChange(new RegisterFragment());
        }
    }
}