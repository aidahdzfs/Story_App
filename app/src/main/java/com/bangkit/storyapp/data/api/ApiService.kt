package com.bangkit.storyapp.data.api

import com.bangkit.storyapp.data.api.response.LoginResponse
import com.bangkit.storyapp.data.api.response.RegisterResponse
import com.bangkit.storyapp.data.api.response.StoryResponse
import com.bangkit.storyapp.data.api.response.StoryUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): StoryResponse

    @Multipart
    @POST("stories")
    fun postStories(
        @Part image: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<StoryUploadResponse>

    @GET("stories")
    fun getStoriesLocation(
        @Query("location") location: Int = 1
    ): Call<StoryResponse>

}