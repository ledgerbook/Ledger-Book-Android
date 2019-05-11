package com.android.ledgerbook.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.ledgerbook.App;
import com.android.ledgerbook.BuildConfig;
import com.android.ledgerbook.R;
import com.android.ledgerbook.domain.UseCaseManager;
import com.android.ledgerbook.models.RetryCallEvent;
import com.android.ledgerbook.utils.StateSaver;
import com.android.ledgerbook.utils.iconify.IconDrawable;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class BaseActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.base_coordinatorLayout) CoordinatorLayout mainCoordinatorLayout;

    @Nullable
    @BindView(R.id.app_toolbar) Toolbar toolbar;

    @Nullable
    @BindView(R.id.toolbar_divider) View viewToolbarDivider;

    @Nullable
    @BindView(R.id.txtToolbarTitle) TextView txtToolbarTitle;

    @Nullable
    @BindView(R.id.imgHomeAction) ImageView imgToolbarHomeIcon;

    @Nullable
    @BindView(R.id.container_progress) ViewGroup containerProgress;

    @Nullable
    @BindView(R.id.activity_progressbar) ProgressBar activityProgressBar;

    @Nullable
    @BindView(R.id.txtActivityProgress) TextView txtProgress;

    private Snackbar snackbarInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        StateSaver.getInstance().restoreInstanceState(savedInstanceState);
        ButterKnife.bind(this);
        setActionBar();

        if (containerProgress != null) {
            containerProgress.setOnTouchListener((v, event) -> true);
        }
    }

    protected void hideActionBar() {
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }
    }

    protected void showActionBar() {
        if (toolbar != null) {
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    public void showActivityProgressWithText(String progressText) {
        if (containerProgress != null && txtProgress != null) {
            containerProgress.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(progressText)) {
                txtProgress.setVisibility(View.VISIBLE);
                txtProgress.setText(progressText);
            } else {
                txtProgress.setVisibility(View.INVISIBLE);
            }
        }
    }

    protected void setScreenTitle(String title) {
        if (txtToolbarTitle != null) {
            if (!TextUtils.isEmpty(title)) {
                txtToolbarTitle.setText(title);
            } else {
                txtToolbarTitle.setVisibility(View.GONE);
            }
        }
    }

    protected void setActionBar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    public void hideSnackbar() {
        if (snackbarInfo != null) {
            if (snackbarInfo.isShown()) {
                snackbarInfo.dismiss();
            }
        }
    }

    public void hideActivityProgress() {
        if (containerProgress != null) {
            containerProgress.setVisibility(View.GONE);
        }
    }

    protected String getScreenTitle() {
        if (txtToolbarTitle != null) {
            return txtToolbarTitle.getText().toString();
        }
        return null;
    }

    protected void setHomeAsUp() {
        if (imgToolbarHomeIcon != null) {
            IconDrawable icBack = new IconDrawable(this,
                    BuildConfig.IC_ARROW_LEFT).sizeDp(15).colorRes(R.color.colorPrimary);
            imgToolbarHomeIcon.setImageDrawable(icBack);
        }
    }

    protected void setHomeAsClose() {
        if (imgToolbarHomeIcon != null) {
            IconDrawable icClose = new IconDrawable(this, BuildConfig.IC_CROSS).
                    sizeDp(15).color(R.color.colorPrimary);
            imgToolbarHomeIcon.setImageDrawable(icClose);
        }
    }

    @OnClick(R.id.imgHomeAction)
    public void onHomeIconClick(ImageView view) {
        Drawable iconDrawable = view.getDrawable();
        if (iconDrawable instanceof IconDrawable) {
            char icon = ((IconDrawable) iconDrawable).getIcon();
            if (icon == BuildConfig.IC_ARROW_LEFT) {
                onBackPressed();
            } else if (icon == BuildConfig.IC_CROSS) {
                onCancelClick();
            }
        }
    }

    protected void onCancelClick() {
        onBackPressed();
    }

    public UseCaseManager getUseCaseManager() {
        return ((App) getApplicationContext()).getUseCaseManager();
    }

    public void showNetworkError(String errorStr, boolean showRetry) {
        if (mainCoordinatorLayout != null) {
            showError(errorStr, showRetry ? getString(R.string.retry) : null,
                    v -> EventBus.getDefault().post(new RetryCallEvent()));
        }
    }

    public void showError(String strError, String strAction, View.OnClickListener actionClickListener) {
        if (mainCoordinatorLayout != null) {
            snackbarInfo = Snackbar.make(mainCoordinatorLayout, strError, Snackbar.LENGTH_INDEFINITE);
            if (strAction != null) {
                snackbarInfo.setAction(strAction, v -> {
                    snackbarInfo.dismiss();
                    if (actionClickListener != null) {
                        actionClickListener.onClick(v);
                    }
                });
            }
            View snackbarView = snackbarInfo.getView();
            snackbarInfo.setActionTextColor(Color.YELLOW);
            snackbarView.setBackgroundColor(Color.BLACK);
            TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbarInfo.show();
        }
    }

    protected int getContentView() {
        return R.layout.activity_base;
    }

    protected Fragment getContainerFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.activity_container);
    }

    protected void setContainerFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, fragment)
                .commit();
    }
}
