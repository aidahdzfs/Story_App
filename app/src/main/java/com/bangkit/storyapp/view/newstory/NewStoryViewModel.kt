package com.bangkit.storyapp.view.newstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.storyapp.data.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class NewStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {

    val isLoading: LiveData<Boolean> = storyRepository.isLoading

    val uploadStory = storyRepository.uploadStory

    fun uploadStory(img: MultipartBody.Part, desc: RequestBody){
        return storyRepository.postStory(img, desc)
    }
}