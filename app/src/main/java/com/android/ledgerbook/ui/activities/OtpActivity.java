package com.android.ledgerbook.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.ledgerbook.models.User;
import com.android.ledgerbook.ui.BaseActivity;
import com.android.ledgerbook.ui.fragments.OtpFragment;

public class OtpActivity extends BaseActivity implements OtpFragment.OtpActionListener {
    private static final String KEY_PHONE = "phone";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHomeAsUp();
        Fragment fragment = getContainerFragment();
        if (fragment == null) {
            String phone = getIntent().getStringExtra(KEY_PHONE);
            fragment = OtpFragment.newInstance(phone);
            setContainerFragment(fragment);
        }
    }

    public static void startActivity(Context startingContext, String phone) {
        Intent intent = new Intent(startingContext, OtpActivity.class);
        intent.putExtra(KEY_PHONE, phone);
        startingContext.startActivity(intent);
    }

    @Override
    public void onOtpLoginSuccess() {
        User user = User.getInstance();
        if (user.getBusiness() == null) {
            CreateBookActivity.startActivity(this);
        } else {
            // TODO: Take to home page
        }
    }

    @Override
    public void onBackPressed() {
        PhoneActivity.startActivity(this);
    }
}
