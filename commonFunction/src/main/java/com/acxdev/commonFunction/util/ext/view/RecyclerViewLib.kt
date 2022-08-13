package com.acxdev.commonFunction.util.ext.view

import android.content.Context
import androidx.recyclerview.widget.*
import com.acxdev.commonFunction.util.ext.getCompatActivity
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.smarteist.autoimageslider.SliderViewAdapter

fun RecyclerView.set(
    adapter: RecyclerView.Adapter<*>?,
    isSnap: Boolean? = null,
    hasFixed: Boolean? = null
) {
    layoutManager = LinearLayoutManager(
        context.getCompatActivity(),
        LinearLayoutManager.HORIZONTAL,
        false
    )
    this.adapter = adapter
    if (isSnap == true) {
        val snapHelper = LinearSnapHelper()
        if (onFlingListener == null) snapHelper.attachToRecyclerView(this)
    }
    if (hasFixed == true) setHasFixedSize(true)
}

fun RecyclerView.set(
    adapter: RecyclerView.Adapter<*>?,
    spanCount: Int,
    hasFixed: Boolean? = null
) {
    this.layoutManager = GridLayoutManager(context.getCompatActivity(), spanCount)
    this.adapter = adapter
    if (hasFixed == true) setHasFixedSize(true)
}

fun RecyclerView.setGrid(
    adapter: RecyclerView.Adapter<*>?,
    numOfColumns: Float,
    hasFixed: Boolean? = null
) {
    this.layoutManager = GridLayoutManager(
        context.getCompatActivity(),
        context.getCompatActivity().numOfColumns(numOfColumns)
    )
    this.adapter = adapter
    if (hasFixed == true) setHasFixedSize(true)
}

fun RecyclerView.setStag(
    adapter: RecyclerView.Adapter<*>?,
    numOfColumns: Float,
    hasFixed: Boolean? = null
) {
    val layoutManager = StaggeredGridLayoutManager(
        context.getCompatActivity().numOfColumns(numOfColumns),
        LinearLayoutManager.VERTICAL
    )
    layoutManager.gapStrategy =
        StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

    this.layoutManager = layoutManager
    this.adapter = adapter
    if (hasFixed == true) setHasFixedSize(true)
}

fun RecyclerView.setFlex(adapter: RecyclerView.Adapter<*>, hasFixed: Boolean? = null) {
    val layoutManager = FlexboxLayoutManager(context.getCompatActivity())
    layoutManager.flexWrap = FlexWrap.WRAP
    layoutManager.flexDirection = FlexDirection.ROW
    this.layoutManager = layoutManager
    this.adapter = adapter
    if (hasFixed == true) setHasFixedSize(true)
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