package com.acdev.commonFunction

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat.getColor
import com.acdev.commonFunction.Constant.Companion.CORNER_DEFAULT
import com.acdev.commonFunction.Constant.Companion.MAX_WEIGHT
import com.acdev.commonFunction.Constant.Companion.USE_GRADIENT_DEFAULT

class ShimmerText : AppCompatTextView, LoaderView {

    private var loaderController: LoaderController? = null
    private var defaultColorResource = 0
    private var darkerColorResource = 0

    constructor(context: Context) : super(context) { init(context, null) }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init(context, attrs) }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) { init(context, attrs) }

    private fun init(context: Context, attrs: AttributeSet?) {
        loaderController = LoaderController(this)
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.loaderView, 0, 0)
        loaderController!!.setWidthWeight(typedArray.getFloat(R.styleable.loaderView_width_weight, MAX_WEIGHT))
        loaderController!!.setHeightWeight(typedArray.getFloat(R.styleable.loaderView_height_weight, MAX_WEIGHT))
        loaderController!!.setUseGradient(typedArray.getBoolean(R.styleable.loaderView_use_gradient, USE_GRADIENT_DEFAULT))
        loaderController!!.setCorners(typedArray.getInt(R.styleable.loaderView_corners, CORNER_DEFAULT))
        defaultColorResource = typedArray.getColor(R.styleable.loaderView_custom_color, getColor(context, R.color.default_color))
        darkerColorResource = typedArray.getColor(R.styleable.loaderView_custom_color, getColor(context, R.color.darker_color))
        typedArray.recycle()
        showShimmer()
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        loaderController!!.onSizeChanged()
    }

    private fun showShimmer() {
        if (!TextUtils.isEmpty(text)) {
            super.setText(null)
            loaderController!!.startLoading()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        loaderController!!.onDraw(
            canvas, compoundPaddingLeft.toFloat(),
            compoundPaddingTop.toFloat(),
            compoundPaddingRight.toFloat(),
            compoundPaddingBottom.toFloat()
        )
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        if (loaderController != null) loaderController!!.stopLoading()
    }

    override fun setRectColor(rectPaint: Paint?) {
        val typeface: Typeface = typeface
        if (typeface.style == Typeface.BOLD) rectPaint?.color = darkerColorResource
        else rectPaint?.color = defaultColorResource
    }

    override fun valueSet(): Boolean {
        return !TextUtils.isEmpty(text)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        loaderController!!.removeAnimatorUpdateListener()
    }
}