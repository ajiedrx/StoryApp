package com.dicoding.storyapp.presentation.story

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.base.BaseResponse
import com.dicoding.storyapp.base.BaseResult
import com.dicoding.storyapp.domain.story.Story
import com.dicoding.storyapp.domain.story.StoryUseCase
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File

class StoryViewModel(private val storyUseCase: StoryUseCase, private val context: Context) : ViewModel() {

    private var _getAllStories: MutableLiveData<BaseResult<List<Story>>> = MutableLiveData<BaseResult<List<Story>>>()
    val getAllStories: LiveData<BaseResult<List<Story>>> by lazy { _getAllStories }

    private var _getStoryDetail: MutableLiveData<BaseResult<Story>> = MutableLiveData< BaseResult<Story>>()
    val getStoryDetail: LiveData<BaseResult<Story>> by lazy { _getStoryDetail }

    private var _uploadStory: MutableLiveData<BaseResult<BaseResponse>> = MutableLiveData<BaseResult<BaseResponse>>()
    val uploadStory: LiveData<BaseResult<BaseResponse>> by lazy { _uploadStory }

    fun getAllStories(){
        viewModelScope.launch {
            storyUseCase
                .getAllStories()
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
            val finalFile = if(photoFile.length() / 1024 > 1000){
                Compressor
                    .compress(context,photoFile){
                        quality(80)
                        size(maxFileSize = 1_048_576)
                    }
            } else {
                photoFile
            }
            storyUseCase
                .postStory(
                    description, finalFile
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