package com.bangkit.storyapp.view.register

import androidx.lifecycle.ViewModel
import com.bangkit.storyapp.data.api.response.RegisterResponse
import com.bangkit.storyapp.data.repository.StoryRepository

class RegisterViewModel(private var storyRepository: StoryRepository): ViewModel() {
    suspend fun register(
        name: String,
        email: String,
        pass: String
    ): RegisterResponse {
        return storyRepository.register(name, email, pass)
    }
}