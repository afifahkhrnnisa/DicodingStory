package com.fifahkhirnnsa.dicodingstory.di

import android.content.Context
import com.fifahkhirnnsa.dicodingstory.data.UserRepository
import com.fifahkhirnnsa.dicodingstory.data.database.StoryDatabase
import com.fifahkhirnnsa.dicodingstory.data.pref.UserPreference
import com.fifahkhirnnsa.dicodingstory.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val preference = UserPreference.getInstance(context)
        val user = runBlocking { preference.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val database = StoryDatabase.getDatabase(context)
        return UserRepository.getInstance(preference, database, apiService)
    }
}