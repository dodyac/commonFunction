package com.acdev.commonFunction.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.acdev.commonFunction.common.InflateFr
import com.acdev.commonFunction.util.Functionx.Companion.useCurrentTheme

abstract class BaseAdapter<VB : ViewBinding>(private val InflateFr: InflateFr<VB>, private val list: List<Any>) :
    RecyclerView.Adapter<BaseAdapter.ViewHolder>() {

    private var _binding: VB? = null
    val binding get() = _binding!!
    val gone: Int = View.GONE
    val visible: Int = View.VISIBLE
    val invisible: Int = View.INVISIBLE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        parent.context.useCurrentTheme()
        _binding = InflateFr.invoke((parent.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}