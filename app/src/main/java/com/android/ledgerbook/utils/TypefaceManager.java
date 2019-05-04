package com.android.ledgerbook.utils;

import android.content.Context;
import android.util.SparseArray;

/**
 * The manager of typefaces.
 *
 * @author Evgeny Shishkin
 * @see <a href="https://github.com/johnkil/Android-RobotoTextView/blob/master/robototextview/src/main/java/com/devspark/robototextview/util/RobotoTypefaceManager.java">RobotoTypefaceManager</a>
 */
public class TypefaceManager {

    /**
     * Array of created typefaces for later reused.
     */
    private final static SparseArray<android.graphics.Typeface> mTypefaces = new SparseArray<android.graphics.Typeface>(22);

    /**
     * Obtain typeface.
     *
     * @param context       The Context the widget is running in, through which it can access the current theme, resources, etc.
     * @param typefaceValue The value of "typeface" attribute
     * @return specify {@link android.graphics.Typeface}
     * @throws IllegalArgumentException if unknown `typeface` attribute value.
     */
    public static android.graphics.Typeface obtainTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
        android.graphics.Typeface typeface = mTypefaces.get(typefaceValue);
        if (typeface == null) {
            typeface = createTypeface(context, typefaceValue);
            mTypefaces.put(typefaceValue, typeface);
        }
        return typeface;
    }

    /**
     * Obtain typeface.
     *
     * @param context    The Context the widget is running in, through which it can access the current theme, resources, etc.
     * @param fontFamily The value of "fontFamily" attribute
     * @param textWeight The value of "textWeight" attribute
     * @param textStyle  The value of "textStyle" attribute
     * @return specify {@link android.graphics.Typeface}
     * @throws IllegalArgumentException if unknown `typeface` attribute value.
     */
    public static android.graphics.Typeface obtainTypeface(
        Context context, int fontFamily, int textWeight, int textStyle) throws IllegalArgumentException {
        int typefaceValue = getTypefaceValue(fontFamily, textWeight, textStyle);
        return obtainTypeface(context, typefaceValue);
    }

    /**
     * @param fontFamily The value of "fontFamily" attribute
     * @param textWeight The value of "textWeight" attribute
     * @param textStyle  The value of "textStyle" attribute
     * @return typeface value
     */
    private static int getTypefaceValue(int fontFamily, int textWeight, int textStyle) {
        int typeface;
        if (fontFamily == FontFamily.ROBOTO) {
            if (textWeight == TextWeight.NORMAL) {
                switch (textStyle) {
                    case TextStyle.NORMAL:
                        typeface = Typeface.ROBOTO_REGULAR;
                        break;
                    default:
                        throw new IllegalArgumentException("`textStyle` attribute value " + textStyle +
                            " is not supported for this fontFamily " + fontFamily +
                            " and textWeight " + textWeight);
                }
            } else if (textWeight == TextWeight.MEDIUM) {
                switch (textStyle) {
                    case TextStyle.NORMAL:
                        typeface = Typeface.ROBOTO_MEDIUM;
                        break;
                    default:
                        throw new IllegalArgumentException("`textStyle` attribute value " + textStyle +
                            " is not supported for this fontFamily " + fontFamily +
                            " and textWeight " + textWeight);
                }
            } else if (textWeight == TextWeight.LIGHT) {
                switch (textStyle) {
                    case TextStyle.NORMAL:
                        typeface = Typeface.ROBOTO_LIGHT;
                        break;
                    default:
                        throw new IllegalArgumentException("`textStyle` attribute value " + textStyle +
                            " is not supported for this fontFamily " + fontFamily +
                            " and textWeight " + textWeight);
                }
            } else {
                throw new IllegalArgumentException("`textWeight` attribute value " + textWeight +
                    " is not supported for this font family " + fontFamily);
            }
        } else {
            throw new IllegalArgumentException("Unknown `fontFamily` attribute value " + fontFamily);
        }

        return typeface;
    }

    /**
     * Create typeface from assets.
     *
     * @param context       The Context the widget is running in, through which it can
     *                      access the current theme, resources, etc.
     * @param typefaceValue The value of "typeface" attribute
     * @return Roboto {@link android.graphics.Typeface}
     * @throws IllegalArgumentException if unknown `typeface` attribute value.
     */

    private static android.graphics.Typeface createTypeface(Context context,
                                                            int typefaceValue) throws IllegalArgumentException {
        String typefacePath;
        switch (typefaceValue) {
            case Typeface.ROBOTO_REGULAR:
                typefacePath = "fonts/Roboto-Regular.ttf";
                break;
            case Typeface.ROBOTO_LIGHT:
                typefacePath = "fonts/Roboto-Light.ttf";
                break;
            case Typeface.ROBOTO_MEDIUM:
                typefacePath = "fonts/Roboto-Medium.ttf";
                break;
            default:
                throw new IllegalArgumentException("Unknown `typeface` attribute value " + typefaceValue);
        }

        return android.graphics.Typeface.createFromAsset(context.getAssets(), typefacePath);
    }

    /**
     * Available values ​​for the "typeface" attribute.
     */
    public class Typeface {
        public final static int ROBOTO_REGULAR = 0;
        public static final int ROBOTO_MEDIUM = 1;
        public static final int ROBOTO_LIGHT = 2;
    }

    /**
     * Available values ​​for the "fontFamily" attribute.
     */
    public class FontFamily {
        public static final int ROBOTO = 0;
    }

    /**
     * Available values ​​for the "textWeight" attribute.
     */
    public class TextWeight {
        public static final int NORMAL = 0;
        public static final int BOLD = 1;
        public static final int SEMI_BOLD = 2;
        public static final int MEDIUM = 3;
        public static final int LIGHT = 4;
    }

    /**
     * Available values ​​for the "textStyle" attribute.
     */
    public class TextStyle {
        public static final int NORMAL = 0;
        public static final int BLACK = 1;
        public static final int ITALIC = 2;
    }
}
