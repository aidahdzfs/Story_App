package com.bangkit.storyapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.storyapp.data.api.response.LoginResponse
import com.bangkit.storyapp.data.preference.UserModel
import com.bangkit.storyapp.data.repository.StoryRepository

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel(){
    val loginResult: LiveData<LoginResponse> = storyRepository.loginResult

    val isLoading: LiveData<Boolean> = storyRepository.isLoading

    fun login(email: String, pass: String){
        storyRepository.login(email, pass)
    }

    suspend fun saveSession(userModel: UserModel){
        storyRepository.saveSession(userModel)
    }
}