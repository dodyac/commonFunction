package com.acxdev.commonFunction.common.base

import android.content.Context
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.Inflater.getInflaterBasedOnTheme
import com.acxdev.commonFunction.common.Inflater.inflateBinding
import com.acxdev.commonFunction.model.ViewHolder
import java.lang.ref.WeakReference

abstract class BaseAdapter2<VB : ViewBinding, T>(
    adapterListener: AdapterListener<T>? = null
) : RecyclerView.Adapter<ViewHolder<VB>>(), Filterable {

    // Use weak reference for listener
    private val adapterListenerRef = WeakReference(adapterListener)

    //full list
    var currentList = emptyList<T>()
    //showed
    protected val filteredList = MutableListDelegate<T> {
        adapterListenerRef.get()?.onListChanged(
            size = size,
            isEmpty = isEmpty()
        )
    }

    // State management using stable IDs
    protected val stateMap = StateMap()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<VB> {
        val inflater = parent.getInflaterBasedOnTheme(
            isDarkMode,
            theme
        )
        val binding = inflateBinding(inflater, parent)
        return ViewHolder(binding).also {
            it.binding.onCreate(parent.context, it.adapterPosition)
        }
    }

    override fun onBindViewHolder(
        holder: ViewHolder<VB>,
        position: Int
    ) {
        val context = holder.itemView.context
        val item = filteredList[position]

        holder.binding.onBind(context, item, holder.adapterPosition)
    }

    override fun getItemCount(): Int {
        return filteredList.size
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
            adapterListenerRef.get()?.onFilteredResult(finalList)
        }
    }

    protected open fun VB.onCreate(
        context: Context,
        position: Int
    ) {}

    protected abstract fun VB.onBind(
        context: Context,
        item: T,
        position: Int
    )

    protected abstract val isDarkMode: Boolean
    protected abstract val theme: Int

    fun filter(charSequence: CharSequence?) {
        filter.filter(charSequence)
    }

    protected open fun T.filterBy(): List<String> {
        return emptyList()
    }

    protected fun VB.getString(@StringRes resId: Int): String {
        return root.context.getString(resId)
    }

    protected fun VB.getString(
        @StringRes resId: Int,
        vararg formatArgs: Any
    ): String {
        return root.context.getString(resId, formatArgs)
    }

    protected fun VB.getColor(@ColorRes resId: Int): Int {
       return root.context.getColor(resId)
    }

    fun setAdapterList(newList: List<T>) {
        currentList = newList
        setFilteredItems(currentList)
    }

    private fun setFilteredItems(newList: List<T>) {
        val diff = Diff(filteredList, newList)
        val diffResult = DiffUtil.calculateDiff(diff)

        filteredList.clear()
        filteredList.addAll(newList)
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

    interface AdapterListener<T> {
        fun onFilteredResult(list: List<T>) {}
        fun onListChanged(
            size: Int,
            isEmpty: Boolean
        ) {}
    }
}