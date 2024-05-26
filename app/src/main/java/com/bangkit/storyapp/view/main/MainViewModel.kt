package com.bangkit.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.storyapp.data.api.response.ListStoryItem
import com.bangkit.storyapp.data.preference.UserModel
import com.bangkit.storyapp.data.repository.StoryRepository

class MainViewModel(private val storyRepository: StoryRepository): ViewModel() {
    val isLoading: LiveData<Boolean> = storyRepository.isLoading

    suspend fun logout(){
        storyRepository.logout()
    }
    fun getSession(): LiveData<UserModel> {
        return storyRepository.getSession().asLiveData()
    }

    fun getStory(): LiveData<List<ListStoryItem>> {
        storyRepository.getStory()
        return storyRepository.storyList
    }
}