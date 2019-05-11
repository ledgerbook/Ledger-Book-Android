package com.android.ledgerbook.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.ledgerbook.ui.BaseActivity;
import com.android.ledgerbook.ui.fragments.PhoneFragment;

public class PhoneActivity extends BaseActivity implements PhoneFragment.PhoneActionListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHomeAsUp();
        Fragment fragment = getContainerFragment();
        if (fragment == null) {
            fragment = new PhoneFragment();
            setContainerFragment(fragment);
        }
    }

    public static void startActivity(Context startingContext) {
        Intent intent = new Intent(startingContext, PhoneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startingContext.startActivity(intent);
    }

    @Override
    public void onOtpSent(String phone) {
        OtpActivity.startActivity(this, phone);
        finish();
    }
}
