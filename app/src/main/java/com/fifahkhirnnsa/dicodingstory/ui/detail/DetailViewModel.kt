package com.fifahkhirnnsa.dicodingstory.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fifahkhirnnsa.dicodingstory.data.database.ListItemStory

class DetailViewModel : ViewModel() {
    private val _storyDetail = MutableLiveData<ListItemStory?>()
    val storyDetail: LiveData<ListItemStory?> = _storyDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setStoryDetail(story: ListItemStory?) {
        _isLoading.value = true
        _storyDetail.value = story
        _isLoading.value = false
    }
}
