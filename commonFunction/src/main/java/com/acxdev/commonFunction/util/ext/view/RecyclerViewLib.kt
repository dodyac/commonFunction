package com.acxdev.commonFunction.util.ext.view

import android.content.Context
import androidx.recyclerview.widget.*
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.smarteist.autoimageslider.SliderViewAdapter

fun RecyclerView.setHStack(
    adapter: RecyclerView.Adapter<*>?,
    isSnap: Boolean = false,
    hasFixed: Boolean = true
) {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    this.adapter = adapter
    setHasFixedSize(hasFixed)
    if (isSnap) {
        val snapHelper = LinearSnapHelper()
        if (onFlingListener == null) snapHelper.attachToRecyclerView(this)
    }
}

fun RecyclerView.setVStack(
    adapter: RecyclerView.Adapter<*>?,
    hasFixed: Boolean = true,
    spanCount: Int = 1
) {
    layoutManager = GridLayoutManager(context, spanCount)
    this.adapter = adapter
    setHasFixedSize(hasFixed)
}

fun RecyclerView.setGrid(
    adapter: RecyclerView.Adapter<*>?,
    numOfColumns: Float,
    hasFixed: Boolean = true
) {
    layoutManager = GridLayoutManager(context, context.numOfColumns(numOfColumns))
    this.adapter = adapter
    setHasFixedSize(hasFixed)
}

fun RecyclerView.setStaggered(
    adapter: RecyclerView.Adapter<*>?,
    numOfColumns: Float,
    hasFixed: Boolean = true
) {
    val layoutManager = StaggeredGridLayoutManager(
        context.numOfColumns(numOfColumns),
        LinearLayoutManager.VERTICAL
    )
    layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

    this.layoutManager = layoutManager
    this.adapter = adapter
    setHasFixedSize(hasFixed)
}

fun RecyclerView.setFlex(adapter: RecyclerView.Adapter<*>, hasFixed: Boolean = true) {
    val layoutManager = FlexboxLayoutManager(context)
    layoutManager.flexWrap = FlexWrap.WRAP
    layoutManager.flexDirection = FlexDirection.ROW
    this.layoutManager = layoutManager
    this.adapter = adapter
    setHasFixedSize(hasFixed)
}

fun SliderView.set(sliderViewAdapter: SliderViewAdapter<*>) {
    setSliderAdapter(sliderViewAdapter)
    setIndicatorAnimation(IndicatorAnimationType.WORM)
    setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
    startAutoCycle()
}

private fun Context.numOfColumns(columnWidthDp: Float): Int {
    val displayMetrics = resources.displayMetrics
    val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
    return (screenWidthDp / columnWidthDp + 0.5).toInt()
}