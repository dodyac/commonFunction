package com.acxdev.usefulmethod

import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.acxdev.commonFunction.common.base.BaseActivity
import com.acxdev.commonFunction.common.base.BaseAdapter
import com.acxdev.commonFunction.common.base.BaseDialog
import com.acxdev.commonFunction.common.base.BaseNetworking.whenLoaded
import com.acxdev.commonFunction.common.base.BaseNetworking.whenLoadedSuccess
import com.acxdev.commonFunction.model.ApiResponse
import com.acxdev.commonFunction.model.BaseResponse
import com.acxdev.commonFunction.utils.ImageLoader.setImageUrl
import com.acxdev.commonFunction.utils.ext.useCurrentTheme
import com.acxdev.commonFunction.utils.ext.view.NumericValidation
import com.acxdev.commonFunction.utils.ext.view.TilValidation
import com.acxdev.commonFunction.utils.ext.view.applyTo
import com.acxdev.commonFunction.utils.ext.view.backgroundTint
import com.acxdev.commonFunction.utils.ext.view.isNotValid
import com.acxdev.commonFunction.utils.ext.view.setVStack
import com.acxdev.commonFunction.utils.toast
import com.acxdev.usefulmethod.databinding.ActivityMainBinding
import com.acxdev.usefulmethod.databinding.DialogLoadingBinding
import com.acxdev.usefulmethod.databinding.RowTBinding
import kotlinx.coroutines.flow.collectLatest

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val testViewModel: TestViewModel by viewModels()
    private val adapterName by lazy {
        AdapterName(object : BaseAdapter.AdapterListener<User> {
            override fun onListChanged(
                size: Int,
                isEmpty: Boolean
            ) {
                binding.tvItemTotal.text = size.toString()
            }
        })
    }
    private val loading by lazy {
        Loading()
    }

    override fun doFetch() {
        useCoroutine()
//        useLoad()
    }

    override fun ActivityMainBinding.setViews() {
        useCurrentTheme()

        recycler.setVStack(adapterName)
        image.setImageUrl(
            "https://www.mast.com.mx/images/easyblog_articles/40/url.jpgz",
            errorDrawable = R.color.black
        )
    }

    override fun ActivityMainBinding.doAction() {
        listOf(
            TilValidation.NotEmpty,
            TilValidation.Numeric(
                numericValidation = NumericValidation.GreaterThan,
                value = 0.0
            ),
            TilValidation.Numeric(
                numericValidation = NumericValidation.LessThan,
                value = 20.0
            )
        ).applyTo(tilSearch)
        button.setOnClickListener {
//            if (isNotValid(tilSearch)) {
//                return@setOnClickListener
//            }
            if (tilSearch.isNotValid()) {
                return@setOnClickListener
            }
            toast("ok")
        }

        tilSearch.editText?.doOnTextChanged { text, _, _, _ ->
            adapterName.filter(text)
        }
    }

    private fun useCoroutine() {
        lifecycleScope.launchWhenStarted {
            testViewModel.stateFlow.collectLatest {
                it.whenSuccess {
                    adapterName.setAdapterList(this)
                }
            }
        }
    }

    private fun useLoadSuccess() {
        Api.fetch().getUsersMain().whenLoadedSuccess {
            adapterName.setAdapterList(this)
        }
    }

    private fun useLoad() {
        showLoading(true)
        Api.fetch().getUsersMain().whenLoaded {
            showLoading(false)
            whenSuccess {
                adapterName.setAdapterList(this)
            }
        }
    }

    private fun <T> BaseResponse<T>.whenSuccess(
        success: T.() -> Unit
    ) {
        when(this) {
            BaseResponse.Load -> {
                showLoading(true)
            }
            is BaseResponse.Success -> {
                showLoading(false)
                success.invoke(body)
            }
            is BaseResponse.Unsuccessful -> {
                showLoading(false)
                toast("Error Code $code")
            }
            is BaseResponse.Error -> {
                showLoading(false)
                toast(msg)
            }
        }
    }

    private fun <T> ApiResponse<T>.whenSuccess(
        success: T.() -> Unit
    ) {
        when(this) {
            is ApiResponse.Success -> {
                success.invoke(body)
            }
            is ApiResponse.Unsuccessful -> {
                toast("Error Code $code")
            }
            is ApiResponse.Error -> {
                toast(msg)
            }
        }
    }

    private fun showLoading(isShow: Boolean) {
        if (isShow) {
            loading.show(supportFragmentManager, loading.TAG)
        } else {
            supportFragmentManager.fragments.forEach {
                if (it.tag == loading.tag && it is Loading) {
                    it.dismiss()
                }
            }
        }
    }

    internal class Loading :BaseDialog<DialogLoadingBinding> () {
        override val canDismiss: Boolean
            get() = false
    }

    internal class AdapterName(
        adapterListener: AdapterListener<User>
    ): BaseAdapter<RowTBinding, User>(
        adapterListener
    ) {

        override fun RowTBinding.setViews(item: User, position: Int) {
            root.apply {
                backgroundTint(getColor(
                    if (stateMap.getState(position)) {
                        R.color.teal_700
                    } else {
                        //crash
//                        Color.parseColor("#FFFFFF")
                        R.color.black
                    }
                ))
                text = item.name
                setOnClickListener {
                    stateMap.toggleState(position)
                    notifyItemChanged(position)
                }
            }
        }

        override fun User.filterBy(): List<String> {
            return listOf(
                username,
                name,
                email
            )
        }
    }
}
