package com.acxdev.commonFunction.widget

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView

class MaxHeightNestedScrollView : NestedScrollView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var maxHeight = -1

    fun setMaxHeightDensity(dps: Int) {
        maxHeight = context.resources.getDimensionPixelSize(dps)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasuredSpec = heightMeasureSpec
        if (maxHeight > 0) {
            heightMeasuredSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthMeasureSpec, heightMeasuredSpec)
    }
}