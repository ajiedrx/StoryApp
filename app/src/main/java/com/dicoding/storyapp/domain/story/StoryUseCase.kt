package com.dicoding.storyapp.domain.story

import com.dicoding.storyapp.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StoryUseCase {
    suspend fun getAllStories(): Flow<List<Story>>
    suspend fun getStoryDetail(id: String): Flow<Story>
    suspend fun postStory(description: String, photoFile: File): Flow<BaseResponse>
}