package com.android.ledgerbook.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ledgerbook.R;
import com.android.ledgerbook.models.CreateBookRequest;
import com.android.ledgerbook.models.CreateBookResponse;
import com.android.ledgerbook.ui.BaseFragment;
import com.android.ledgerbook.ui.widgets.CustomInputBox;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateBookFragment extends BaseFragment<CreateBookFragment.CreateBookActionListener> {

    @BindView(R.id.ibOwnerName) CustomInputBox ibOwnerName;
    @BindView(R.id.ibBusinessName) CustomInputBox ibBusinessName;
    @BindView(R.id.ibBusinessAddress) CustomInputBox ibBusinessAddress;
    @BindView(R.id.ibEmail) CustomInputBox ibEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_book, container, false);
        bindView(view);
        return view;
    }

    @OnClick(R.id.btnCreate)
    void onClick(View view) {
        boolean isOwnerNameValid = validateInputBox(ibOwnerName);
        boolean isBusinessNameValid = validateInputBox(ibBusinessName);
        boolean isBusinessAddressValid = validateInputBox(ibBusinessAddress);
        String emailId = ibEmail.getText();
        boolean isEmailValid = true;
        if (!TextUtils.isEmpty(emailId)) {
            isEmailValid = validateInputBox(ibEmail);
        }

        if (isOwnerNameValid && isBusinessNameValid && isBusinessAddressValid && isEmailValid) {
            CreateBookRequest request = new CreateBookRequest(
                    ibOwnerName.getText(),
                    ibBusinessName.getText(),
                    ibBusinessAddress.getText(),
                    ibEmail.getText()
            );

            registerExecuteUseCaseWithActivityProgress(getUseCaseManager().createBook(request),
                    this::onBookCreated, null);
        }
    }

    public void onBookCreated(CreateBookResponse createBookResponse) {
        fragmentActionListener.onBookCreated();
    }

    private boolean validateInputBox(CustomInputBox inputBox) {
        if (!inputBox.isValid()) {
            inputBox.handleError(true);
            return false;
        } else {
            inputBox.setErrorEnabled(false);
            return true;
        }
    }

    public interface CreateBookActionListener {
        void onBookCreated();
    }
}
