package com.acdev.commonFunction

import android.graphics.Paint

interface LoaderView {
    fun setRectColor(rectPaint: Paint?)

    fun invalidate()

    fun valueSet(): Boolean
}