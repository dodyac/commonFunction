package com.acxdev.commonFunction.common

import android.content.res.Configuration
import android.view.ContextThemeWrapper
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
        val inflateMethod = bindingClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        return inflateMethod.invoke(null, inflater, container, false) as VB
    }

    fun <VB: ViewBinding> RecyclerView.Adapter<ViewHolder<VB>>.inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB {
        val bindingClass = javaClass.getClass<VB>()
        val inflateMethod = bindingClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        return inflateMethod.invoke(null, inflater, container, false) as VB
    }

    fun ViewGroup.getInflaterBasedOnTheme(
        isDarkMode: Boolean,
        theme: Int
    ): LayoutInflater {
        val nightMode = if (isDarkMode) {
            Configuration.UI_MODE_NIGHT_YES
        } else {
            Configuration.UI_MODE_NIGHT_NO
        }

        val config = Configuration(context.resources.configuration).apply {
            uiMode = (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or nightMode
        }
        val configContext = context.createConfigurationContext(config)

        val themedContext = ContextThemeWrapper(configContext, theme)
        val inflater = LayoutInflater.from(themedContext)
        return inflater
    }
}

