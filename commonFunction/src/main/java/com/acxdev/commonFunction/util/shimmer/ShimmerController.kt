package com.acxdev.commonFunction.util.shimmer

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.*
import android.view.animation.LinearInterpolator
import com.acxdev.commonFunction.common.ConstantX
import com.acxdev.commonFunction.common.ConstantX.COLOR_DEFAULT_GRADIENT
import com.acxdev.commonFunction.common.ConstantX.MAX_WEIGHT
import com.acxdev.commonFunction.common.ConstantX.MIN_WEIGHT

class ShimmerController(view: ShimmerView) : AnimatorUpdateListener {
    private var shimmerView: ShimmerView? = view
    private var rectPaint: Paint? = null
    private var linearGradient: LinearGradient? = null
    private var progress = 0f
    private var valueAnimator: ValueAnimator? = null
    private var widthWeight = MAX_WEIGHT
    private var heightWeight = MAX_WEIGHT
    private var useGradient = ConstantX.USE_GRADIENT_DEFAULT
    private var corners = ConstantX.CORNER_DEFAULT
    private val maxColorConstantValue = 255
    private val animationCycleDuration = 750

    init { init() }

    private fun init() {
        rectPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        shimmerView!!.setRectColor(rectPaint)
        setValueAnimator(0.5f, 1f, ObjectAnimator.INFINITE)
    }

    fun onDraw(canvas: Canvas) { onDraw(canvas, 0f, 0f, 0f, 0f) }

    fun onDraw(canvas: Canvas, left_pad: Float, top_pad: Float, right_pad: Float, bottom_pad: Float) {
        val marginHeight = canvas.height * (1 - heightWeight) / 2
        rectPaint!!.alpha = (progress * maxColorConstantValue).toInt()
        if (useGradient) prepareGradient(canvas.width * widthWeight)
        canvas.drawRoundRect(RectF(0 + left_pad, marginHeight + top_pad,
            canvas.width * widthWeight - right_pad, canvas.height - marginHeight - bottom_pad),
            corners.toFloat(), corners.toFloat(), rectPaint!!)
    }

    fun onSizeChanged() {
        linearGradient = null
        startLoading()
    }

    private fun prepareGradient(width: Float) {
        if (linearGradient == null) linearGradient = LinearGradient(0F, 0F, width, 0F,
            rectPaint!!.color, COLOR_DEFAULT_GRADIENT, Shader.TileMode.MIRROR)
        rectPaint!!.shader = linearGradient
    }

    fun startLoading() {
        if (valueAnimator != null && !shimmerView!!.valueSet()) {
            valueAnimator!!.cancel()
            init()
            valueAnimator!!.start()
        }
    }

    fun setHeightWeight(heightWeight: Float) { this.heightWeight = validateWeight(heightWeight) }

    fun setWidthWeight(widthWeight: Float) { this.widthWeight = validateWeight(widthWeight) }

    fun setUseGradient(useGradient: Boolean) { this.useGradient = useGradient }

    fun setCorners(corners: Int) { this.corners = corners }

    private fun validateWeight(weight: Float): Float {
        return if (weight > MAX_WEIGHT) MAX_WEIGHT else weight.coerceAtLeast(MIN_WEIGHT)
    }

    fun stopLoading() {
        if (valueAnimator != null) {
            valueAnimator!!.cancel()
            setValueAnimator(progress, 0f, 0)
            valueAnimator!!.start()
        }
    }

    private fun setValueAnimator(begin: Float, end: Float, repeatCount: Int) {
        valueAnimator = ValueAnimator.ofFloat(begin, end)
        valueAnimator?.repeatCount = repeatCount
        valueAnimator?.duration = animationCycleDuration.toLong()
        valueAnimator?.repeatMode = ValueAnimator.REVERSE
        valueAnimator?.interpolator = LinearInterpolator()
        valueAnimator?.addUpdateListener(this)
    }

    override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
        progress = valueAnimator.animatedValue as Float
        shimmerView!!.invalidate()
    }

    fun removeAnimatorUpdateListener() {
        if (valueAnimator != null) {
            valueAnimator!!.removeUpdateListener(this)
            valueAnimator!!.cancel()
        }
        progress = 0f
    }
}