package com.acxdev.commonFunction.util.ext.view

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class SwipeRefreshLayoutLib {
    companion object {
        fun SwipeRefreshLayout.set(action: () -> Unit) {
            action.invoke()
            setOnRefreshListener {
                postDelayed({
                    isRefreshing = false
                    action.invoke()
                }, 2)
            }
        }
    }
}