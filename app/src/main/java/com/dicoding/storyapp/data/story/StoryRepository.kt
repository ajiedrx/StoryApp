package com.dicoding.storyapp.data.story

import com.dicoding.storyapp.base.BaseResponse
import com.dicoding.storyapp.data.story.model.GetStoryDetailResponse
import com.dicoding.storyapp.data.story.model.GetStoryListResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StoryRepository {
    suspend fun getAllStories(): Flow<GetStoryListResponse>
    suspend fun getStoryDetail(id: String): Flow<GetStoryDetailResponse>
    suspend fun postStory(description: String, photoFile: File): Flow<BaseResponse>
}