package com.dicoding.storyapp.domain.auth

import android.content.SharedPreferences
import com.dicoding.storyapp.base.BaseResponse
import com.dicoding.storyapp.const.Const
import com.dicoding.storyapp.data.auth.AuthRepository
import com.dicoding.storyapp.data.auth.model.LoginRequest
import com.dicoding.storyapp.data.auth.model.RegisterRequest
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class AuthInteractor(private val repository: AuthRepository, private val sharedPreferencesEditor: SharedPreferences.Editor): AuthUseCase {
    override suspend fun login(request: LoginRequest): Flow<Login> {
        return repository.login(request).mapNotNull {
            val loginResult = it.loginResult
            sharedPreferencesEditor
                .putString(Const.USER_LOGIN_INFO, Gson().toJson(loginResult))
                .apply()
            loginResult?.toDomain()
        }
    }

    override suspend fun register(request: RegisterRequest): Flow<BaseResponse> {
        return repository.register(request)
    }
}