package com.acxdev.commonFunction.utils

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecyclerViewPauseImageLoadOnScrollListener(
    pauseScrollConfiguration: ImageLoadingScrollConfiguration = ImageLoadingScrollConfiguration.PAUSE_ON_SETTLING,
    private val mExternalListener: RecyclerView.OnScrollListener?
) : RecyclerView.OnScrollListener() {
    private var stopped = false
    private var mPauseOnDragging = false
    private var pauseOnSettling = true

    init {
        when (pauseScrollConfiguration) {
            ImageLoadingScrollConfiguration.PAUSE_ON_SETTLING -> {
                mPauseOnDragging = false
                pauseOnSettling = true
            }
            ImageLoadingScrollConfiguration.PAUSE_ON_DRAGGING -> {
                mPauseOnDragging = true
                pauseOnSettling = false
            }
        }
    }

    companion object {
        @JvmStatic
        fun getPauseScrollListener(recyclerViewScrollListener: RecyclerView.OnScrollListener?): RecyclerView.OnScrollListener {
            return RecyclerViewPauseImageLoadOnScrollListener(ImageLoadingScrollConfiguration.PAUSE_ON_SETTLING, recyclerViewScrollListener)
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        mExternalListener?.onScrolled(recyclerView, dx, dy)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        when (newState) {
            RecyclerView.SCROLL_STATE_IDLE -> {
                Glide.with(recyclerView).resumeRequests()
                stopped = false
            }
            RecyclerView.SCROLL_STATE_DRAGGING -> {
                if (mPauseOnDragging) {
                    Glide.with(recyclerView).pauseRequests()
                    stopped = true
                } else if (stopped) {
                    Glide.with(recyclerView).resumeRequests()
                    stopped = false
                }
            }
            RecyclerView.SCROLL_STATE_SETTLING -> {
                if (pauseOnSettling) {
                    Glide.with(recyclerView).pauseRequests()
                    stopped = true
                } else if (stopped) {
                    Glide.with(recyclerView).resumeRequests()
                    stopped = false
                }
            }
        }
        mExternalListener?.onScrollStateChanged(recyclerView, newState)
    }

}
/*
* Please use this config for setting up the scroll listener as per your requirement.
* PAUSE_ON_DRAGGING - Use this if you want to pause image loading requests on user dragging the recyclerview.
* PAUSE_ON_SETTLING - Use this if you want to pause image loading requests on setting callback the recyclerview.
*/
enum class ImageLoadingScrollConfiguration {
    PAUSE_ON_DRAGGING,
    PAUSE_ON_SETTLING
}


fun RecyclerView.smoothScroll() {
    addOnScrollListener(RecyclerViewPauseImageLoadOnScrollListener.getPauseScrollListener(object : RecyclerView.OnScrollListener() {}))
}