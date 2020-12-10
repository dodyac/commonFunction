package com.acdev.commonFunction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapterRv(private val lists: List<Any>) : RecyclerView.Adapter<BaseAdapterRv.ViewHolder>() {

    abstract fun layout(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(layout(), parent, false))

    override fun getItemCount() = lists.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}