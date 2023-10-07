package com.acxdev.commonFunction.widget

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.base.BaseAdapter
import com.acxdev.commonFunction.model.MultiChoice

class AdapterSelectable<VB : ViewBinding> (
    list: List<MultiChoice>,
    private val onLayout: VB.(Boolean, Any) -> Unit
) : BaseAdapter<VB, MultiChoice>(list) {

    override fun VB.configureViews(item: MultiChoice, position: Int) {
        onLayout.invoke(this, item.isChecked, item.data)

        root.apply {
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT

            setOnClickListener {
                item.isChecked = !item.isChecked
                notifyItemChanged(position)
            }
        }
    }
}