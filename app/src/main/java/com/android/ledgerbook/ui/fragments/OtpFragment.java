package com.android.ledgerbook.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ledgerbook.R;
import com.android.ledgerbook.models.LoginRequest;
import com.android.ledgerbook.models.User;
import com.android.ledgerbook.ui.BaseFragment;
import com.android.ledgerbook.utils.ApiError;
import com.android.ledgerbook.utils.ApiSuccess;
import com.mukesh.OtpView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class OtpFragment extends BaseFragment<OtpFragment.OtpActionListener> {
    private static final String KEY_PHONE = "phone";

    @BindView(R.id.txtTitle) TextView txtTitle;
    @BindView(R.id.viewOtp) OtpView viewOtp;
    @BindView(R.id.btnVerifyPhone) Button btnVerifyPhone;

    private String phone;

    public static OtpFragment newInstance(String phone) {
        Bundle args = new Bundle();
        args.putString(KEY_PHONE, phone);
        OtpFragment fragment = new OtpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phone = getArguments().getString(KEY_PHONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp, container, true);
        bindView(view);
        txtTitle.setText(getString(R.string.auto_verify_otp, phone));
        return view;
    }

    @OnTextChanged(value = {R.id.viewOtp}, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onTextChanged(Editable editable) {
        String otp = editable.toString();
        btnVerifyPhone.setEnabled(otp.length() == 4);
    }

    @OnClick(R.id.btnVerifyPhone)
    void onClick(View view) {
        LoginRequest request = new LoginRequest(phone, viewOtp.getText().toString());
        registerExecuteUseCaseWithActivityProgress(getUseCaseManager().login(request),
                this::onLoginSuccess, this::onLoginFailed);
    }

    @ApiSuccess
    private void onLoginSuccess(User user) {
        fragmentActionListener.onOtpLoginSuccess();
    }

    @ApiError
    private void onLoginFailed(LoginRequest error) {
        Toast.makeText(getContext(), error.getOtp(), Toast.LENGTH_SHORT).show();
    }

    public interface OtpActionListener {
        void onOtpLoginSuccess();
    }
}
