package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.api.dto.Post

interface PostRepository {
    fun getPosts(): Flow<List<Post>>
    suspend fun sync()
    suspend fun likePost(id: Long): Post
    suspend fun deletePost(id: Long)
    suspend fun createPost(post: Post): Post
    suspend fun updatePost(post: Post): Post
}
