package com.android.ledgerbook.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.ledgerbook.ui.BaseActivity;
import com.android.ledgerbook.ui.fragments.CreateBookFragment;

public class CreateBookActivity extends BaseActivity implements CreateBookFragment.CreateBookActionListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBarDivider();

        Fragment fragment = getContainerFragment();
        if (fragment == null) {
            fragment = new CreateBookFragment();
            replaceContainerFragment(fragment);
        }
    }

    public void startActivity(Context startingContext) {
        Intent intent = new Intent(startingContext.getApplicationContext(), CreateBookActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startingContext.startActivity(intent);
    }

    @Override
    public void onBookCreated() {

    }
}
