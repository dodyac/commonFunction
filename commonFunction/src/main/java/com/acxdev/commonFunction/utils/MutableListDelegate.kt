package com.acxdev.commonFunction.utils

class MutableListDelegate<T>(
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