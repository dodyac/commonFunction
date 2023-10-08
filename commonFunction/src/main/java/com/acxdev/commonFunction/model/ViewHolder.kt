package com.acxdev.commonFunction.model

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class ViewHolder<VB : ViewBinding>(
    val binding: VB
) : RecyclerView.ViewHolder(binding.root)