package com.dicoding.storyapp.data.auth

import com.dicoding.storyapp.base.BaseResponse
import com.dicoding.storyapp.data.auth.model.LoginRequest
import com.dicoding.storyapp.data.auth.model.LoginResponse
import com.dicoding.storyapp.data.auth.model.RegisterRequest
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(request: LoginRequest): Flow<LoginResponse>
    suspend fun register(request: RegisterRequest): Flow<BaseResponse>
}