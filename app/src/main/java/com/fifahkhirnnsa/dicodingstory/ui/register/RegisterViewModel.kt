package com.fifahkhirnnsa.dicodingstory.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fifahkhirnnsa.dicodingstory.data.Result
import com.fifahkhirnnsa.dicodingstory.data.UserRepository
import com.fifahkhirnnsa.dicodingstory.data.response.RegisterResponse
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> get() = _registerResult

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerResult.value = Result.Loading
            val result = repository.register(name, email, password)
            _registerResult.value = result
        }
    }
}