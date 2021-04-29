package com.acdev.commonFunction.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.acdev.commonFunction.common.InflateFr
import com.acdev.commonFunction.util.Functionx.Companion.useCurrentTheme

abstract class BaseAdapterNew<VB : ViewBinding>(private val InflateFr: InflateFr<VB>, private val list: List<Any>) :
    RecyclerView.Adapter<BaseAdapterNew.ViewHolder<VB>>() {

    val gone: Int = View.GONE
    val visible: Int = View.VISIBLE
    val invisible: Int = View.INVISIBLE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        parent.context.useCurrentTheme()
        val binding = InflateFr.invoke((parent.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    class ViewHolder<VB: ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}