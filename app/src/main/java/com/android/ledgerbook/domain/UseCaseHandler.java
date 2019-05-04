package com.android.ledgerbook.domain;

import android.os.Handler;
import android.os.Looper;

import com.android.ledgerbook.models.CustomError;
import com.android.ledgerbook.storage.RepositoryHandler;
import com.android.ledgerbook.utils.Constants;

public abstract class UseCaseHandler<R> implements Runnable {
    private boolean isProgressCancellable;
    private boolean isCancelled;
    private boolean isActive;
    private Handler handler = new Handler(Looper.getMainLooper());
    private UseCaseCallback<R, CustomError> useCaseCallback;
    private RepositoryHandler repositoryHandler;

    @Override
    public void run() {
        try {
            R output = runUseCase();
            if (output != null) {
                publishResult(output);
            } else {
                publishError(new CustomError<>(Constants.ERROR_UNKNOWN));
            }
        } catch (CustomDataException e) {
            publishError(e.getCustomError());
        }
    }

    public abstract R runUseCase() throws CustomDataException;

    public <T extends CustomError> void setCallback(UseCaseCallback<R, T> resultCallback) {
        this.useCaseCallback = (UseCaseCallback<R, CustomError>) resultCallback;
    }

    public void registerRepositoryHandler(RepositoryHandler handler) {
        repositoryHandler = handler;
    }

    public boolean isProgressCancellable() {
        return isProgressCancellable;
    }

    public void setProgressCancellable(boolean isProgressCancellable) {
        this.isProgressCancellable = isProgressCancellable;
    }

    public void cancel() {
        isCancelled = true;
        if (repositoryHandler != null) {
            repositoryHandler.cancel();
        }
        isActive = false;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setActiveState(boolean activeState) {
        isActive = activeState;
    }

    public boolean isActive() {
        return isActive;
    }

    protected void publishResult(R result) {
        postToMainThread(result);
        isActive = false;
    }

    protected void publishError(CustomError error) {
        if (!isCancelled()) {
            if (error == null) {
                error = new CustomError(Constants.ERROR_UNKNOWN);
            }
            postErrorToMainThread(error);
        }
        isActive = false;
    }

    private void postToMainThread(R result) {
        if (!isCancelled()) {
            handler.post(() -> {
                if (useCaseCallback != null) {
                    useCaseCallback.onSuccess(result);
                }
            });
        }
    }

    private void postErrorToMainThread(CustomError error) {
        if (!isCancelled()) {
            handler.post(() -> {
                if (useCaseCallback != null) {
                    useCaseCallback.onError(error);
                }
            });
        }
    }
}
