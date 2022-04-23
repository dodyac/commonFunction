package com.acxdev.commonFunction.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.InflateViewGroup
import com.acxdev.commonFunction.util.ext.useCurrentTheme
import com.smarteist.autoimageslider.SliderViewAdapter

abstract class BaseSlider<VB : ViewBinding>(
    private val inflateViewGroup: InflateViewGroup<VB>,
    private val list: List<Any>
) :
    SliderViewAdapter<BaseSlider.ViewHolder<VB>>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder<VB>? {
        parent.context.useCurrentTheme()
        context = parent.context
        val binding = inflateViewGroup.invoke(
            (parent.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getCount() = list.size

    class ViewHolder<VB : ViewBinding>(val binding: VB) : SliderViewAdapter.ViewHolder(binding.root)

    interface OnClick<T> {
        fun onItemClick(item: T, position: Int)
    }
}