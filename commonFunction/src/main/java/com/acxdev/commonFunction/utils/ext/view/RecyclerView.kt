package com.acxdev.commonFunction.utils.ext.view

import android.content.Context
import androidx.recyclerview.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.InflateViewGroup
import com.acxdev.commonFunction.model.ChoiceType
import com.acxdev.commonFunction.model.MultiChoice
import com.acxdev.commonFunction.utils.RecyclerViewScrollListener
import com.acxdev.commonFunction.widget.AdapterSelectable
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

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

fun RecyclerView.setStag(
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

fun RecyclerView.sync(swipeRefreshLayout: SwipeRefreshLayout) {
    addOnScrollListener(RecyclerViewScrollListener(object : RecyclerViewScrollListener.OnScroll {
        override fun onScrolled(canScrollUp: Boolean) {
            swipeRefreshLayout.isEnabled = !canScrollUp
        }
    }))

    swipeRefreshLayout.setOnChildScrollUpCallback { _, _ ->
        canScrollVertically(-1)
    }
}

fun <VB: ViewBinding> RecyclerView.asMultiChoice(
    type: ChoiceType,
    list: List<MultiChoice>,
    inflateViewGroup: InflateViewGroup<VB>,
    onLayout: VB.(Boolean, Any) -> Unit
) {
    val adapter = AdapterSelectable(inflateViewGroup, list) { b, data ->
        onLayout.invoke(this, b, data)
    }

    when(type) {
        ChoiceType.Horizontal -> setHStack(adapter)
        ChoiceType.Flex -> setFlex(adapter)
    }
}

private fun Context.numOfColumns(columnWidthDp: Float): Int {
    val displayMetrics = resources.displayMetrics
    val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
    return (screenWidthDp / columnWidthDp + 0.5).toInt()
}