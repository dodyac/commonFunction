package com.acxdev.commonFunction.util.view

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acxdev.commonFunction.util.FunctionX.Companion.getCompatActivity
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.smarteist.autoimageslider.SliderViewAdapter

class RecyclerViewX {
    companion object{

        fun RecyclerView.adapter(adapter: RecyclerView.Adapter<*>?, spanCount: Int) {
            val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context.getCompatActivity(), spanCount)
            this.layoutManager = layoutManager
            this.adapter = adapter
            adapter?.notifyDataSetChanged()
        }

        fun RecyclerView.adapterGrid(adapter: RecyclerView.Adapter<*>?, numOfColumns: Float) {
            val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context.getCompatActivity()!!, context.getCompatActivity()!!.numOfColumns(numOfColumns))
            this.layoutManager = layoutManager
            this.adapter = adapter
            adapter?.notifyDataSetChanged()
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