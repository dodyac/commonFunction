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
    private val list: List<T>
) :
    RecyclerView.Adapter<BaseAdapterFilterLib.ViewHolder<VB>>(), Filterable {

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    protected val listFilter = list.toMutableList()
    protected lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        context = parent.context
        context.useCurrentTheme()
        _binding = inflateViewGroup.invoke(
            (parent.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<VB>, position: Int) {
        val item = listFilter[position]

        configureViews(item, holder.adapterPosition)
    }

    override fun getItemCount() = listFilter.size

    class ViewHolder<VB : ViewBinding>(binding: VB) : RecyclerView.ViewHolder(binding.root)

    open fun scopeLayout(viewBinding: (VB.() -> Unit)) {
        viewBinding.invoke(binding)
    }

    abstract fun configureViews(item: T, position: Int)

    protected abstract val filterable: Filter

    override fun getFilter() = filterable

    fun updateItem(newList: List<T>) {
        val diff = Diff(listFilter, newList)
        val diffResult = DiffUtil.calculateDiff(diff)

        listFilter.clear()
        listFilter.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    protected fun myFilter(onFilter: OnFilter<T>): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val iList = mutableListOf<T>()
                val query = constraint.toString().lowercase()
                if (query.isEmpty()) iList.addAll(list)
                else for (item in list) {
                    onFilter.onPerformFiltering(item, iList, query)
                }
                val filterResults = FilterResults()
                filterResults.values = iList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                @Suppress("UNCHECKED_CAST")
                onFilter.onPublishResults(results.values as List<T>)
            }
        }
    }

    interface OnFilter<T> {
        fun onPerformFiltering(item: T, list: MutableList<T>, query: String)
        fun onPublishResults(list: List<T>)
    }

    interface OnBehavior<T> {
        fun onItemClick(item: T, position: Int)
        fun onFilter(results: List<T>)
    }
}