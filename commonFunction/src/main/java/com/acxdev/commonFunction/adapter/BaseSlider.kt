package com.acxdev.commonFunction.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.Inflater.inflateBinding
import com.acxdev.commonFunction.util.ext.useCurrentTheme
import com.smarteist.autoimageslider.SliderViewAdapter

abstract class BaseSlider<VB : ViewBinding, T>(
    private val list: List<T>
) :
    SliderViewAdapter<BaseSlider.ViewHolder<VB>>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder<VB>? {
        parent.context.useCurrentTheme()
        context = parent.context
        val binding = inflateBinding(parent.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater, parent)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder<VB>, position: Int) {
        val item = list[position]

        viewHolder.bind(item)
    }

    override fun getCount() = list.size

    class ViewHolder<VB : ViewBinding>(val binding: VB) : SliderViewAdapter.ViewHolder(binding.root)

    protected abstract fun ViewHolder<VB>.bind(item: T)

    protected fun ViewHolder<VB>.scopeLayout(viewBinding: (VB.() -> Unit)) {
        try {
            viewBinding.invoke(binding)
        } catch (e: Exception) {
            println("${javaClass.simpleName} was destroyed")
            e.printStackTrace()
        }
    }
}