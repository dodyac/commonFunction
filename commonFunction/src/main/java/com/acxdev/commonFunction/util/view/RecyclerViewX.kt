package com.acxdev.commonFunction.util.view

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.acxdev.commonFunction.util.FunctionX.Companion.getCompatActivity
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.smarteist.autoimageslider.SliderViewAdapter

class RecyclerViewX {
    companion object{

        fun RecyclerView.adapter(adapter: RecyclerView.Adapter<*>?, isSnap: Boolean? = null, hasFixed: Boolean? = null) {
            layoutManager = LinearLayoutManager(context.getCompatActivity()!!, LinearLayoutManager.HORIZONTAL ,false)
            this.adapter = adapter
            if(isSnap == true){
                val snapHelper = LinearSnapHelper()
                if (onFlingListener == null) snapHelper.attachToRecyclerView(this)
            }
            if(hasFixed == true) setHasFixedSize(true)
        }

        fun RecyclerView.adapter(adapter: RecyclerView.Adapter<*>?, spanCount: Int, hasFixed: Boolean? = null) {
            this.layoutManager = GridLayoutManager(context.getCompatActivity(), spanCount)
            this.adapter = adapter
            if(hasFixed == true) setHasFixedSize(true)
        }

        fun RecyclerView.adapterGrid(adapter: RecyclerView.Adapter<*>?, numOfColumns: Float, hasFixed: Boolean? = null) {
            this.layoutManager = GridLayoutManager(context.getCompatActivity()!!, context.getCompatActivity()!!.numOfColumns(numOfColumns))
            this.adapter = adapter
            if(hasFixed == true) setHasFixedSize(true)
        }

        fun SliderView.adapter(sliderViewAdapter: SliderViewAdapter<*>){
            setSliderAdapter(sliderViewAdapter)
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            startAutoCycle()
        }

        private fun Context.numOfColumns(columnWidthDp: Float): Int {
            val displayMetrics = resources.displayMetrics
            val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
            return (screenWidthDp / columnWidthDp + 0.5).toInt()
        }
    }
}