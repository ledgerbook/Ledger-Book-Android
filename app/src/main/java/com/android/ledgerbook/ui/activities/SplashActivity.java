package com.android.ledgerbook.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.ledgerbook.models.User;
import com.android.ledgerbook.ui.BaseActivity;
import com.android.ledgerbook.ui.fragments.SplashFragment;

public class SplashActivity extends BaseActivity implements
        SplashFragment.SplashActionListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        Fragment fragment = getContainerFragment();
        if (fragment == null) {
            fragment = new SplashFragment();
            replaceContainerFragment(fragment);
        }
    }

    @Override
    public void onAppInitiated() {
        User user = User.getInstance();
    }
}
