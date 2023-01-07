package com.dicoding.storyapp.domain.story

import androidx.paging.PagingData
import androidx.paging.map
import com.dicoding.storyapp.base.BaseResponse
import com.dicoding.storyapp.data.story.StoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import java.io.File

class StoryInteractor(private val repository: StoryRepository):
    StoryUseCase {
    override suspend fun getAllStories(isRetrieveLocation: Boolean): Flow<List<Story>> {
        return repository.getAllStories(isRetrieveLocation).mapNotNull { it.listStory?.mapNotNull { it?.toDomain() } }
    }

    override fun getAllStories(): Flow<PagingData<Story>> {
        return repository.getAllStories().mapNotNull { it.map { it.toDomain() } }
    }

    override suspend fun getStoryDetail(id: String): Flow<Story> {
        return repository.getStoryDetail(id).mapNotNull { it.story?.toDomain() }
    }

    override suspend fun postStory(description: String, photoFile: File): Flow<BaseResponse> {
        return repository.postStory(description, photoFile)
    }
}