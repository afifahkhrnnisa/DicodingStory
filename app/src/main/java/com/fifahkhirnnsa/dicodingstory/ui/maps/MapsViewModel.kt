package com.fifahkhirnnsa.dicodingstory.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fifahkhirnnsa.dicodingstory.data.UserRepository
import com.fifahkhirnnsa.dicodingstory.data.response.ListStoryItem
import com.fifahkhirnnsa.dicodingstory.data.Result
import kotlinx.coroutines.launch

class MapsViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _stories = MutableLiveData<Result<List<ListStoryItem>>>()
    val stories: LiveData<Result<List<ListStoryItem>>> = _stories

    fun getStoriesLocation() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = userRepository.getStoriesLocation()

                if (response is Result.Success) {
                    _stories.value = Result.Success(response.data.listStory)
                } else {
                    _stories.value = Result.Error("Error getting stories.")
                }
            } catch (e: Exception) {
                _stories.value = Result.Error(e.message ?: "An unknown error occurred")
            } finally {
                _isLoading.value = false
            }
        }
    }

}
