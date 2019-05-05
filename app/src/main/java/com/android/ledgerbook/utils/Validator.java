package com.android.ledgerbook.utils;

import android.view.View;

public interface Validator {
    String NO_ERROR = "noError";

    int MANDATORY = 0;
    int PHONE = 1;
    int OTP = 2;
    int POSTAL_CODE = 3;
    int AMOUNT = 4;

    String runValidation(View view);
}
