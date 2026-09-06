package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.api.dto.Post

import ru.netology.nmedia.api.dto.Media
import java.io.File

interface PostRepository {
    fun getPosts(): Flow<List<Post>>
    suspend fun sync()
    suspend fun likePost(id: Long): Post
    suspend fun deletePost(id: Long)
    suspend fun createPost(post: Post): Post
    suspend fun uploadMedia(file: File): Media
    suspend fun updatePost(post: Post): Post
}
