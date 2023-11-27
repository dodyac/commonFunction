package com.acxdev.commonFunction.utils

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewScrollListener(
    private val onScroll: OnScroll
) : RecyclerView.OnScrollListener() {

    private val scrollHandler = Handler(Looper.getMainLooper())
    private val scrollRunnable = Runnable {
        onScroll.onScrollStopped()
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            scrollHandler.removeCallbacks(scrollRunnable)
            scrollHandler.postDelayed(scrollRunnable, 100)
        } else {
            scrollHandler.removeCallbacks(scrollRunnable)
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val canScrollUp = recyclerView.canScrollVertically(-1)
        onScroll.onScrolled(canScrollUp)
    }

    interface OnScroll {
        fun onScrollStopped() {}
        fun onScrolled(canScrollUp: Boolean) {}
    }
}