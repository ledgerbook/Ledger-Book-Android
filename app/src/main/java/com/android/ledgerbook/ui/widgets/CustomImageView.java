package com.android.ledgerbook.ui.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.android.ledgerbook.utils.iconify.Iconify;

public class CustomImageView extends AppCompatImageView {
    public CustomImageView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Drawable fontDrawable = Iconify.getDrawable(context, attrs);
        if (fontDrawable != null) {
            setImageDrawable(fontDrawable);
        }
    }
}
