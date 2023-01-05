package com.dicoding.storyapp.data.story

import com.dicoding.storyapp.base.BaseResponse
import com.dicoding.storyapp.data.story.model.GetStoryDetailResponse
import com.dicoding.storyapp.data.story.model.GetStoryListResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface StoryApiService {
    @GET("stories")
    @Headers("Authorization:need")
    suspend fun getAllStories(): Response<GetStoryListResponse>

    @GET("stories/{id}")
    @Headers("Authorization:need")
    suspend fun getStoryDetail(@Path("id") id: String): Response<GetStoryDetailResponse>

    @Multipart
    @POST("stories")
    @Headers("Multipart:need", "Authorization:need")
    suspend fun postStory(@Part description: MultipartBody.Part, @Part photo: MultipartBody.Part): Response<BaseResponse>
}