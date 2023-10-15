package com.acxdev.usefulmethod

import androidx.lifecycle.viewModelScope
import com.acxdev.commonFunction.common.base.BaseViewModel
import com.acxdev.commonFunction.model.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TestViewModel : BaseViewModel() {

    private val _stateFlow = MutableStateFlow<BaseResponse<List<User>>>(BaseResponse.Load)
    val stateFlow = _stateFlow.asStateFlow()

    init {
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            safeApiCall({
                Api.fetch().getUsers()
            }) {
                _stateFlow.value = this
            }
        }
    }
}