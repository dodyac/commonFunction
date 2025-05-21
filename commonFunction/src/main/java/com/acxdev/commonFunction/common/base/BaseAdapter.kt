package com.acxdev.commonFunction.common.base

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.Inflater.inflateBinding
import com.acxdev.commonFunction.model.ViewHolder

abstract class BaseAdapter<VB : ViewBinding, T>(
    private val adapterListener: AdapterListener<T>? = null
) : RecyclerView.Adapter<ViewHolder<VB>>(), Filterable {

    var currentList = emptyList<T>()
    protected val listFilter = MutableListDelegate<T> {
        adapterListener?.onListChanged(
            size = size,
            isEmpty = isEmpty()
        )
    }

    private var shimmerLoadedListener: ShimmerLoadedListener? = null

    protected lateinit var context: Context
    protected val stateMap by lazy {
        StateMap()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<VB> {
        context = parent.context
        val inflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = inflateBinding(inflater, parent)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder<VB>,
        position: Int
    ) {
        val item = listFilter[position]

        holder.binding.setViews(item, holder.adapterPosition)
    }

    override fun getItemCount(): Int {
        return listFilter.size
    }

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val query = constraint.toString().lowercase()
            val iList = if (query.isEmpty()) {
                currentList
            } else {
                val list = mutableListOf<T>()
                currentList.forEach {
                    val filtered = it.filterBy()

                    if (filtered.isNotEmpty()) {
                        val isExist = filtered.any { string ->
                            string.lowercase().contains(query)
                        }

                        if (isExist) {
                            list.add(it)
                        }
                    } else {
                        list.add(it)
                    }
                }
                list
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

    protected abstract fun VB.setViews(
        item: T,
        position: Int
    )

    fun filter(charSequence: CharSequence?) {
        filter.filter(charSequence)
    }

    protected open fun T.filterBy(): List<String> {
        return emptyList()
    }

    protected fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    protected fun getString(
        @StringRes resId: Int,
        vararg formatArgs: Any
    ): String {
        return context.getString(resId, formatArgs)
    }

    protected fun getColor(@ColorRes resId: Int): Int {
       return context.getColor(resId)
    }

    fun setAdapterList(newList: List<T>) {
        currentList = newList
        setFilteredItems(currentList)
        shimmerLoadedListener?.onAdapterListSet()
    }

    fun setShimmerLoadedListener(
        shimmerLoadedListener: ShimmerLoadedListener
    ) {
        this.shimmerLoadedListener = shimmerLoadedListener
    }

    private fun setFilteredItems(newList: List<T>) {
        val diff = Diff(listFilter, newList)
        val diffResult = DiffUtil.calculateDiff(diff)

        listFilter.clear()
        listFilter.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class Diff<T>(
        private val oldList: List<T>,
        private val newList: List<T>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItemPosition == newItemPosition

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }

    inner class StateMap {
        private val stateMap: MutableMap<Int, Boolean> = mutableMapOf()

        fun toggleState(position: Int) {
            val currentState = stateMap[position] ?: false
            stateMap[position] = !currentState
        }

        fun getState(position: Int): Boolean {
            return stateMap[position] == true
        }

        fun clearState() {
            stateMap.clear()
        }
    }

    inner class MutableListDelegate<T>(
        private val list: MutableList<T> = mutableListOf(),
        private val listener: MutableList<T>.() -> Unit
    ) : MutableList<T> by list {

        override fun addAll(elements: Collection<T>): Boolean {
            list.addAll(elements).also {
                listener.invoke(list)
            }
            return true
        }
    }

    interface ShimmerLoadedListener {
        fun onAdapterListSet()
    }

    interface AdapterListener<T> {
        fun onFilteredResult(list: List<T>) {}
        fun onListChanged(
            size: Int,
            isEmpty: Boolean
        ) {}
    }
}