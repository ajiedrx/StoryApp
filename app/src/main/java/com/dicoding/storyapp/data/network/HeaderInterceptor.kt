package com.dicoding.storyapp.data.network

import android.content.SharedPreferences
import com.dicoding.storyapp.const.Const
import com.dicoding.storyapp.domain.auth.Login
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor(
    private val headers: HashMap<String, String>,
    private val sharedPreferences: SharedPreferences,
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = mapHeaders(chain)
        return chain.proceed(request)
    }

    private fun mapAccessToken() {
        if(sharedPreferences.contains(Const.USER_LOGIN_INFO)){
            val peekAuthToken = Gson().fromJson(sharedPreferences.getString(Const.USER_LOGIN_INFO, ""), Login::class.java).token
            headers["Authorization"] = "Bearer $peekAuthToken"
        }
    }

    private fun mapHeaders(chain: Interceptor.Chain): Request {
        var original = chain.request()
        val authorizationHeadersMap = original.headers().values("Authorization")
        val multipartHeadersMap = original.headers().values("Multipart")

        if (authorizationHeadersMap.any()) {
            original = original.newBuilder().removeHeader("Authorization").build()
            mapAccessToken()
        }

        if(multipartHeadersMap.any()){
            original = original.newBuilder().removeHeader("Multipart").build()
            headers["Content-Type"] = "multipart/form-data"
        }

        val requestBuilder = original.newBuilder()
        for ((key, value) in headers) {
            requestBuilder.addHeader(key, value)
        }
        headers.clear()
        return requestBuilder.build()
    }
}
