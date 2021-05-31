package com.acdev.commonFunction.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat.getColor
import com.acdev.commonFunction.R
import com.acdev.commonFunction.common.Constantx.Companion.CORNER_DEFAULT
import com.acdev.commonFunction.common.Constantx.Companion.MAX_WEIGHT
import com.acdev.commonFunction.common.Constantx.Companion.USE_GRADIENT_DEFAULT
import com.acdev.commonFunction.util.shimmer.ShimmerController
import com.acdev.commonFunction.util.shimmer.ShimmerView

class ShimmerText : AppCompatTextView, ShimmerView {

    private var shimmerController: ShimmerController? = null
    private var defaultColorResource = 0
    private var darkerColorResource = 0

    constructor(context: Context) : super(context) { init(context, null) }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init(context, attrs) }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) { init(context, attrs) }

    private fun init(context: Context, attrs: AttributeSet?) {
        shimmerController = ShimmerController(this)
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ShimmerText, 0, 0)
        shimmerController!!.setWidthWeight(typedArray.getFloat(R.styleable.ShimmerText_shimmerTextWidth, MAX_WEIGHT))
        shimmerController!!.setHeightWeight(typedArray.getFloat(R.styleable.ShimmerText_shimmerTextHeight, MAX_WEIGHT))
        shimmerController!!.setUseGradient(typedArray.getBoolean(R.styleable.ShimmerText_shimmerTextGradientColor, USE_GRADIENT_DEFAULT))
        shimmerController!!.setCorners(typedArray.getInt(R.styleable.ShimmerText_shimmerTextCornerRadius, CORNER_DEFAULT))
        defaultColorResource = typedArray.getColor(R.styleable.ShimmerText_shimmerTextColor, getColor(context, R.color.default_color))
        darkerColorResource = typedArray.getColor(R.styleable.ShimmerText_shimmerTextColor, getColor(context, R.color.darker_color))
        typedArray.recycle()
        showShimmer()
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        shimmerController!!.onSizeChanged()
    }

    fun showShimmer() {
        if (!TextUtils.isEmpty(text)) {
            super.setText(null)
            shimmerController!!.startLoading()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        shimmerController!!.onDraw(
            canvas, compoundPaddingLeft.toFloat(),
            compoundPaddingTop.toFloat(),
            compoundPaddingRight.toFloat(),
            compoundPaddingBottom.toFloat()
        )
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        if (shimmerController != null) shimmerController!!.stopLoading()
    }

    override fun setRectColor(rectPaint: Paint?) {
        val typeface: Typeface = typeface
        if (typeface.style == Typeface.BOLD) rectPaint?.color = darkerColorResource
        else rectPaint?.color = defaultColorResource
    }

    override fun valueSet(): Boolean { return !TextUtils.isEmpty(text) }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        shimmerController!!.removeAnimatorUpdateListener()
    }
}