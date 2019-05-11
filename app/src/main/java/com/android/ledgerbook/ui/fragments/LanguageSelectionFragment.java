package com.android.ledgerbook.ui.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.ledgerbook.R;
import com.android.ledgerbook.ui.BaseFragment;
import com.android.ledgerbook.ui.widgets.CustomTextView;
import com.android.ledgerbook.utils.LocaleUtils;
import com.android.ledgerbook.utils.TypefaceManager;
import com.android.ledgerbook.utils.TypefaceUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LanguageSelectionFragment extends BaseFragment<LanguageSelectionFragment.LanguageSelectionActionListener> {

    @BindView(R.id.txtEnglish) CustomTextView txtEnglish;
    @BindView(R.id.txtHindi) CustomTextView txtHindi;
    @BindView(R.id.imgTickEnglish) ImageView imgTickEnglish;
    @BindView(R.id.imgTickHindi) ImageView imgTickHindi;
    @BindView(R.id.btnSelectLanguage) Button btnSelectLanguage;

    private Typeface robotoRegular;
    private Typeface robotoMedium;
    String selectedLanguage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_language_selection, container, false);
        bindView(view);
        robotoMedium = TypefaceManager.obtainTypeface(getContext(), TypefaceManager.Typeface.ROBOTO_MEDIUM);
        robotoRegular = TypefaceManager.obtainTypeface(getContext(), TypefaceManager.Typeface.ROBOTO_REGULAR);
        return view;
    }

    @OnClick({
            R.id.txtEnglish,
            R.id.txtHindi,
            R.id.btnSelectLanguage
    })
    void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.txtEnglish:
                TypefaceUtils.setUp(txtEnglish, robotoMedium);
                TypefaceUtils.setUp(txtHindi, robotoRegular);
                imgTickEnglish.setVisibility(View.VISIBLE);
                imgTickHindi.setVisibility(View.GONE);
                btnSelectLanguage.setEnabled(true);
                selectedLanguage = LocaleUtils.LanguageCode.ENGLISH;
                break;
            case R.id.txtHindi:
                TypefaceUtils.setUp(txtEnglish, robotoRegular);
                TypefaceUtils.setUp(txtHindi, robotoMedium);
                imgTickEnglish.setVisibility(View.GONE);
                imgTickHindi.setVisibility(View.VISIBLE);
                btnSelectLanguage.setEnabled(true);
                selectedLanguage = LocaleUtils.LanguageCode.HINDI;
                break;
            case R.id.btnSelectLanguage:
                fragmentActionListener.onLanguageSelected(selectedLanguage);
        }
    }

    public interface LanguageSelectionActionListener {
        void onLanguageSelected(String language);
    }
}
