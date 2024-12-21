package com.fifahkhirnnsa.dicodingstory.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.fifahkhirnnsa.dicodingstory.data.database.ListItemStory
import com.fifahkhirnnsa.dicodingstory.data.database.StoryDatabase
import com.fifahkhirnnsa.dicodingstory.data.pref.UserModel
import com.fifahkhirnnsa.dicodingstory.data.pref.UserPreference
import com.fifahkhirnnsa.dicodingstory.data.response.ErrorResponse
import com.fifahkhirnnsa.dicodingstory.data.response.FileUploadResponse
import com.fifahkhirnnsa.dicodingstory.data.response.ListStoryResponse
import com.fifahkhirnnsa.dicodingstory.data.response.LoginResponse
import com.fifahkhirnnsa.dicodingstory.data.response.RegisterResponse
import com.fifahkhirnnsa.dicodingstory.data.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(email, password)
                Result.Success(response)
            } catch (e: HttpException) {
                val errorMessage = parseErrorMessage(e)
                Result.Error(errorMessage)
            } catch (e: IOException) {
                Result.Error("Network connection error. Please check your connection.")
            }
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.register(name, email, password)
                Result.Success(response)
            } catch (e: HttpException) {
                val errorMessage = parseErrorMessage(e)
                Result.Error(errorMessage)
            } catch (e: IOException) {
                Result.Error("Network connection error. Please check your connection.")
            }
        }
    }

    private fun parseErrorMessage(e: HttpException): String {
        return try {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            errorBody.message ?: "Unknown error"
        } catch (exception: Exception) {
            "Unknown error"
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getStories(): LiveData<PagingData<ListItemStory>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    suspend fun uploadImage(file: MultipartBody.Part, description: RequestBody, lat: RequestBody?, lon: RequestBody?): Result<FileUploadResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.uploadImage(file, description, lat, lon)
                Result.Success(response)
            } catch (e: HttpException) {
                val error = e.response()?.errorBody()?.string()
                val errorMessage = Gson().fromJson(error, FileUploadResponse::class.java)
                Result.Error(errorMessage.message)
            } catch (e: IOException) {
                Result.Error("Network connection error. Please check your connection.")
            }
        }
    }

    suspend fun getStoriesLocation(location: Int = 1): Result<ListStoryResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val successResponse = apiService.getStoriesLocation(location)
                Result.Success(successResponse)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = Gson().fromJson(errorBody, ListStoryResponse::class.java)
                Result.Error(errorMessage.message ?: "Unknown Error")
            } catch (e: IOException) {
                Result.Error("Network connection error. Please check your connection.")
            }
        }
    }

    companion object {
        fun getInstance(
            userPreference: UserPreference,
            storyDatabase: StoryDatabase,
            apiService: ApiService
        ) = UserRepository(userPreference, storyDatabase, apiService)
    }
}