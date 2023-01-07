package com.dicoding.storyapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dicoding.storyapp.const.Const
import com.dicoding.storyapp.data.auth.AuthApiService
import com.dicoding.storyapp.data.auth.AuthDataStore
import com.dicoding.storyapp.data.auth.AuthRepository
import com.dicoding.storyapp.data.network.ApiService
import com.dicoding.storyapp.data.network.HeaderInterceptor
import com.dicoding.storyapp.data.story.StoryApiService
import com.dicoding.storyapp.data.story.StoryDataStore
import com.dicoding.storyapp.data.story.StoryRepository
import com.dicoding.storyapp.domain.auth.AuthInteractor
import com.dicoding.storyapp.domain.auth.AuthUseCase
import com.dicoding.storyapp.domain.story.StoryInteractor
import com.dicoding.storyapp.domain.story.StoryUseCase
import com.dicoding.storyapp.presentation.auth.AuthViewModel
import com.dicoding.storyapp.presentation.story.StoryViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val baseUrl = "https://story-api.dicoding.dev/v1/"

val networkModule = module {
    single {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(getHeaderInterceptor(get()))
            .addInterceptor(
                ChuckerInterceptor.Builder(get())
                .collector(ChuckerCollector(get()))
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(false)
                .build())
            .build()

        val client = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        client
    }
}

private fun getHeaderInterceptor(sharedPreferences: SharedPreferences): Interceptor {
    val headers = HashMap<String, String>()
    return HeaderInterceptor(headers, sharedPreferences)
}

val sharedPreferenceModule = module {
    single {
        getSharedPrefs(androidApplication())
    }
    single<SharedPreferences.Editor> {
        getSharedPrefs(androidApplication()).edit()
    }
}

fun getSharedPrefs(androidApplication: Application): SharedPreferences{
    return  androidApplication.getSharedPreferences(Const.PREFERENCE_NAME,  Context.MODE_PRIVATE)
}

val authModule = module {
    single { ApiService.createReactiveService(AuthApiService::class.java, get()) }
    single<AuthRepository> { AuthDataStore(get()) }
    single<AuthUseCase> { AuthInteractor(get(), get()) }
    viewModel { AuthViewModel(get()) }
}

val storyModule = module {
    single { ApiService.createReactiveService(StoryApiService::class.java, get()) }
    single<StoryRepository> { StoryDataStore(get()) }
    single<StoryUseCase> { StoryInteractor(get()) }
    viewModel { StoryViewModel(get()) }
}


