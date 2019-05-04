package com.android.ledgerbook.domain;


import com.android.ledgerbook.models.CustomError;

public class CustomDataException extends Exception {
    private CustomError customError;

    public CustomError getCustomError() {
        return customError;
    }

    public void setCustomError(CustomError customError) {
        this.customError = customError;
    }
}
