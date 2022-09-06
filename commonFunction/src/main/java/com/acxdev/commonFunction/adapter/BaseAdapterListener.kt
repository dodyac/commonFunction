package com.acxdev.commonFunction.adapter


interface OnFilter<T> {
    fun onFilteredResult(list: List<T>)
}

interface OnClick<T> {
    fun onItemClick(item: T, position: Int)
}