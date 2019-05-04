package com.android.ledgerbook.ui.activities;

import android.content.Context;
import android.content.Intent;

import com.android.ledgerbook.ui.BaseActivity;

public class LoginActivity extends BaseActivity {

    public static void startActivityForUnauthorizedUser(Context startingContext) {
        Intent intent = new Intent(startingContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startingContext.startActivity(intent);
    }
}
