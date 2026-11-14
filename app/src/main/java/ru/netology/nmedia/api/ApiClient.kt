package ru.netology.nmedia.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.auth.TokenStorage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiClient {

    @Provides
    @Singleton
    fun provideOkHttpClient(storage: TokenStorage): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()

                val apiKey = BuildConfig.API_KEY.trim()
                if (apiKey.isNotEmpty()) {
                    requestBuilder.header("Api-Key", apiKey)
                }

                val token = storage.getToken()
                if (!token.isNullOrBlank()) {
                    requestBuilder.header("Authorization", token)
                }

                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    // Do not log request bodies, passwords or authorization data.
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://94.228.125.136:8080/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
