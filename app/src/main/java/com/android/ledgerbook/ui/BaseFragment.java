package com.android.ledgerbook.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.android.ledgerbook.App;
import com.android.ledgerbook.R;
import com.android.ledgerbook.domain.UseCaseCallback;
import com.android.ledgerbook.domain.UseCaseHandler;
import com.android.ledgerbook.domain.UseCaseManager;
import com.android.ledgerbook.models.BaseResponse;
import com.android.ledgerbook.models.CustomError;
import com.android.ledgerbook.models.RetryCallEvent;
import com.android.ledgerbook.models.User;
import com.android.ledgerbook.ui.activities.LoginActivity;
import com.android.ledgerbook.utils.Constants;
import com.android.ledgerbook.utils.ConsumerFunc;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment<T> extends Fragment {
    protected T fragmentActionListener;
    protected BaseActivity baseActivity;
    private String strProgress;
    private Unbinder unbinder;
    private App appInstance;
    private UseCaseHandler<? extends BaseResponse> useCaseHandler;

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            baseActivity = (BaseActivity) context;
        }
        appInstance = (App) context.getApplicationContext();

        //Set listener
        Class<T> listenerClass = null;
        try {
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                listenerClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                listenerClass = (Class<T>) ((ParameterizedType) ((Class<?>) type)
                        .getGenericSuperclass()).getActualTypeArguments()[0];
            }
        } catch (Exception ignored) {
        }
        if (listenerClass != null) {
            if (!listenerClass.isAssignableFrom(context.getClass())) {
                throw new RuntimeException(context.toString()
                        + " must implement " + listenerClass.getSimpleName());
            } else {
                fragmentActionListener = listenerClass.cast(context);
            }
        }
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (useCaseHandler != null) {
            useCaseHandler.cancel();
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }

    protected App getApp() {
        return appInstance;
    }

    protected UseCaseManager getUseCaseManager() {
        if (baseActivity != null) {
            return baseActivity.getUseCaseManager();
        }
        return null;
    }

    protected void registerExecuteUseCase(UseCaseHandler<? extends BaseResponse> useCaseHandler) {
        if (this.useCaseHandler != null && this.useCaseHandler.isActive()) {
            return;
        }
        if (baseActivity != null) {
            baseActivity.hideSnackbar();
        }
        this.useCaseHandler = useCaseHandler;
        getUseCaseManager().execute(useCaseHandler);
    }

    protected <S extends BaseResponse, E> void registerExecuteUseCase(
            UseCaseHandler<S> useCaseHandler, ConsumerFunc<S> successFunc, ConsumerFunc<E> errorFunc) {
        useCaseHandler.setCallback(new CommonUseCaseCallback<>(successFunc, errorFunc));
        registerExecuteUseCase(useCaseHandler);
    }

    protected <S extends BaseResponse, E> void registerExecuteUseCaseWithActivityProgress(
            UseCaseHandler<S> useCaseHandler, ConsumerFunc<S> successFunc, ConsumerFunc<E> errorFunc,
            int progressStrId) {
        useCaseHandler.setCallback(new CommonUseCaseCallback<>(successFunc, errorFunc));
        registerExecuteUseCase(useCaseHandler);
        if (progressStrId != -1) {
            showActivityProgress(getString(progressStrId));
        }
    }

    protected <S extends BaseResponse, E> void registerExecuteUseCaseWithActivityProgress(
            UseCaseHandler<S> useCaseHandler, ConsumerFunc<S> successFunc, ConsumerFunc<E> errorFunc) {
        registerExecuteUseCaseWithActivityProgress(useCaseHandler, successFunc, errorFunc,
                R.string.please_wait);
    }

    protected void showActivityProgress(String progressText) {
        this.strProgress = progressText;
        if (baseActivity != null) {
            baseActivity.showActivityProgressWithText(progressText);
        }
    }

    protected void hideActivityProgress() {
        if (baseActivity != null) {
            baseActivity.hideActivityProgress();
        }
    }

    protected void setScreenTitle(String title) {
        if (baseActivity != null) {
            baseActivity.setScreenTitle(title);
        }
    }

    protected String getScreenTitle() {
        if (baseActivity != null) {
            return baseActivity.getScreenTitle();
        }
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void retry(RetryCallEvent call) {
        registerExecuteUseCase(useCaseHandler);
        if (!TextUtils.isEmpty(strProgress)) {
            showActivityProgress(strProgress);
        }
    }

    protected void bindView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    protected UseCaseHandler<? extends BaseResponse> getUseCaseHandler() {
        return useCaseHandler;
    }

    public class CommonUseCaseCallback<S extends BaseResponse, E> implements UseCaseCallback<S, CustomError<E>> {
        ConsumerFunc<S> successFunc;
        ConsumerFunc<E> errorFunc;

        public CommonUseCaseCallback(ConsumerFunc<S> successFunc, ConsumerFunc<E> errorFunc) {
            this.successFunc = successFunc;
            this.errorFunc = errorFunc;
        }

        @Override
        public void onSuccess(S successObj) {
            if (baseActivity != null) {
                baseActivity.hideActivityProgress();
            }
            successFunc.accept(successObj);
        }

        @Override
        public void onError(CustomError<E> error) {
            if (!handleError(error)) {
                if (errorFunc != null) {
                    E errorObj = error.getErrorObj();
                    if (errorObj != null) {
                        errorFunc.accept(errorObj);
                    }
                }
            }
        }
    }

    protected boolean handleError(CustomError error) {
        if (baseActivity != null) {
            baseActivity.hideActivityProgress();

            int errorCode = error.getErrorCode();
            if (errorCode > 0) {
                String errorStr = null;
                switch (errorCode) {
                    case Constants.ERROR_UNEXPECTED:
                        errorStr = getString(R.string.something_went_wrong);
                        break;
                    case Constants.ERROR_NO_NETWORK:
                        errorStr = getString(R.string.error_no_network);
                        break;
                    case Constants.ERROR_UNAUTHORIZED:
                        Activity activity = getActivity();
                        if (!(activity instanceof LoginActivity) && User.getInstance().getId() != 0) {
                            getUseCaseManager().logout();
                            LoginActivity.startActivityForUnauthorizedUser(BaseFragment.this.getContext());
                        }
                        break;
                    default:
                        if (errorCode >= 400 && errorCode <= 500) {
                            String errorDetailsStr = error.getDetail();
                            if (TextUtils.isEmpty(errorDetailsStr)) {
                                errorDetailsStr = error.getErrors();
                            }
                            if (!TextUtils.isEmpty(errorDetailsStr)) {
                                baseActivity.showError(errorDetailsStr, getString(android.R.string.ok), null);
                            } else {
                                if (errorCode != 400) {
                                    errorStr = getString(R.string.something_went_wrong);
                                }
                            }
                        }
                }
                if (!TextUtils.isEmpty(errorStr)) {
                    baseActivity.showNetworkError(errorStr, true);
                }
            }
        }
        return false;
    }
}
