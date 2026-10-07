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

    private const val BASE_URL = "http://94.228.125.136:8080/"

    @Provides
    @Singleton
    fun provideOkHttpClient(storage: TokenStorage): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()

                if (BuildConfig.API_KEY.isNotBlank() && !BuildConfig.API_KEY.contains("ВАШ_КЛЮЧ")) {
                    builder.addHeader("Api-Key", BuildConfig.API_KEY)
                }

                val token = storage.getToken()
                if (!token.isNullOrEmpty()) {
                    builder.addHeader("Authorization", token)
                }

                chain.proceed(builder.build())
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
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