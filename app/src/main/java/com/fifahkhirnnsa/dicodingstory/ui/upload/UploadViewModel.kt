package com.fifahkhirnnsa.dicodingstory.ui.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fifahkhirnnsa.dicodingstory.data.Result
import com.fifahkhirnnsa.dicodingstory.data.UserRepository
import com.fifahkhirnnsa.dicodingstory.data.response.FileUploadResponse
import com.fifahkhirnnsa.dicodingstory.utils.EspressoIdlingResource
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _uploadResult = MutableLiveData<Result<FileUploadResponse>>()
    val uploadResult: LiveData<Result<FileUploadResponse>> = _uploadResult

    fun uploadImage(file: MultipartBody.Part, description: RequestBody, lat: RequestBody?, lon: RequestBody?) {
        viewModelScope.launch {
            try {
                EspressoIdlingResource.increment()
                _isLoading.value = true
                val result = repository.uploadImage(file, description, lat, lon)
                _uploadResult.value = result
            } catch (e: Exception) {
                _uploadResult.value = Result.Error(e.message ?: "An unknown error occurred")
            } finally {
                _isLoading.value = false
                EspressoIdlingResource.decrement()
            }
        }
    }
}