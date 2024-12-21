package com.fifahkhirnnsa.dicodingstory.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fifahkhirnnsa.dicodingstory.data.Result
import com.fifahkhirnnsa.dicodingstory.data.UserRepository
import com.fifahkhirnnsa.dicodingstory.data.pref.UserModel
import com.fifahkhirnnsa.dicodingstory.data.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun login(email: String, password: String) {
        _loginResult.value = Result.Loading
        viewModelScope.launch {
            val result = userRepository.login(email, password)
            _loginResult.value = result
            if (result is Result.Success) {
                saveSession(result.data)
            }
        }
    }

    private fun saveSession(loginResponse: LoginResponse) {
        viewModelScope.launch {
            val user = UserModel(
                email = loginResponse.loginResult.name,
                token = loginResponse.loginResult.token,
                isLogin = true
            )
            userRepository.saveSession(user)
        }
    }
}
