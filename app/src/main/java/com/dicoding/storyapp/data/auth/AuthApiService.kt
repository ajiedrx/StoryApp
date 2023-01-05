package com.dicoding.storyapp.data.auth

import com.dicoding.storyapp.base.BaseResponse
import com.dicoding.storyapp.data.auth.model.LoginRequest
import com.dicoding.storyapp.data.auth.model.LoginResponse
import com.dicoding.storyapp.data.auth.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<BaseResponse>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}