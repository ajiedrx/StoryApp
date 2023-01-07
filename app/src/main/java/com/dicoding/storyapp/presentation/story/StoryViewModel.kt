package com.dicoding.storyapp.presentation.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.base.BaseResponse
import com.dicoding.storyapp.base.BaseResult
import com.dicoding.storyapp.domain.story.Story
import com.dicoding.storyapp.domain.story.StoryUseCase
import com.dicoding.storyapp.getViewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File

class StoryViewModel(private val storyUseCase: StoryUseCase, coroutineScopeProvider: CoroutineScope? = null) : ViewModel() {

    private val coroutineScope: CoroutineScope

    init {
        coroutineScope = getViewModelScope(coroutineScopeProvider)
    }


    private var _getAllStories: MutableLiveData<BaseResult<List<Story>>> = MutableLiveData<BaseResult<List<Story>>>()
    val getAllStories: LiveData<BaseResult<List<Story>>> by lazy { _getAllStories }

//    private var _getAllStoriesPaging: MutableLiveData<PagingData<Story>> = MutableLiveData<PagingData<Story>>()
//    val getAllStoriesPaging: LiveData<PagingData<Story>> by lazy { _getAllStoriesPaging }

    private var _getStoryDetail: MutableLiveData<BaseResult<Story>> = MutableLiveData< BaseResult<Story>>()
    val getStoryDetail: LiveData<BaseResult<Story>> by lazy { _getStoryDetail }

    private var _uploadStory: MutableLiveData<BaseResult<BaseResponse>> = MutableLiveData<BaseResult<BaseResponse>>()
    val uploadStory: LiveData<BaseResult<BaseResponse>> by lazy { _uploadStory }

    fun getAllStories(isRetrieveLocation: Boolean){
        viewModelScope.launch {
            storyUseCase
                .getAllStories(isRetrieveLocation)
                .onStart {
                    _getAllStories.value = BaseResult.Loading
                }
                .catch {
                    _getAllStories.value = BaseResult.Error(it.message.orEmpty())
                }
                .collect{
                    _getAllStories.value = BaseResult.Success(it)
                }
        }
    }

//    val storyListPaging: LiveData<PagingData<Story>> =
//        storyUseCase.getAllStories().cachedIn(viewModelScope).asLiveData()

    fun getAllStoriesPaging(): LiveData<PagingData<Story>> {
        return storyUseCase.getAllStories().cachedIn(coroutineScope).asLiveData()
//        coroutineScope.launch {
//            storyUseCase
//                .getAllStories()
//                .cachedIn(coroutineScope)
//                .onStart {
//                    _getAllStoriesPaging.value = BaseResult.Loading
//                }
//                .catch {
//                    _getAllStoriesPaging.value = BaseResult.Error(it.message.orEmpty())
//                }
//                .collect {
//                    _getAllStoriesPaging.value = BaseResult.Success(it)
//                }
//        }
    }

    fun getStoryDetail(id: String){
        viewModelScope.launch {
            storyUseCase
                .getStoryDetail(
                    id
                )
                .onStart {
                    _getStoryDetail.value = BaseResult.Loading
                }
                .catch {
                    _getStoryDetail.value = BaseResult.Error(it.message.orEmpty())
                }
                .collect{
                    _getStoryDetail.value = BaseResult.Success(it)
                }
        }
    }

    fun postStory(description: String, photoFile: File){
        viewModelScope.launch {
            storyUseCase
                .postStory(
                    description, photoFile
                )
                .onStart {
                    _uploadStory.value = BaseResult.Loading
                }
                .catch {
                    _uploadStory.value = BaseResult.Error(it.message.orEmpty())
                }
                .collect{
                    _uploadStory.value = BaseResult.Success(it)
                }
        }
    }
}