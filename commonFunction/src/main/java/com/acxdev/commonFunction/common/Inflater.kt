package com.acxdev.commonFunction.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.model.ViewHolder
import java.lang.reflect.ParameterizedType

object Inflater {

    private fun <VB> Class<*>.getClass(): Class<VB> {
        return (genericSuperclass as? ParameterizedType)
            ?.actualTypeArguments
            ?.firstOrNull {
                it is Class<*> && ViewBinding::class.java.isAssignableFrom(it)
            } as? Class<VB>
            ?: throw IllegalArgumentException("Unable to determine ViewBinding class.")
    }

    fun <VB : ViewBinding> AppCompatActivity.inflateBinding(
        inflater: LayoutInflater
    ): VB {
        val bindingClass = javaClass.getClass<VB>()
        val inflateMethod = bindingClass.getMethod("inflate", LayoutInflater::class.java)
        return inflateMethod.invoke(null, inflater) as VB
    }

    fun <VB : ViewBinding> Fragment.inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB {
        val bindingClass = javaClass.getClass<VB>()
        val inflateMethod = bindingClass.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        return inflateMethod.invoke(null, inflater, container, false) as VB
    }

    fun <VB: ViewBinding> RecyclerView.Adapter<ViewHolder<VB>>.inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB {
        val bindingClass = javaClass.getClass<VB>()
        val inflateMethod = bindingClass.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        return inflateMethod.invoke(null, inflater, container, false) as VB
    }
}

