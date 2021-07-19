package com.acxdev.commonFunction.adapter

import android.app.Activity
import android.content.Context
import android.database.MatrixCursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.InflateFr
import com.acxdev.commonFunction.databinding.LayoutShimmerBinding
import com.acxdev.commonFunction.util.Diff
import com.acxdev.commonFunction.util.FunctionX.Companion.getView
import com.acxdev.commonFunction.util.FunctionX.Companion.useCurrentTheme

abstract class BaseAdapter<VB : ViewBinding, T>(private val InflateFr: InflateFr<VB>, private val list: MutableList<T>) :
    RecyclerView.Adapter<BaseAdapter.ViewHolder<VB>>() {

    val gone: Int = View.GONE
    val visible: Int = View.VISIBLE
    val invisible: Int = View.INVISIBLE
    lateinit var context: Context
    lateinit var rootView: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        context = parent.context
        context.useCurrentTheme()
        rootView = context.getView()
        val binding = InflateFr.invoke((parent.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    class ViewHolder<VB: ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    fun updateItem(newList: List<T>){
        val diff = Diff(list, newList)
        val diffResult = DiffUtil.calculateDiff(diff)

        list.clear()
        list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    interface OnClick<T>{ fun onItemClick(item: T, position: Int) }
}