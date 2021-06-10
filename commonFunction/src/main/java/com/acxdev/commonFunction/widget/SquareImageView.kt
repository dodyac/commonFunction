package com.acxdev.commonFunction.widget

import android.content.Context
import android.util.AttributeSet

open class SquareImageView : androidx.appcompat.widget.AppCompatImageView {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) :
            super(context!!, attrs, defStyle) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width: Int = measuredWidth
        setMeasuredDimension(width, width)
    }
}