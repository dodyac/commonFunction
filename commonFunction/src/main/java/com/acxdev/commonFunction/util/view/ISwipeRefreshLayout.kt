package com.acxdev.commonFunction.util.view

import android.content.Context
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class ISwipeRefreshLayout {
    class Listener(context: Context) : SwipeRefreshLayout(context) {
        private var action: ((OnRefreshListener) -> Unit)? = null

        override fun setOnRefreshListener(listener: OnRefreshListener?) { action?.invoke(listener!!) }
    }

    companion object {
        fun SwipeRefreshLayout.listener(action: Listener.() -> Unit) {
            val listen = Listener(context)
            action.invoke(listen)
            setOnRefreshListener {
                postDelayed({
                    isRefreshing = false
                    action.invoke(listen)
                }, 2)
            }
        }
    }
}