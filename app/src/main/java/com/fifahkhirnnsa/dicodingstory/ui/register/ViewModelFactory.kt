package com.fifahkhirnnsa.dicodingstory.ui.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fifahkhirnnsa.dicodingstory.data.UserRepository
import com.fifahkhirnnsa.dicodingstory.di.Injection
import com.fifahkhirnnsa.dicodingstory.ui.detail.DetailViewModel
import com.fifahkhirnnsa.dicodingstory.ui.login.LoginViewModel
import com.fifahkhirnnsa.dicodingstory.ui.main.MainViewModel
import com.fifahkhirnnsa.dicodingstory.ui.maps.MapsViewModel
import com.fifahkhirnnsa.dicodingstory.ui.upload.UploadViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel() as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }
          
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        fun getInstance(context: Context) =
            ViewModelFactory(Injection.provideRepository(context))
    }
}
