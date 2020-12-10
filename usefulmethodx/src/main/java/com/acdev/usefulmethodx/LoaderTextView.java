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

import static com.acdev.usefulmethodx.Constant.CORNER_DEFAULT;
import static com.acdev.usefulmethodx.Constant.MAX_WEIGHT;
import static com.acdev.usefulmethodx.Constant.USE_GRADIENT_DEFAULT;

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
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.loaderView, 0, 0);
        loaderController.setWidthWeight(typedArray.getFloat(R.styleable.loaderView_width_weight, MAX_WEIGHT));
        loaderController.setHeightWeight(typedArray.getFloat(R.styleable.loaderView_height_weight, MAX_WEIGHT));
        loaderController.setUseGradient(typedArray.getBoolean(R.styleable.loaderView_use_gradient, USE_GRADIENT_DEFAULT));
        loaderController.setCorners(typedArray.getInt(R.styleable.loaderView_corners, CORNER_DEFAULT));
        defaultColorResource = typedArray.getColor(R.styleable.loaderView_custom_color, ContextCompat.getColor(getContext(), R.color.default_color));
        darkerColorResource = typedArray.getColor(R.styleable.loaderView_custom_color, ContextCompat.getColor(getContext(), R.color.darker_color));
        typedArray.recycle();
        showShimmer();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        loaderController.onSizeChanged();
    }

    public void showShimmer() {
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

