package com.android.ledgerbook.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.android.ledgerbook.ui.BaseActivity;
import com.android.ledgerbook.ui.fragments.SplashFragment;
import com.android.ledgerbook.utils.NavigationUtils;

public class SplashActivity extends BaseActivity implements
        SplashFragment.SplashActionListener {

    private static final long MANDATORY_WAIT_MILLIS = 2000;
    private boolean isSessionVerificationComplete;
    private boolean isMandatoryWaitComplete;
    public Handler handler = new Handler();
    private Runnable runnableSplash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        Fragment fragment = getContainerFragment();
        if (fragment == null) {
            fragment = new SplashFragment();
            setContainerFragment(fragment);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        runnableSplash = () -> {
            isMandatoryWaitComplete = true;
            startNextActivity();
        };

        handler.postDelayed(runnableSplash, MANDATORY_WAIT_MILLIS);
    }

    @Override
    protected void onStop() {
        handler.removeCallbacks(runnableSplash);
        super.onStop();
    }

    @Override
    public void onAppInitiated() {
        isSessionVerificationComplete = true;
    }

    private void startNextActivity() {
        if (!isSessionVerificationComplete || !isMandatoryWaitComplete) {
            return;
        }

        String selectedLanguage = getApp().getPreferences().getSelectedLanguage();
        if (TextUtils.isEmpty(selectedLanguage)) {
            LanguageSelectionActivity.startActivity(this);
        } else {
            NavigationUtils.startUserFlow(this);
        }
        SplashActivity.this.finish();
    }
}
