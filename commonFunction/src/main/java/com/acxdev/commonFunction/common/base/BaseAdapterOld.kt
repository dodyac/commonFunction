package com.acxdev.commonFunction.common.base

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
import com.acxdev.commonFunction.model.ViewHolder

abstract class BaseAdapterOld<VB : ViewBinding, T>(
    private val inflateViewGroup: InflateViewGroup<VB>,
    list: List<T> = emptyList(),
    private val listener: Listener<T>? = null
) : RecyclerView.Adapter<ViewHolder<VB>>(), Filterable {

    var mList = list
    private val listFilter = mList.toMutableList()
    val TAG = javaClass.simpleName

    protected lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        context = parent.context
        val inflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = inflateViewGroup.invoke(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<VB>, position: Int) {
        val item = listFilter[position]

        holder.binding.configureViews(item, holder.adapterPosition)
    }

    override fun getItemCount(): Int {
        val count = listFilter.size
        listener?.onItemCount(count < 1)
        return count
    }

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val iList = mutableListOf<T>()
            val query = constraint.toString().lowercase()
            if (query.isEmpty()) iList.addAll(mList)
            else mList.forEach {
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
            setFilteredItems(finalList)
            listener?.onFilteredResult(finalList)
        }
    }

    protected abstract fun VB.configureViews(item: T, position: Int)

    protected open fun filterBy(item: T, query: String): Boolean {
        return true
    }

    fun setAdapterList(newList: List<T>) {
        mList = newList
        setFilteredItems(mList)
    }

    private fun setFilteredItems(newList: List<T>) {
        val diff = Diff(listFilter, newList)
        val diffResult = DiffUtil.calculateDiff(diff)

        listFilter.clear()
        listFilter.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class Diff<T>(private val oldList: List<T>, private val newList: List<T>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItemPosition == newItemPosition

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }

    interface Listener<T> {
        fun onItemClick(item: T, position: Int) {}
        fun onEditClick(item: T, position: Int) {}
        fun onDeleteClick(item: T, position: Int) {}
        fun onFilteredResult(list: List<T>) {}
        fun onItemCount(isEmpty: Boolean) {}
    }
}