package com.dicoding.storyapp.data.story

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.storyapp.data.story.model.GetStoryListResponse

class StoryPagingSource(private val apiService: StoryApiService):
    PagingSource<Int, GetStoryListResponse.Story>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, GetStoryListResponse.Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetStoryListResponse.Story> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllStories(page, params.loadSize)

            LoadResult.Page(
                data = responseData.body()?.listStory?.mapNotNull { it }.orEmpty(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.body()?.listStory.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}