package com.dicoding.storyapp.data.network

import com.dicoding.storyapp.base.BaseResponse
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import retrofit2.Response

fun <T> Response<T>.handleApiError(): T =
    try {
        if(!isSuccessful){
            val errorBody = Gson().fromJson(errorBody()?.string(), BaseResponse::class.java)
            when{
                code() == 401 -> {
                    EventBus.getDefault().post(UnauthorizedAccessEvent())
                }
            }
            throw Exception(errorBody?.message)
        } else {
            body()!!
        }
    } catch (e: ClassCastException){
        when{
            !isSuccessful -> throw Exception(this.message())
            else -> body() ?: kotlin.run {
                throw java.lang.Exception(this.message())
            }
        }
    }