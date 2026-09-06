package ru.netology.nmedia.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import ru.netology.nmedia.api.dto.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/users/authentication")
    suspend fun login(
        @Field("login") login: String,
        @Field("password") password: String
    ): Response<Token>

    @Multipart
    @POST("api/users/registration")
    suspend fun register(
        @Part("login") login: MultipartBody.Part,
        @Part("password") password: MultipartBody.Part,
        @Part("name") name: MultipartBody.Part,
        @Part file: MultipartBody.Part? = null
    ): Response<Token>

    @Multipart
    @POST("api/media")
    suspend fun uploadMedia(@Part file: MultipartBody.Part): Response<Media>

    @GET("api/posts")
    suspend fun getPosts(): List<Post>

    @GET("api/{authorId}/wall")
    suspend fun getUserWall(@Path("authorId") authorId: Long): List<Post>

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
