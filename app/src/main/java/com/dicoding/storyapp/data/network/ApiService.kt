package com.dicoding.storyapp.data.network

import retrofit2.Retrofit

object ApiService {
    fun <T> createReactiveService(
        serviceClass: Class<T>,
        retrofitClient: Retrofit
    ): T {
        return retrofitClient.create(serviceClass)
    }
}