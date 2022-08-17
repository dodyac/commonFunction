package com.acxdev.commonFunction.widget

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.common.InflateViewGroup
import com.acxdev.commonFunction.util.ext.view.setHStack

class RowTabLayout<VB : ViewBinding, T>(
    list: MutableList<T>,
    inflateViewGroup: InflateViewGroup<VB>,
    private val onBehavior: OnBehavior<VB, T>) :
    BaseAdapterLib<VB, T>(inflateViewGroup, list) {

    private var selected = 0

    override fun ViewHolder<VB>.bind(item: T) {
        scopeLayout {
            onBehavior.onLayout(this, item,selected == adapterPosition)

            root.setOnClickListener {
                if (selected >= 0) {
                    notifyItemChanged(selected)
                }
                selected = adapterPosition
                notifyItemChanged(selected)
                onBehavior.onItemClick(item)
            }
        }
    }

    interface OnBehavior<VB, T> {
        fun onItemClick(item: T)
        fun onLayout(binding: VB, item: T, isSelected: Boolean)
    }
}

fun <VB : ViewBinding, T>RecyclerView.asTabLayoutWith(
    menuList: MutableList<T>,
    inflateViewGroup: InflateViewGroup<VB>,
    onLayout: ((VB, T, Boolean) -> Unit)
) {
    setHStack(RowTabLayout(menuList, inflateViewGroup, object : RowTabLayout.OnBehavior<VB, T> {
        override fun onItemClick(item: T) {
        }

        override fun onLayout(binding: VB, item: T, isSelected: Boolean) {
            onLayout.invoke(binding, item, isSelected)
        }

    }), hasFixed = true)
}