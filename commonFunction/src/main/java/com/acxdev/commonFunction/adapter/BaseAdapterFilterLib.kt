package com.acxdev.commonFunction.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.InflateViewGroup
import com.acxdev.commonFunction.util.Diff
import com.acxdev.commonFunction.util.ext.useCurrentTheme

abstract class BaseAdapterFilterLib<VB : ViewBinding, T>(
    private val inflateViewGroup: InflateViewGroup<VB>,
    private val list: List<T>,
    private val onFilter: OnFilter<T>?
) :
    RecyclerView.Adapter<BaseAdapterFilterLib.ViewHolder<VB>>(), Filterable {

    protected val listFilter = list.toMutableList()
    protected lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        context = parent.context
        context.useCurrentTheme()
        val binding = inflateViewGroup.invoke(
            (parent.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<VB>, position: Int) {
        val item = listFilter[position]

        holder.bind(item)
    }

    override fun getItemCount() = listFilter.size

    class ViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    protected abstract fun ViewHolder<VB>.bind(item: T)

    open fun ViewHolder<VB>.scopeLayout(viewBinding: (VB.() -> Unit)) {
        try {
            viewBinding.invoke(binding)
        } catch (e: Exception) {
            println("${javaClass.simpleName} was destroyed")
            e.printStackTrace()
        }
    }

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val iList = mutableListOf<T>()
            val query = constraint.toString().lowercase()
            if (query.isEmpty()) iList.addAll(list)
            else list.forEach {
                if (filterBy(it, query)) {
                    iList.add(it)
                }
            }
            val filterResults = FilterResults()
            filterResults.values = iList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            @Suppress("UNCHECKED_CAST")
            val result = results.values as List<T>
            val finalList = result.distinct()
            updateItem(finalList)
            onFilter?.onFilteredResult(finalList)
        }
    }

    fun updateItem(newList: List<T>) {
        val diff = Diff(listFilter, newList)
        val diffResult = DiffUtil.calculateDiff(diff)

        listFilter.clear()
        listFilter.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    protected abstract fun <T>filterBy(item: T, query: String): Boolean

    interface OnFilter<T> {
        fun onFilteredResult(list: List<T>)
    }
    
    interface OnClick<T> {
        fun onItemClick(item: T, position: Int)
    }
}