package com.acdev.commonFunction

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikelau.views.shimmer.BindViewHolderPlugin
import com.mikelau.views.shimmer.ShimmerAdapter
import kotlinx.android.synthetic.main.layout_empty.view.*

class ShimmerRecycler : RecyclerView {

    enum class LayoutMangerType { LINEAR_VERTICAL, LINEAR_HORIZONTAL, GRID }

    private var mActualAdapter: Adapter<*>? = null
    private var mShimmerAdapter: ShimmerAdapter? = null
    private var mShimmerLayoutManager: LayoutManager? = null
    private var mActualLayoutManager: LayoutManager? = null
    private var mLayoutMangerType: LayoutMangerType? = null
    private var mCanScroll = false
    private var mLayoutReference = 0
    private var mGridCount = 0
    private var mShimmerAngle = 0
    private var mShimmerColor = 0
    private var mShimmerDuration = 0
    private var mShimmerMaskWidth = 0f
    private var isAnimationReversed = false
    private var mShimmerItemBackground: Drawable? = null
    private var emptyView: View? = null

    constructor(context: Context) : super(context) { init(context, null) }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init(context, attrs) }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) { init(context, attrs) }

    private fun init(context: Context, attrs: AttributeSet?) {
        mShimmerAdapter = ShimmerAdapter()
        val a = context.obtainStyledAttributes(attrs, R.styleable.ShimmerRecycler, 0, 0)
        try {
            layoutReference(a.getResourceId(R.styleable.ShimmerRecycler_layout, R.layout.layout_sample_view))
            childCount = a.getInteger(R.styleable.ShimmerRecycler_child_count, 10)
            a.getResourceId(R.styleable.ShimmerRecycler_empty_layout, 0)
            setGridChildCount(a.getInteger(R.styleable.ShimmerRecycler_grid_child_count, 2))
            when (a.getInteger(R.styleable.ShimmerRecycler_layout_manager, 0)) {
                0 -> layoutManager(LayoutMangerType.LINEAR_VERTICAL)
                1 -> layoutManager(LayoutMangerType.LINEAR_HORIZONTAL)
                2 -> layoutManager(LayoutMangerType.GRID)
                else -> throw IllegalArgumentException("This value for layout manager is not valid!")
            }
            mShimmerAngle = a.getInteger(R.styleable.ShimmerRecycler_angle, 0)
            mShimmerColor = a.getColor(R.styleable.ShimmerRecycler_color, getColor(R.color.color))
            mShimmerItemBackground = a.getDrawable(R.styleable.ShimmerRecycler_item_background)
            mShimmerDuration = a.getInteger(R.styleable.ShimmerRecycler_duration, 1500)
            mShimmerMaskWidth = a.getFloat(R.styleable.ShimmerRecycler_mask_width, 0.5f)
            isAnimationReversed = a.getBoolean(R.styleable.ShimmerRecycler_reverse_animation, false)
        } finally { a.recycle() }
        setupShimmerProperties()
    }

    private fun setupShimmerProperties() {
        mShimmerAdapter!!.setShimmerAngle(mShimmerAngle)
        mShimmerAdapter!!.setShimmerColor(mShimmerColor)
        mShimmerAdapter!!.setShimmerMaskWidth(mShimmerMaskWidth)
        mShimmerAdapter!!.setShimmerItemBackground(mShimmerItemBackground)
        mShimmerAdapter!!.setShimmerDuration(mShimmerDuration)
        mShimmerAdapter!!.setAnimationReversed(isAnimationReversed)
        showShimmer()
    }

    fun setBindViewHolderPlugin(plugin: BindViewHolderPlugin?) {
        mShimmerAdapter!!.setBindViewHolderPlugin(plugin)
    }

    fun setGridChildCount(count: Int) {
        mGridCount = count
    }

    fun layoutManager(type: LayoutMangerType?) {
        mLayoutMangerType = type
    }

    fun setChildCount(count: Int) {
        mShimmerAdapter!!.setMinItemCount(count)
    }

    fun duration(duration: Int) {
        mShimmerAdapter!!.setShimmerDuration(duration)
    }

     fun maskWidth(maskWidth: Float) {
        mShimmerAdapter!!.setShimmerMaskWidth(maskWidth)
    }

    fun showShimmer() {
        mCanScroll = false
        if (mShimmerLayoutManager == null) initShimmerManager()
        layoutManager = mShimmerLayoutManager
        adapter = mShimmerAdapter
    }

    fun hideShimmer() {
        mCanScroll = true
        layoutManager = mActualLayoutManager
        adapter = mActualAdapter
    }

    fun Context.setLayout(recyclerAdapter: Adapter<*>?, spanCount: Int) {
        val layoutManagerx: LayoutManager = GridLayoutManager(this, spanCount)
        layoutManager = layoutManagerx
        adapter = recyclerAdapter
        adapter?.notifyDataSetChanged()
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val view = emptyView
        if (view != null && adapter?.itemCount == 0) {
            view.measure(widthSpec, heightSpec)
            val width = view.measuredWidth
            val height = view.measuredHeight
            setMeasuredDimension(width, height)
            return
        }

        super.onMeasure(widthSpec, heightSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        emptyView?.layout(l, t, r, b)
    }

    override fun onDraw(c: Canvas) {
        super.onDraw(c)
        if (adapter?.itemCount == 0) {
            emptyView?.draw(c)
        }
    }


    @SuppressWarnings("unused")
    fun setEmptyView(view: View?) {
        emptyView = view

        invalidate()
    }

    /**
     * Set a view to show if the adapter is empty
     *
     * @param resId empty view's layout id
     */
    @SuppressWarnings("unused")
    fun setEmptyView(@LayoutRes resId: Int) {
        if (resId == 0) return

        val view = LayoutInflater.from(context).inflate(resId, null)
        setEmptyView(view)
    }


    fun setEmptyImage(layout: Int) {
        icon.setImageResource(layout)
    }

    fun setEmptyText(string: String) {
        text.text = string
    }

    fun setEmptyText(string: Int) {
        text.text = this.resources.getString(string)
    }

    override fun setLayoutManager(manager: LayoutManager?) {
        if (manager == null) mActualLayoutManager = null
        else if (manager !== mShimmerLayoutManager) mActualLayoutManager = manager
        super.setLayoutManager(manager)
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter == null) mActualAdapter = null
        else if (adapter !== mShimmerAdapter) mActualAdapter = adapter
        super.setAdapter(adapter)
    }

    fun getActualAdapter(): Adapter<*>? {
        return mActualAdapter
    }

    fun getShimmerAdapter(): Adapter<*>? {
        return mShimmerAdapter
    }

    fun getLayoutReference(): Int {
        return mLayoutReference
    }

    fun layoutReference(mLayoutReference: Int) {
        this.mLayoutReference = mLayoutReference
        mShimmerAdapter!!.setLayoutReference(getLayoutReference())
    }

    private fun initShimmerManager() {
        when (mLayoutMangerType) {
            LayoutMangerType.LINEAR_VERTICAL -> mShimmerLayoutManager = object : LinearLayoutManager(context) {
                    override fun canScrollVertically(): Boolean { return mCanScroll } }
            LayoutMangerType.LINEAR_HORIZONTAL -> mShimmerLayoutManager = object : LinearLayoutManager(
                    context, HORIZONTAL, false) {
                    override fun canScrollHorizontally(): Boolean { return mCanScroll } }
            LayoutMangerType.GRID -> mShimmerLayoutManager = object : GridLayoutManager(context, mGridCount) {
                    override fun canScrollVertically(): Boolean { return mCanScroll } }
        }
    }

    private fun getColor(id: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) context.getColor(id)
        else resources.getColor(id)
    }
}