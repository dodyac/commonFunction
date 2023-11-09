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
import com.acxdev.commonFunction.common.Inflater.inflateBinding
import com.acxdev.commonFunction.model.ViewHolder
import com.acxdev.commonFunction.utils.MutableListDelegate

abstract class BaseAdapter<VB : ViewBinding, T>(
    private val adapterListener: AdapterListener<T>? = null
) : RecyclerView.Adapter<ViewHolder<VB>>(), Filterable {

    var currentList = emptyList<T>()
    private val listFilter = MutableListDelegate(list = currentList.toMutableList()) {
        adapterListener?.onListChanged(isEmpty())
    }

    private var shimmerLoadedListener: ShimmerLoadedListener? = null

    protected lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        context = parent.context
        val inflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = inflateBinding(inflater, parent)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<VB>, position: Int) {
        val item = listFilter[position]

        holder.binding.setViews(item, holder.adapterPosition)
    }

    override fun getItemCount(): Int {
        return listFilter.size
    }

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val query = constraint.toString().lowercase()
            val iList = mutableListOf<T>()
            if (query.isEmpty()) {
                iList.addAll(currentList)
                currentList
            } else currentList.forEach {
                if (filterBy(it, query)) {
                    iList.add(it)
                }
            }
            val filterResults = FilterResults()
            filterResults.values = iList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            val result = results.values as List<T>
            val finalList = result.distinct()
            setFilteredItems(finalList)
            adapterListener?.onFilteredResult(finalList)
        }
    }

    protected abstract fun VB.setViews(item: T, position: Int)

    protected open fun filterBy(item: T, query: String): Boolean {
        return true
    }

    fun setAdapterList(newList: List<T>) {
        currentList = newList
        setFilteredItems(currentList)
        shimmerLoadedListener?.onAdapterListSet()
    }

    fun setShimmerLoadedListener(shimmerLoadedListener: ShimmerLoadedListener) {
        this.shimmerLoadedListener = shimmerLoadedListener
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

    interface ShimmerLoadedListener {
        fun onAdapterListSet()
    }

    interface AdapterListener<T> {
        fun onFilteredResult(list: List<T>) {}
        fun onListChanged(isEmpty: Boolean) {}
    }
}