package com.bangkit.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bangkit.storyapp.data.StoryPagingSource
import com.bangkit.storyapp.data.api.ApiService
import com.bangkit.storyapp.data.api.response.ListStoryItem
import com.bangkit.storyapp.data.api.response.LoginResponse
import com.bangkit.storyapp.data.api.response.RegisterResponse
import com.bangkit.storyapp.data.api.response.StoryResponse
import com.bangkit.storyapp.data.api.response.StoryUploadResponse
import com.bangkit.storyapp.data.preference.UserModel
import com.bangkit.storyapp.data.preference.UserPreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
){
    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: MutableLiveData<LoginResponse> = _loginResult

    private val _storyList = MutableLiveData<List<ListStoryItem>>()
    val storyList: MutableLiveData<List<ListStoryItem>> = _storyList
    
    private val _uploadStory = MutableLiveData<StoryUploadResponse>()
    val uploadStory: MutableLiveData<StoryUploadResponse> = _uploadStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading



    suspend fun register(name: String, email: String, pass: String): RegisterResponse{
        return apiService.register(name, email, pass)
    }

    fun login(email: String, pass: String){
        _isLoading.value = true
        apiService.login(email, pass).enqueue(object  : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _loginResult.value = response.body()
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}", )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure: ${t.message}" )
            }

        })
    }

    suspend fun saveSession(userModel: UserModel){
        userPreference.saveSession(userModel)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout(){
        userPreference.logout()
    }

//
    fun getStory(): LiveData<PagingData<ListStoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    fun postStory(img: MultipartBody.Part, desc: RequestBody){
        _isLoading.value = true
        apiService.postStories(img, desc).enqueue(object : Callback<StoryUploadResponse>{
            override fun onResponse(
                call: Call<StoryUploadResponse>,
                response: Response<StoryUploadResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _uploadStory.value = response.body()
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}", )
                }
            }

            override fun onFailure(call: Call<StoryUploadResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure: ${t.message}", )
            }

        })
    }

    fun getStoriesLocation(){
        _isLoading.value = true
        apiService.getStoriesLocation().enqueue(object : Callback<StoryResponse>{
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _storyList.value = response.body()?.listStory
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}", )
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}", )
            }

        })
    }



    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun clearInstance(){
            instance = null
        }

        fun getInstance(apiService: ApiService, userPreference: UserPreference): StoryRepository =
            instance ?: synchronized(this){
                instance ?: StoryRepository(apiService, userPreference)
            }.also { instance = it }
        
        private const val TAG = "StoryRepository"
    }
}