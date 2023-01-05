package com.dicoding.storyapp.data.auth.model


import com.dicoding.storyapp.base.BaseResponse
import com.dicoding.storyapp.domain.auth.Login
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("loginResult")
    val loginResult: LoginResult?,
): BaseResponse() {
    data class LoginResult(
        @SerializedName("name")
        val name: String?,
        @SerializedName("token")
        val token: String?,
        @SerializedName("userId")
        val userId: String?
    ) {
        fun toDomain(): Login {
            return Login(
                name, token, userId
            )
        }
    }
}