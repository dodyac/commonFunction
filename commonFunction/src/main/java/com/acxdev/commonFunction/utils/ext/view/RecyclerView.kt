package com.acxdev.commonFunction.utils.ext.view

import android.view.ViewTreeObserver
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.acxdev.commonFunction.utils.RecyclerViewScrollListener
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

fun RecyclerView.setHStack(
    adapter: RecyclerView.Adapter<*>,
    isSnap: Boolean = false,
    hasFixed: Boolean = true
) {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    this.adapter = adapter
    setHasFixedSize(hasFixed)
    if (isSnap) {
        val snapHelper = LinearSnapHelper()
        if (onFlingListener == null) {
            snapHelper.attachToRecyclerView(this)
        }
    }
}

fun RecyclerView.setVStack(
    adapter: RecyclerView.Adapter<*>,
    spanCount: Int = 1,
    hasFixed: Boolean = true,
) {
    layoutManager = if (spanCount == 1) {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    } else {
        GridLayoutManager(context, spanCount)
    }
    this.adapter = adapter
    setHasFixedSize(hasFixed)
}

fun RecyclerView.setGrid(
    adapter: RecyclerView.Adapter<*>,
    @DimenRes columnWidthRes: Int,
    hasFixed: Boolean = true
) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)

            val columnWidthPx = context.resources.getDimension(columnWidthRes)
            val columnCount = ((width / columnWidthPx) + 0.5).toInt()

            layoutManager = GridLayoutManager(context, columnCount)
            this@setGrid.adapter = adapter
            setHasFixedSize(hasFixed)
        }
    })
}

fun RecyclerView.setStag(
    adapter: RecyclerView.Adapter<*>,
    @DimenRes columnWidthRes: Int,
    hasFixed: Boolean = false
) {
    val displayMetrics = context.resources.displayMetrics
    val columnWidthPx = context.resources.getDimension(columnWidthRes)
    val screenWidthPx = displayMetrics.widthPixels
    val columnCount = ((screenWidthPx / columnWidthPx) + 0.5).toInt()

    val layoutManager = StaggeredGridLayoutManager(
        columnCount,
        LinearLayoutManager.VERTICAL
    )
    layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

    this.layoutManager = layoutManager
    this.adapter = adapter
    setHasFixedSize(hasFixed)
}

fun RecyclerView.setFlex(
    adapter: RecyclerView.Adapter<*>,
    hasFixed: Boolean = false
) {
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

fun RecyclerView.disableAnimation() {
    (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
}

fun <T : RecyclerView.Adapter<*>> RecyclerView.getOrCreateAdapter(
    id: Int,
    factory: () -> T
): T {
    @Suppress("UNCHECKED_CAST")
    return (getTag(id) as? T) ?: factory().also {
        setTag(id, it)
    }
}
