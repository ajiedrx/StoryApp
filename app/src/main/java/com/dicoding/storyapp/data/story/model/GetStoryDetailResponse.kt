package com.dicoding.storyapp.data.story.model


import com.dicoding.storyapp.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class GetStoryDetailResponse(
    @SerializedName("story")
    val story: Story?
): BaseResponse() {
    data class Story(
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("lat")
        val lat: Double?,
        @SerializedName("lon")
        val lon: Double?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("photoUrl")
        val photoUrl: String?
    ) {
        fun toDomain(): com.dicoding.storyapp.domain.story.Story{
            return com.dicoding.storyapp.domain.story.Story(
                createdAt = createdAt.orEmpty(),
                description = description.orEmpty(),
                id = id.orEmpty(),
                lat = lat ?: 0.0,
                lon = lon ?: 0.0,
                name = name.orEmpty(),
                photoUrl = photoUrl.orEmpty()
            )
        }
    }
}