package com.acxdev.commonFunction.widget.shimmer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acxdev.commonFunction.R
import com.acxdev.commonFunction.common.base.BaseAdapter

class ShimmerRecyclerView: RecyclerView {

    enum class LayoutMangerType {
        LINEAR_VERTICAL,
        LINEAR_HORIZONTAL,
        GRID
    }

    companion object {
        const val COUNT_DEFAULT = 3
        const val GRID_COUNT_DEFAULT = 2
        val SHIMMER_LAYOUT_DEFAULT = R.layout.row_shimmer_default
        val SHIMMER_LAYOUT_MANAGER_TYPE_DEFAULT = LayoutMangerType.LINEAR_VERTICAL
    }

    private var actualAdapter: BaseAdapter<*, *>? = null
    private var actualLayoutManager: LayoutManager? = null

    private var count: Int = COUNT_DEFAULT
    private var shimmerLayout: Int = SHIMMER_LAYOUT_DEFAULT
    private var gridCount: Int = GRID_COUNT_DEFAULT
    private var layoutMangerType: LayoutMangerType = SHIMMER_LAYOUT_MANAGER_TYPE_DEFAULT
    private var shimmerLayoutManager: LayoutManager? = null
    private var canScroll = false
    private var autoRun = true
    private var isShimmering = false

    constructor(context: Context) : super(context) {
        init(context, null)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShimmerRecyclerView)

        count = typedArray.getInt(R.styleable.ShimmerRecyclerView_count, COUNT_DEFAULT)
        shimmerLayout = typedArray.getResourceId(R.styleable.ShimmerRecyclerView_layout, SHIMMER_LAYOUT_DEFAULT)
        layoutMangerType = when(typedArray.getInteger(R.styleable.ShimmerRecyclerView_layoutType, 0)) {
            0 -> LayoutMangerType.LINEAR_VERTICAL
            1 -> LayoutMangerType.LINEAR_HORIZONTAL
            2 -> LayoutMangerType.GRID
            else -> LayoutMangerType.LINEAR_VERTICAL
        }
        gridCount = typedArray.getInteger(R.styleable.ShimmerRecyclerView_gridCount, GRID_COUNT_DEFAULT)
        autoRun = typedArray.getBoolean(R.styleable.ShimmerRecyclerView_autoRun, true)

        typedArray.recycle()

        if (autoRun) {
            showShimmer()
        }
    }

    private fun setAdapterListener() {
        actualAdapter?.setShimmerLoadedListener(object : BaseAdapter.ShimmerLoadedListener {
            override fun onAdapterListSet() {
                hideShimmer()
            }
        })
    }

    fun showShimmer() {
        canScroll = false
        isShimmering = true
        val shimmerAdapter = ShimmerAdapter()
        shimmerAdapter.layoutRes = shimmerLayout
        shimmerAdapter.count = count

        shimmerLayoutManager = when(layoutMangerType) {
            LayoutMangerType.LINEAR_VERTICAL -> {
                object : LinearLayoutManager(context) {
                    override fun canScrollVertically(): Boolean {
                        return canScroll
                    }
                }
            }
            LayoutMangerType.LINEAR_HORIZONTAL -> {
                object : LinearLayoutManager(context, HORIZONTAL, false) {
                    override fun canScrollHorizontally(): Boolean {
                        return canScroll
                    }
                }
            }
            LayoutMangerType.GRID -> {
                object : GridLayoutManager(context, gridCount) {
                    override fun canScrollVertically(): Boolean {
                        return canScroll
                    }
                }
            }
        }
        layoutManager = shimmerLayoutManager
        adapter = shimmerAdapter
    }

    fun hideShimmer() {
        canScroll = true
        isShimmering = false
        layoutManager = actualLayoutManager
        adapter = actualAdapter
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        if (layout == null) {
            actualLayoutManager = null
        } else if (layout != shimmerLayoutManager) {
            actualLayoutManager = layout
        }
        super.setLayoutManager(layout)
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter == null) {
            actualAdapter = null
        } else if (adapter !is ShimmerAdapter && adapter is BaseAdapter<*, *>) {
            actualAdapter = adapter
            setAdapterListener()
            if (isShimmering) {
                return
            }
        }
        super.setAdapter(adapter)
    }

    internal class ShimmerAdapter : Adapter<ViewHolder>() {
        var layoutRes = SHIMMER_LAYOUT_DEFAULT
        var count = COUNT_DEFAULT

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))
        }

        override fun getItemCount(): Int = count

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
    }
}