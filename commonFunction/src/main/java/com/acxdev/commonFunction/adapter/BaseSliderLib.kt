package com.acxdev.commonFunction.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.InflateViewGroup
import com.acxdev.commonFunction.util.ext.useCurrentTheme
import com.smarteist.autoimageslider.SliderViewAdapter

abstract class BaseSliderLib<VB : ViewBinding, T>(
    private val inflateViewGroup: InflateViewGroup<VB>,
    private val list: List<T>
) :
    SliderViewAdapter<BaseSliderLib.ViewHolder<VB>>() {

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder<VB>? {
        parent.context.useCurrentTheme()
        context = parent.context
        _binding = inflateViewGroup.invoke(
            (parent.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getCount() = list.size

    class ViewHolder<VB : ViewBinding>(binding: VB) : SliderViewAdapter.ViewHolder(binding.root)

    open fun scopeLayout(viewBinding: (VB.() -> Unit)) {
        viewBinding.invoke(binding)
    }

    interface OnClick<T> {
        fun onItemClick(item: T, position: Int)
    }
}