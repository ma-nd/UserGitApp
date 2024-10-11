package com.example.userrepo.di

import com.example.userrepo.BuildConfig
import com.example.userrepo.data.AuthManager
import com.example.userrepo.data.GithubRepository
import com.example.userrepo.data.api.GithubApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val githubModule = module {
    single { provideHttpLoggingInterceptor() }
    single { provideGson() }
    single { provideOkHttpClient(get(), get()) }
    single { provideGithubApi(get()) }
    single { provideRetrofit(get(), get()) }
    single { GithubRepository(get()) }
}

private fun provideGithubApi(retrofit: Retrofit): GithubApi = retrofit.create(GithubApi::class.java)

private fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    val githubUrl = "https://api.github.com/"
    return Retrofit.Builder()
        .baseUrl(githubUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

private fun provideGson(): Gson {
    return GsonBuilder().create()
}

private fun provideOkHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor,
    authManager: AuthManager
): OkHttpClient {
    val timeout = 60L
    val builder = OkHttpClient.Builder()
        .readTimeout(timeout, TimeUnit.SECONDS)
        .connectTimeout(timeout, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            if (authManager.isUserAuthorize()) {
                val credentials = authManager.getUserCredentials()
                val requestBuilder = chain.request().newBuilder()
                requestBuilder.addHeader(
                    "Authorization",
                    Credentials.basic(credentials.first, credentials.second)
                )
                chain.proceed(requestBuilder.build())
            } else {
                chain.proceed(chain.request())
            }
        }

    if (BuildConfig.DEBUG) {
        builder.addInterceptor(httpLoggingInterceptor)
    }

    return builder.build()
}

private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return interceptor
}