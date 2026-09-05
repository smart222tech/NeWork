package ru.netology.nmedia.api

import retrofit2.http.*
import ru.netology.nmedia.api.dto.*

interface ApiService {
    @GET("api/posts")
    suspend fun getPosts(): List<Post>

    @POST("api/posts")
    suspend fun createPost(@Body post: Post): Post

    @PUT("api/posts/{id}")
    suspend fun updatePost(@Path("id") id: Long, @Body post: Post): Post

    @DELETE("api/posts/{id}")
    suspend fun deletePost(@Path("id") id: Long)

    @POST("api/posts/{id}/likes")
    suspend fun likePost(@Path("id") id: Long): Post

    @GET("api/events")
    suspend fun getEvents(): List<Event>

    @POST("api/events")
    suspend fun createEvent(@Body event: Event): Event

    @PUT("api/events/{id}")
    suspend fun updateEvent(@Path("id") id: Long, @Body event: Event): Event

    @DELETE("api/events/{id}")
    suspend fun deleteEvent(@Path("id") id: Long)

    @POST("api/events/{id}/likes")
    suspend fun likeEvent(@Path("id") id: Long): Event

    @POST("api/events/{id}/participants")
    suspend fun participateEvent(@Path("id") id: Long): Event

    @GET("api/users")
    suspend fun getUsers(): List<User>

    @GET("api/users/{id}")
    suspend fun getUser(@Path("id") id: Long): User

    @GET("api/users/{id}/jobs")
    suspend fun getJobs(@Path("id") id: Long): List<Job>

    @POST("api/jobs")
    suspend fun createJob(@Body job: Job): Job

    @DELETE("api/jobs/{id}")
    suspend fun deleteJob(@Path("id") id: Long)
}
