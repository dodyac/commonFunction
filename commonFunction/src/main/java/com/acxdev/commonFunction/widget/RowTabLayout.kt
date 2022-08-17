package com.acxdev.commonFunction.widget

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.common.InflateViewGroup
import com.acxdev.commonFunction.util.ext.view.setHStack

class RowTabLayout<VB : ViewBinding, T>(
    list: MutableList<T>,
    inflateViewGroup: InflateViewGroup<VB>,
    private val onLayout: OnLayout<VB, T>) :
    BaseAdapterLib<VB, T>(inflateViewGroup, list) {

    private var selected = 0

    override fun ViewHolder<VB>.bind(item: T) {
        scopeLayout {
            onLayout.onLoaded(this, item,selected == adapterPosition)

            root.setOnClickListener {
                if (selected >= 0) {
                    notifyItemChanged(selected)
                }
                selected = adapterPosition
                notifyItemChanged(selected)
            }
        }
    }

    interface OnLayout<VB, T> {
        fun onLoaded(binding: VB, item: T, isSelected: Boolean)
    }
}

fun <VB : ViewBinding, T>RecyclerView.asTabLayoutWith(
    menuList: MutableList<T>,
    inflateViewGroup: InflateViewGroup<VB>,
    onLayout: ((VB, T, Boolean) -> Unit)
) {
    setHStack(RowTabLayout(menuList, inflateViewGroup, object : RowTabLayout.OnLayout<VB, T> {
        override fun onLoaded(binding: VB, item: T, isSelected: Boolean) {
            onLayout.invoke(binding, item, isSelected)
        }

    }), hasFixed = true)
}