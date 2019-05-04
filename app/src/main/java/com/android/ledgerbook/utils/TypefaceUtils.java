package com.android.ledgerbook.utils;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.ledgerbook.R;

/**
 * Utilities for working with text views.
 *
 * @author Evgeny Shishkin
 * @see <a href="https://github.com/johnkil/Android-RobotoTextView/blob/master/robototextview/src/main/java/com/devspark/robototextview/util/RobotoTypefaceUtils.java">RobotoTypefaceUtils</a>
 */
public final class TypefaceUtils {

    private TypefaceUtils() {
    }

    /**
     * Typeface initialization using the attributes. Used in RobotoTextView constructor.
     *
     * @param textView The roboto text view
     * @param context  The context the widget is running in, through which it can
     *                 access the current theme, resources, etc.
     * @param attrs    The attributes of the XML tag that is inflating the widget.
     */
    public static void initView(@NonNull TextView textView, @NonNull Context context, @Nullable AttributeSet attrs) {
        Typeface typeface;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.customTextView);
            typeface = typefaceFromAttrs(context, a);
            a.recycle();
        } else {
            typeface = TypefaceManager.obtainTypeface(context, TypefaceManager.Typeface.ROBOTO_REGULAR);
        }

        setUp(textView, typeface);
    }

    @NonNull
    public static Typeface typefaceFromAttrs(@NonNull Context context, @NonNull TypedArray a) {
        Typeface typeface;
        int typefaceValue = a.getInt(R.styleable.customTextView_typeface, TypefaceManager.Typeface.ROBOTO_REGULAR);
        typeface = TypefaceManager.obtainTypeface(context, typefaceValue);
        return typeface;
    }

    /**
     * Set up typeface for TextView. Wrapper over {@link TextView#setTypeface(Typeface)}
     * for making the font anti-aliased.
     *
     * @param textView The text view
     * @param typeface The specify typeface
     */
    public static void setUp(@NonNull TextView textView, @NonNull Typeface typeface) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        textView.setTypeface(typeface);
    }

    /**
     * Set up typeface for Paint.
     *
     * @param paint    The paint
     * @param typeface The specify typeface
     */
    public static void setUp(@NonNull Paint paint, @NonNull Typeface typeface) {
        paint.setFlags(paint.getFlags() | Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        paint.setTypeface(typeface);
    }
}
