package com.acdev.commonFunction.util

import android.content.Context
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class Refresh {
    class Listener(context: Context) : SwipeRefreshLayout(context) {
        private var action: ((OnRefreshListener) -> Unit)? = null

        override fun setOnRefreshListener(listener: OnRefreshListener?) { action?.invoke(listener!!) }
    }

    companion object {
        fun SwipeRefreshLayout?.set(context: Context?, action: Listener.() -> Unit) {
            val listen = Listener(context!!)
            action.invoke(listen)
            this?.setOnRefreshListener {
                postDelayed({
                    isRefreshing = false
                    action.invoke(listen)
                }, 2)
            }
        }
    }
}