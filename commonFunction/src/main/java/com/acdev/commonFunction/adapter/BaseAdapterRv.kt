package com.acdev.commonFunction.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.acdev.commonFunction.util.Function.Companion.useCurrentTheme

abstract class BaseAdapterRv(private val list: List<Any>, @LayoutRes val layoutRes: Int)
    : RecyclerView.Adapter<BaseAdapterRv.ViewHolder>() {

    val gone: Int = View.GONE
    val visible: Int = View.VISIBLE
    val invisible: Int = View.INVISIBLE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        parent.context.useCurrentTheme()
        return ViewHolder((parent.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(layoutRes, parent, false))
    }

    override fun getItemCount() = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}