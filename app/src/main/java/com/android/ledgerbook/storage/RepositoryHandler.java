package com.android.ledgerbook.storage;

import retrofit2.Call;

public class RepositoryHandler {
    private Call call;

    public RepositoryHandler registerCall(Call call) {
        this.call = call;
        return this;
    }

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }
}
