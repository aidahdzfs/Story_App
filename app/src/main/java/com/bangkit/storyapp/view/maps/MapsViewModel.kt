package com.bangkit.storyapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.storyapp.data.api.response.ListStoryItem
import com.bangkit.storyapp.data.api.response.StoryResponse
import com.bangkit.storyapp.data.repository.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository): ViewModel() {
    val isLoading = storyRepository.isLoading
    fun getStoryLocation(): LiveData<List<ListStoryItem>>{
        storyRepository.getStoriesLocation()
        return storyRepository.storyList
    }
}