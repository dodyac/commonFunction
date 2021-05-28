package com.acdev.commonFunction.util

import android.graphics.Paint

interface ShimmerView {
    fun setRectColor(rectPaint: Paint?)

    fun invalidate()

    fun valueSet(): Boolean
}