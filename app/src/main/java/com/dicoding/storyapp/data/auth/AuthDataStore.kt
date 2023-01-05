package com.dicoding.storyapp.data.auth

import com.dicoding.storyapp.base.BaseResponse
import com.dicoding.storyapp.data.auth.model.LoginRequest
import com.dicoding.storyapp.data.auth.model.LoginResponse
import com.dicoding.storyapp.data.auth.model.RegisterRequest
import com.dicoding.storyapp.data.network.handleApiError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthDataStore(private val authApiService: AuthApiService): AuthRepository {
    override suspend fun login(request: LoginRequest): Flow<LoginResponse> {
        return flow {
            emit(authApiService.login(request).handleApiError())
        }
    }

    override suspend fun register(request: RegisterRequest): Flow<BaseResponse> {
        return flow {
            emit(authApiService.register(request).handleApiError())
        }
    }
}