package com.acdev.commonFunction.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.smarteist.autoimageslider.SliderViewAdapter

abstract class BaseSlider(private val list: List<Any>, @LayoutRes val layoutRes: Int) :
    SliderViewAdapter<BaseSlider.ViewHolder>() {

    val gone: Int = View.GONE
    val visible: Int = View.VISIBLE
    val invisible: Int = View.INVISIBLE

    override fun getCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder? =
        ViewHolder(LayoutInflater.from(parent?.context).inflate(layoutRes, parent, false))

    class ViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView)
}