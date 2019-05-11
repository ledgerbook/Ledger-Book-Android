package com.android.ledgerbook.utils.iconify;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.ledgerbook.R;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.applyDimension;
import static java.lang.String.valueOf;

public final class Iconify {

    public static final String TAG = "Iconify";
    private static final String TTF_FILE = "fonts/custom.ttf";
    private static Typeface typeface = null;

    private Iconify() {
        // Prevent instantiation
    }

    public static void setIcon(TextView textView, char value) {
        textView.setTypeface(getTypeface(textView.getContext()));
        textView.setText(valueOf(value));
    }

    public static int convertDpToPx(Context context, float dp) {
        return (int) applyDimension(COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static boolean isEnabled(int[] stateSet) {
        for (int state : stateSet) {
            if (state == android.R.attr.state_enabled) {
                return true;
            }
        }
        return false;
    }

    public static Typeface getTypeface(Context context) {
        if (typeface == null) typeface = Typeface.createFromAsset(context.getAssets(), TTF_FILE);
        return typeface;
    }

    public static IconDrawable getDrawable(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.fontDrawableView);
            int iconId = a.getInt(R.styleable.fontDrawableView_fontDrawable, -1);
            if (iconId != -1) {
                char icon = (char) Integer.parseInt("E" + iconId, 16);
                int size = a.getDimensionPixelSize(R.styleable.fontDrawableView_fontDrawableSize, -1);
                if (size == -1) size = 15;
                int iconColor = a.getColor(R.styleable.fontDrawableView_fontDrawableColor,
                    ContextCompat.getColor(context, android.R.color.black));

                return new IconDrawable(context, icon).sizePx(size).color(iconColor);

            }
            a.recycle();
        }
        return null;
    }
}
