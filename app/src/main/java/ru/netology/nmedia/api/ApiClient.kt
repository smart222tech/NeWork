package ru.netology.nmedia.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiClient {

    @Provides
    @Singleton
    fun provideOkHttpClient(storage: ru.netology.nmedia.auth.TokenStorage): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Api-Key", "ВАШ_КЛЮЧ_ИЗ_ЛИЧНОГО_КАБИНЕТА")
                    .apply {
                        storage.getToken()?.let { token ->
                            addHeader("Authorization", token)
                        }
                    }
                    .build()
                chain.proceed(request)
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

    @Provides
    @Singleton
    fun providePostDao(@ApplicationContext context: android.content.Context): ru.netology.nmedia.db.PostDao {
        return ru.netology.nmedia.db.AppDatabase.getInstance(context).postDao()
    }

    @Provides
    @Singleton
    fun providePostRepository(apiService: ApiService, dao: ru.netology.nmedia.db.PostDao): ru.netology.nmedia.repository.PostRepository {
        return ru.netology.nmedia.repository.PostRepositoryImpl(apiService, dao)
    }

    @Provides
    @Singleton
    fun provideEventRepository(apiService: ApiService): ru.netology.nmedia.repository.EventRepository {
        return ru.netology.nmedia.repository.EventRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideJobRepository(apiService: ApiService): ru.netology.nmedia.repository.JobRepository {
        return ru.netology.nmedia.repository.JobRepositoryImpl(apiService)
    }
}
