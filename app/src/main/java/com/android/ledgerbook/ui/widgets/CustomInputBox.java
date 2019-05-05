package com.android.ledgerbook.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ledgerbook.R;
import com.android.ledgerbook.utils.Validatable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomInputBox extends LinearLayout implements Validatable {

    @BindView(R.id.txtIbTitle) TextView txtIbTitle;
    @BindView(R.id.containerIbData) FrameLayout containerIbData;
    @BindView(R.id.txtIbError) TextView txtIbError;

    private CustomTextInput edtData;
    boolean isErrorEnabled;

    public CustomInputBox(Context context) {
        super(context);
        init(context, null);
    }

    public CustomInputBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomInputBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomInputBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.widget_input_box, this, true);
        ButterKnife.bind(this, view);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomInputBox);
            String title = a.getString(R.styleable.CustomInputBox_IbTitle);
            if (!TextUtils.isEmpty(title)) {
                txtIbTitle.setText(title);
            }
            a.recycle();
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof CustomTextInput && edtData == null) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int negativeMargin = getResources().getDimensionPixelSize(R.dimen.negative_margin);
            layoutParams.leftMargin = negativeMargin;
            layoutParams.rightMargin = negativeMargin;
            child.setLayoutParams(layoutParams);
            containerIbData.addView(child);
            edtData = (CustomTextInput) child;
            edtData.setPadding(
                    child.getPaddingLeft(),
                    getResources().getDimensionPixelSize(R.dimen.input_text_top_padding),
                    child.getPaddingRight(),
                    getResources().getDimensionPixelSize(R.dimen.input_text_bottom_padding)
            );
        } else {
            super.addView(child, index, params);
        }
    }

    public String getText() {
        return edtData.getPrefixLessText();
    }

    public void setError(CharSequence error) {
        setErrorEnabled(true);
        txtIbError.setText(error);
    }

    public void setErrorEnabled(boolean enabled) {
        if (isErrorEnabled != enabled) {
            isErrorEnabled = enabled;
            if (enabled) {
                txtIbError.setVisibility(VISIBLE);
            } else {
                txtIbError.setVisibility(GONE);
            }
        }
    }

    @Override
    public boolean isValid() {
        if (isErrorEnabled) {
            return false;
        }
        return edtData.isValid();
    }

    @Override
    public void handleError(boolean isError) {
        edtData.handleError(isError);
    }

    public void setText(CharSequence text) {
        edtData.setText(text);
    }
}
