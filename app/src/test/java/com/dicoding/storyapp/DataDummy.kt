package com.dicoding.storyapp

import com.dicoding.storyapp.data.story.model.GetStoryListResponse

object DataDummy {

    fun generateDummyStory(): List<GetStoryListResponse.Story> {

        val storyList = ArrayList<GetStoryListResponse.Story>()

        for (i in 0..15) {
            val news = GetStoryListResponse.Story(
                "2022-02-22T22:22:22Z",
                "Desc {$i}",
                "$i",
                0.0,
                0.0,
                "Data {$i}",
                ""
            )
            storyList.add(news)
        }

        return storyList

    }
}