package com.acdev.usefulmethodx;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

public class LoaderTextView extends AppCompatTextView implements LoaderView {

    private LoaderController loaderController;
    private int defaultColorResource;
    private int darkerColorResource;

    public LoaderTextView(Context context) {
        super(context);
        init(null);
    }

    public LoaderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoaderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        loaderController = new LoaderController(this);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.loader_view, 0, 0);
        loaderController.setWidthWeight(typedArray.getFloat(R.styleable.loader_view_width_weight, LoaderConstant.MAX_WEIGHT));
        loaderController.setHeightWeight(typedArray.getFloat(R.styleable.loader_view_height_weight, LoaderConstant.MAX_WEIGHT));
        loaderController.setUseGradient(typedArray.getBoolean(R.styleable.loader_view_use_gradient, LoaderConstant.USE_GRADIENT_DEFAULT));
        loaderController.setCorners(typedArray.getInt(R.styleable.loader_view_corners, LoaderConstant.CORNER_DEFAULT));
        defaultColorResource = typedArray.getColor(R.styleable.loader_view_custom_color, ContextCompat.getColor(getContext(), R.color.default_color));
        darkerColorResource = typedArray.getColor(R.styleable.loader_view_custom_color, ContextCompat.getColor(getContext(), R.color.darker_color));
        typedArray.recycle();
        resetLoader();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        loaderController.onSizeChanged();
    }

    public void resetLoader() {
        if (!TextUtils.isEmpty(getText())) {
            super.setText(null);
            loaderController.startLoading();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        loaderController.onDraw(canvas, getCompoundPaddingLeft(),
                getCompoundPaddingTop(),
                getCompoundPaddingRight(),
                getCompoundPaddingBottom());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (loaderController != null) {
            loaderController.stopLoading();
        }
    }

    @Override
    public void setRectColor(Paint rectPaint) {
        final Typeface typeface = getTypeface();
        if (typeface != null && typeface.getStyle() == Typeface.BOLD) {
            rectPaint.setColor(darkerColorResource);
        } else {
            rectPaint.setColor(defaultColorResource);
        }
    }

    @Override
    public boolean valueSet() {
        return !TextUtils.isEmpty(getText());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        loaderController.removeAnimatorUpdateListener();
    }
}

