package com.dicoding.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.storyapp.domain.story.Story
import com.dicoding.storyapp.domain.story.StoryInteractor
import com.dicoding.storyapp.presentation.adapter.StoryListPagingAdapter
import com.dicoding.storyapp.presentation.story.StoryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var storyViewModel: StoryViewModel

    @Mock
    private lateinit var storyInteractor: StoryInteractor

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        storyViewModel = StoryViewModel(storyInteractor, testScope)
    }

    @Test
    fun `when Get Stories Should Not Null and Return Success`() = runTest {
        val dataSource = DataDummy.generateDummyStory().map { it.toDomain() }
        val data: PagingData<Story> = PagingData.from(dataSource)
        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data
        `when`(storyInteractor.getAllStories()).thenReturn(expectedStory.asFlow())

        val actualData = storyViewModel.getAllStoriesPaging().getOrAwaitValue()
        advanceUntilIdle()
        val differ = AsyncPagingDataDiffer(
            diffCallback =  StoryListPagingAdapter.DIFF_CALLBACK,
            updateCallback = object: ListUpdateCallback{
                override fun onInserted(position: Int, count: Int) {}
                override fun onRemoved(position: Int, count: Int) {}
                override fun onMoved(fromPosition: Int, toPosition: Int) {}
                override fun onChanged(position: Int, count: Int, payload: Any?) {}
            }
        )
        differ.submitData(actualData)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dataSource.size, differ.snapshot().size)
        Assert.assertEquals(dataSource[0].id, differ.snapshot()[0]?.id ?: 0)
    }

    @Test
    fun `when Data Empty Data Size Shoule Be Zero`() = runTest {
        val dataSource: List<Story> = listOf()
        val data: PagingData<Story> = PagingData.from(dataSource)
        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data
        `when`(storyInteractor.getAllStories()).thenReturn(expectedStory.asFlow())

        val actualData = storyViewModel.getAllStoriesPaging().getOrAwaitValue()
        advanceUntilIdle()
        val differ = AsyncPagingDataDiffer(
            diffCallback =  StoryListPagingAdapter.DIFF_CALLBACK,
            updateCallback = object: ListUpdateCallback{
                override fun onInserted(position: Int, count: Int) {}
                override fun onRemoved(position: Int, count: Int) {}
                override fun onMoved(fromPosition: Int, toPosition: Int) {}
                override fun onChanged(position: Int, count: Int, payload: Any?) {}
            }
        )
        differ.submitData(actualData)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(0, differ.snapshot().size)
    }
}