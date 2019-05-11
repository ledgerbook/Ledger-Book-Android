package com.android.ledgerbook.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.ledgerbook.R;
import com.android.ledgerbook.models.GenericResponse;
import com.android.ledgerbook.models.OtpRequest;
import com.android.ledgerbook.ui.BaseFragment;
import com.android.ledgerbook.ui.widgets.CustomTextInput;
import com.android.ledgerbook.utils.ApiSuccess;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class PhoneFragment extends BaseFragment<PhoneFragment.PhoneActionListener> {

    @BindView(R.id.edtPhone) CustomTextInput edtPhone;
    @BindView(R.id.btnPhoneEntered) Button btnPhoneEntered;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone, container, false);
        bindView(view);
        return view;
    }

    @OnTextChanged(value = {R.id.edtPhone}, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onTextChanged(Editable editable) {
        btnPhoneEntered.setEnabled(edtPhone.isValid());
    }

    @OnClick(R.id.btnPhoneEntered)
    void onClick(View view) {
        OtpRequest request = new OtpRequest(getPhoneNumber());
        registerExecuteUseCaseWithActivityProgress(getUseCaseManager().sendOtp(request),
                this::onOtpSent, null);
    }

    private String getPhoneNumber() {
        return edtPhone.getPrefixLessText();
    }

    @ApiSuccess
    private void onOtpSent(GenericResponse response) {
        fragmentActionListener.onOtpSent(getPhoneNumber());
    }

    public interface PhoneActionListener {
        void onOtpSent(String phone);
    }
}
