package com.acxdev.commonFunction.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.InflateFr
import com.acxdev.commonFunction.util.FunctionX.Companion.useCurrentTheme

abstract class BaseAdapter<VB : ViewBinding>(private val InflateFr: InflateFr<VB>, private val list: MutableList<*>) :
    RecyclerView.Adapter<BaseAdapter.ViewHolder<VB>>() {

    val gone: Int = View.GONE
    val visible: Int = View.VISIBLE
    val invisible: Int = View.INVISIBLE
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        parent.context.useCurrentTheme()
        context = parent.context
        val binding = InflateFr.invoke((parent.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    class ViewHolder<VB: ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    fun removeItem(position: Int){
        list.removeAt(position)
        notifyItemChanged(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    interface OnClick<T>{ fun onItemClick(item: T, position: Int) }
}