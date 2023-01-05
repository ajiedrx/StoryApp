package com.dicoding.storyapp

import android.app.Application
import com.dicoding.storyapp.di.authModule
import com.dicoding.storyapp.di.networkModule
import com.dicoding.storyapp.di.sharedPreferenceModule
import com.dicoding.storyapp.di.storyModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                sharedPreferenceModule,
                networkModule,
                authModule,
                storyModule
            )
        }
    }
}