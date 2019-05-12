package com.android.ledgerbook.utils;

import android.content.Context;

import com.android.ledgerbook.models.User;
import com.android.ledgerbook.ui.activities.CreateBookActivity;
import com.android.ledgerbook.ui.activities.HomeActivity;
import com.android.ledgerbook.ui.activities.PhoneActivity;

public class NavigationUtils {

    public static void startUserFlow(Context startingContext) {
        User user = User.getInstance();
        if (user.getId() == 0) {
            PhoneActivity.startActivity(startingContext);
        } else if (user.getBusiness() == null) {
            CreateBookActivity.startActivity(startingContext);
        } else {
            HomeActivity.startActivity(startingContext);
        }
    }
}
