package com.example.kplogbook.di

import android.content.Context
import com.example.kplogbook.data.UserRepository
import com.example.kplogbook.data.pref.UserPreference
import com.example.kplogbook.data.pref.dataStore
import com.example.kplogbook.data.remote.ApiConfig


    object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()

        return UserRepository.getInstance(apiService, pref)

    }
}