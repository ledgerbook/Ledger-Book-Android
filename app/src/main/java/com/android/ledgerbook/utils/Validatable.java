package com.android.ledgerbook.utils;

public interface Validatable {
    boolean isValid();

    void handleError(boolean isError);
}
