package com.dicoding.storyapp.domain.auth

import com.google.gson.annotations.SerializedName

data class Login(
        @SerializedName("name")
        val name: String?,
        @SerializedName("token")
        val token: String?,
        @SerializedName("userId")
        val userId: String?
    )