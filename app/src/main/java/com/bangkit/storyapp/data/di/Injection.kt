package com.bangkit.storyapp.data.di

import android.content.Context
import com.bangkit.storyapp.data.api.ApiConfig
import com.bangkit.storyapp.data.preference.UserPreference
import com.bangkit.storyapp.data.preference.dataStore
import com.bangkit.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {   fun provideRepository(context: Context): StoryRepository {
    val preferences = UserPreference.getInstance(context.dataStore)
    val user = runBlocking { preferences.getSession().first() }
    val apiService = ApiConfig.getApiService(user.token)
    return StoryRepository.getInstance(apiService, preferences)
}
}