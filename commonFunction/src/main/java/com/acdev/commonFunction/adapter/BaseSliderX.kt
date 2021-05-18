package com.acdev.commonFunction.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.acdev.commonFunction.common.InflateFr
import com.acdev.commonFunction.util.Functionx.Companion.useCurrentTheme
import com.smarteist.autoimageslider.SliderViewAdapter

abstract class BaseSliderX<VB : ViewBinding>(private val InflateFr: InflateFr<VB>, private val list: List<Any>) :
    SliderViewAdapter<BaseSliderX.ViewHolder>() {

    private var _binding: VB? = null
    val binding get() = _binding!!
    val gone: Int = View.GONE
    val visible: Int = View.VISIBLE
    val invisible: Int = View.INVISIBLE
    lateinit var context: Context

    override fun getCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder? {
        parent.context.useCurrentTheme()
        context = parent.context
        _binding = InflateFr.invoke((parent.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater), parent, false)
        return ViewHolder(binding.root)
    }

    class ViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView)
}