package com.dicoding.storyapp.data.story

import com.dicoding.storyapp.base.BaseResponse
import com.dicoding.storyapp.data.network.handleApiError
import com.dicoding.storyapp.data.story.model.GetStoryDetailResponse
import com.dicoding.storyapp.data.story.model.GetStoryListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class StoryDataStore(private val storyApiService: StoryApiService): StoryRepository {
    override suspend fun getAllStories(): Flow<GetStoryListResponse> {
        return flow {
            emit(storyApiService.getAllStories().handleApiError())
        }
    }

    override suspend fun getStoryDetail(id: String): Flow<GetStoryDetailResponse> {
        return flow {
            emit(storyApiService.getStoryDetail(id).handleApiError())
        }
    }

    override suspend fun postStory(description: String, photoFile: File): Flow<BaseResponse> {
        return flow {
            emit(
                storyApiService
                    .postStory(
                        MultipartBody.Part.createFormData(
                            "description",
                            description
                        ),
                        MultipartBody.Part.createFormData(
                            "photo",
                            photoFile.name,
                            RequestBody.create(MultipartBody.FORM, photoFile)
                        )
                    )
                    .handleApiError()
            )
        }.flowOn(
            Dispatchers.IO)
    }
}