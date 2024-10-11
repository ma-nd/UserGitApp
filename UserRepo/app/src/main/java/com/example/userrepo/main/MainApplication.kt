package com.example.userrepo.main

import android.app.Application
import com.example.userrepo.di.appModule
import com.example.userrepo.di.githubModule
import com.example.userrepo.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(githubModule, viewModelsModule, appModule)
        }
    }
}