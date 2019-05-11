package com.android.ledgerbook.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ledgerbook.R;
import com.android.ledgerbook.ui.BaseFragment;

public class OtpFragment extends BaseFragment<OtpFragment.OtpActionListener> {
    private static final String KEY_PHONE = "phone";

    public static OtpFragment newInstance(String phone) {
        Bundle args = new Bundle();
        args.putString(KEY_PHONE, phone);
        OtpFragment fragment = new OtpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp, container, true);
        bindView(view);
        return view;
    }

    public interface OtpActionListener {
        void onOtpLoginSuccess();
    }
}
