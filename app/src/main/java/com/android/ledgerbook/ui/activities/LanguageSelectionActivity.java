package com.android.ledgerbook.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.ledgerbook.ui.BaseActivity;
import com.android.ledgerbook.ui.fragments.LanguageSelectionFragment;
import com.android.ledgerbook.utils.LocaleUtils;

public class LanguageSelectionActivity extends BaseActivity implements
        LanguageSelectionFragment.LanguageSelectionActionListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        Fragment fragment = getContainerFragment();
        if (fragment == null) {
            fragment = new LanguageSelectionFragment();
            setContainerFragment(fragment);
        }
    }

    @Override
    public void onLanguageSelected(String language) {
        LocaleUtils.saveNewLanguage(this, language);
    }
}
