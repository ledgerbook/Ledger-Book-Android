package com.android.ledgerbook.domain;

public interface UseCaseCallback<S, E> {
    void onSuccess(S successObj);

    void onError(E errorObj);
}
