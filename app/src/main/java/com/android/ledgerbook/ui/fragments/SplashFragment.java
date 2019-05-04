package com.android.ledgerbook.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ledgerbook.R;
import com.android.ledgerbook.models.CustomError;
import com.android.ledgerbook.models.User;
import com.android.ledgerbook.ui.BaseFragment;
import com.android.ledgerbook.utils.Constants;

public class SplashFragment extends BaseFragment<SplashFragment.SplashActionListener> {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        bindView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        registerExecuteUseCase(getUseCaseManager().initApp(), this::onUserExist, null);
    }

    public void onUserExist(User user) {
        fragmentActionListener.onAppInitiated();
    }

    @Override
    public boolean handleError(CustomError error) {
        super.handleError(error);
        if (error.getErrorCode() == Constants.ERROR_UNAUTHORIZED) {
            fragmentActionListener.onAppInitiated();
        }
        return false;
    }

    public interface SplashActionListener {
        void onAppInitiated();
    }
}
