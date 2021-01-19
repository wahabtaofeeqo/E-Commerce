package com.taocoder.ecommerce.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.taocoder.ecommerce.OnFragmentChangeListener;
import com.taocoder.ecommerce.R;
import com.taocoder.ecommerce.activities.MainActivity;
import com.taocoder.ecommerce.data.SessionManager;
import com.taocoder.ecommerce.models.ServerResponse;
import com.taocoder.ecommerce.models.User;
import com.taocoder.ecommerce.vmodels.RegisterViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterFragment extends Fragment implements View.OnClickListener, Validator.ValidationListener {

    @NotEmpty
    private TextInputEditText name;

    @NotEmpty
    private TextInputEditText mobile;

    @NotEmpty
    private TextInputEditText email;

    @NotEmpty
    private TextInputEditText dob;

    @NotEmpty
    private TextInputEditText docId;

    @NotEmpty
    private TextInputEditText docType;

    private TextInputEditText otp;

    private ProgressBar progressBar;
    private Validator validator;

    private LinearLayout basicData;
    private LinearLayout idData;

    private OnFragmentChangeListener listener;
    private RegisterViewModel registerViewModel;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerViewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);

        final MaterialButton next = view.findViewById(R.id.next);
        final MaterialButton prev = view.findViewById(R.id.prev);
        final MaterialButton login = view.findViewById(R.id.login);
        final MaterialButton register = view.findViewById(R.id.register);
        final MaterialButton verify = view.findViewById(R.id.veriy);
        final MaterialButton resend = view.findViewById(R.id.resend);

        basicData = view.findViewById(R.id.basicData);
        idData = view.findViewById(R.id.idData);
        final LinearLayout otpForm = view.findViewById(R.id.otpForm);

        name = view.findViewById(R.id.name);
        mobile = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        dob = view.findViewById(R.id.dob);
        docId = view.findViewById(R.id.docId);
        docType = view.findViewById(R.id.docType);
        otp = view.findViewById(R.id.otp);

        progressBar = view.findViewById(R.id.loading);

        prev.setOnClickListener(this);
        next.setOnClickListener(this);
        login.setOnClickListener(this);
        verify.setOnClickListener(this);
        resend.setOnClickListener(this);
        register.setOnClickListener(this);

        registerViewModel.getRegisterResult().observe(requireActivity(), new Observer<ServerResponse<User>>() {
            @Override
            public void onChanged(ServerResponse<User> response) {
                if (response == null)
                    return;

                progressBar.setVisibility(View.GONE);
                if (response.getStatus().equalsIgnoreCase("success")) {
                    idData.setVisibility(View.GONE);
                    basicData.setVisibility(View.GONE);
                    otpForm.setVisibility(View.VISIBLE);
                }

                showMessage(response.getMessage());
            }
        });

        registerViewModel.getVerificationResult().observe(requireActivity(), new Observer<ServerResponse<User>>() {
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
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentChangeListener)
            listener = (OnFragmentChangeListener) getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                basicData.setVisibility(View.GONE);
                idData.setVisibility(View.VISIBLE);
                break;

            case R.id.login:
                listener.onFragmentChange(new LoginFragment());
                break;

            case R.id.prev:
                idData.setVisibility(View.GONE);
                basicData.setVisibility(View.VISIBLE);
                break;

            case R.id.register:
                validator.validate();
                break;

            case R.id.veriy:
                String code = otp.getText().toString();
                if (!code.isEmpty())
                    registerViewModel.verifyOTP(code, mobile.getText().toString());
                break;

            case R.id.resend:
                registerViewModel.resendOTP(mobile.getText().toString());
        }
    }

    @Override
    public void onValidationSucceeded() {
        Map<String, String> map = new HashMap<>();
        map.put("name", name.getText().toString());
        map.put("mobile", mobile.getText().toString());
        map.put("email", mobile.getText().toString());
        map.put("dob", dob.getText().toString());
        map.put("docid", docId.getText().toString());
        map.put("doctype", docType.getText().toString());

        progressBar.setVisibility(View.VISIBLE);
        registerViewModel.register(map);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        showMessage("Validation Error.");
    }

    private void showMessage(String message) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    message,
                    Toast.LENGTH_LONG).show();
        }
    }
}