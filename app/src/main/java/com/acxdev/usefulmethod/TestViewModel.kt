package com.acxdev.usefulmethod

import androidx.lifecycle.viewModelScope
import com.acxdev.commonFunction.common.base.BaseViewModel
import com.acxdev.commonFunction.model.BaseResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TestViewModel : BaseViewModel() {

    private val _stateFlow = MutableStateFlow<BaseResponse<List<User>>>(BaseResponse.Load)
    val stateFlow = _stateFlow.asStateFlow()

    fun getUsers() {
        viewModelScope.launch {
            safeApiCall({
                Api.fetch().getUsers()
            }) {
                _stateFlow.value = this
            }
        }
    }
}