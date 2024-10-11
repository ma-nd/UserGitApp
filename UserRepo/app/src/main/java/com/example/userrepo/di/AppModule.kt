package com.example.userrepo.di

import com.example.userrepo.data.AuthManager
import com.example.userrepo.data.EncryptedPreferences
import org.koin.dsl.module

val appModule = module {
    single { EncryptedPreferences(get()) }
    single { AuthManager(get()) }
}
