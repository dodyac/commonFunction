package com.acdev.usefulmethodx

import android.graphics.Paint

interface LoaderView {
    fun setRectColor(rectPaint: Paint?)

    fun invalidate()

    fun valueSet(): Boolean
}