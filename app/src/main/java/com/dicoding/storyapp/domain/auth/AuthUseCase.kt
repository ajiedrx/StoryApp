package com.dicoding.storyapp.domain.auth

import com.dicoding.storyapp.base.BaseResponse
import com.dicoding.storyapp.data.auth.model.LoginRequest
import com.dicoding.storyapp.data.auth.model.RegisterRequest
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {
    suspend fun login(request: LoginRequest): Flow<Login>
    suspend fun register(request: RegisterRequest): Flow<BaseResponse>
}