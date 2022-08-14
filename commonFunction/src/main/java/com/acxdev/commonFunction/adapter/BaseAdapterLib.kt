package com.acxdev.commonFunction.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.InflateViewGroup
import com.acxdev.commonFunction.util.Diff
import com.acxdev.commonFunction.util.ext.useCurrentTheme

abstract class BaseAdapterLib<VB : ViewBinding, T>(
    private val inflateViewGroup: InflateViewGroup<VB>,
    private val list: MutableList<T>
) :
    RecyclerView.Adapter<BaseAdapterLib.ViewHolder<VB>>() {

    protected lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        context = parent.context
        context.useCurrentTheme()
        val binding = inflateViewGroup.invoke(
            (parent.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    class ViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    open fun ViewHolder<VB>.scopeLayout(viewBinding: (VB.() -> Unit)) {
        viewBinding.invoke(binding)
    }

    fun updateItem(newList: List<T>) {
        val diff = Diff(list, newList)
        val diffResult = DiffUtil.calculateDiff(diff)

        list.clear()
        list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    interface OnClick<T> {
        fun onItemClick(item: T, position: Int)
    }
}