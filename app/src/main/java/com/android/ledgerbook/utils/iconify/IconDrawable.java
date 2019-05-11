package com.android.ledgerbook.utils.iconify;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;

import static java.lang.String.valueOf;

public class IconDrawable extends Drawable {

    public static final int ANDROID_ACTIONBAR_ICON_SIZE_DP = 24;

    private final Context context;

    private final char icon;

    private TextPaint paint;

    private int size = -1;

    private int alpha = 255;

    private Paint backgroundPaint;

    private int borderThickness;

    private int borderColor;

    private int backgroundColor;

    private static final int DEFAULT_PADDING_DP = 5;

    /**
     * Create an IconDrawable.
     *
     * @param context Your activity or application context.
     * @param icon    The icon you want this drawable to display.
     */
    public IconDrawable(Context context, char icon) {
        this(context, icon, false);
    }

    /**
     * Create an IconDrawable.
     *
     * @param context Your activity or application context.
     * @param icon    The icon you want this drawable to display.
     */
    public IconDrawable(Context context, char icon, boolean isTextDrawable) {
        this.context = context;
        this.icon = icon;
        paint = new TextPaint();
        if (isTextDrawable) {
            paint.setTypeface(Typeface.DEFAULT);
        } else {
            paint.setTypeface(Iconify.getTypeface(context));
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setUnderlineText(false);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
    }

    /**
     * Set the size of this icon to the standard Android ActionBar.
     *
     * @return The current IconDrawable for chaining.
     */
    public IconDrawable actionBarSize() {
        return sizeDp(ANDROID_ACTIONBAR_ICON_SIZE_DP);
    }

    /**
     * Set the size of the drawable.
     *
     * @param dimenRes The dimension resource.
     * @return The current IconDrawable for chaining.
     */
    public IconDrawable sizeRes(int dimenRes) {
        return sizePx(context.getResources().getDimensionPixelSize(dimenRes));
    }

    /**
     * Set the size of the drawable.
     *
     * @param size The size in density-independent pixels (dp).
     * @return The current IconDrawable for chaining.
     */
    public IconDrawable sizeDp(int size) {
        return sizePx(Iconify.convertDpToPx(context, size));
    }

    /**
     * Set the size of the drawable.
     *
     * @param size The size in pixels (px).
     * @return The current IconDrawable for chaining.
     */
    public IconDrawable sizePx(int size) {
        this.size = size;
        setBounds(0, 0, size, size);
        invalidateSelf();
        return this;
    }

    /**
     * Set the color of the drawable.
     *
     * @param color The color, usually from android.graphics.Color or 0xFF012345.
     * @return The current IconDrawable for chaining.
     */
    public IconDrawable color(int color) {
        paint.setColor(color);
        invalidateSelf();
        return this;
    }

    /**
     * Set the color of the drawable.
     *
     * @param colorRes The color resource, from your R file.
     * @return The current IconDrawable for chaining.
     */
    public IconDrawable colorRes(int colorRes) {
        paint.setColor(ContextCompat.getColor(context, colorRes));
        invalidateSelf();
        return this;
    }

    /**
     * Set the alpha of this drawable.
     *
     * @param alpha The alpha, between 0 (transparent) and 255 (opaque).
     * @return The current IconDrawable for chaining.
     */
    public IconDrawable alpha(int alpha) {
        setAlpha(alpha);
        invalidateSelf();
        return this;
    }

    @Override
    public int getIntrinsicHeight() {
        return size;
    }

    @Override
    public int getIntrinsicWidth() {
        return size;
    }

    @Override
    public void draw(Canvas canvas) {
        if (backgroundColor != 0) {
            RectF rect = getIconBackgroundRect();
            backgroundPaint.setStyle(Paint.Style.FILL);
            backgroundPaint.setColor(backgroundColor);
            canvas.drawOval(rect, backgroundPaint);
        }

        if (borderThickness > 0) {
            RectF rect = getIconBackgroundRect();
            rect.inset(borderThickness / 2, borderThickness / 2);
            backgroundPaint.setColor(borderColor);
            backgroundPaint.setStyle(Paint.Style.STROKE);
            backgroundPaint.setStrokeWidth(borderThickness);
            canvas.drawOval(rect, backgroundPaint);
        }

        paint.setTextSize(getBounds().height());
        Rect textBounds = new Rect();
        String textValue = valueOf(icon);
        paint.getTextBounds(textValue, 0, 1, textBounds);
        float textBottom = (getBounds().height() - textBounds.height()) / 2f + textBounds.height() - textBounds.bottom;
        canvas.drawText(textValue, getBounds().width() / 2f, textBottom, paint);
    }

    private RectF getIconBackgroundRect() {
        RectF rect = new RectF(getBounds());
        int paddingPx = Iconify.convertDpToPx(context, DEFAULT_PADDING_DP);
        rect.set(rect.left - paddingPx, rect.top - paddingPx, rect.right + paddingPx, rect.bottom + paddingPx);
        return rect;
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    public boolean setState(int[] stateSet) {
        int oldValue = paint.getAlpha();
        int newValue = Iconify.isEnabled(stateSet) ? alpha : alpha / 2;
        paint.setAlpha(newValue);
        return oldValue != newValue;
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public void clearColorFilter() {
        paint.setColorFilter(null);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    /**
     * Sets paint style.
     *
     * @param style to be applied
     */
    public void setStyle(Paint.Style style) {
        paint.setStyle(style);
    }

    public char getIcon() {
        return icon;
    }

    public IconDrawable drawBorder(int thickness, int color) {
        borderThickness = thickness;
        borderColor = color;
        invalidateSelf();
        return this;
    }

    public IconDrawable setBackgroundColor(int color) {
        backgroundColor = color;
        invalidateSelf();
        return this;
    }
}
