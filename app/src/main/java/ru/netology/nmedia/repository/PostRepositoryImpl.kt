package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.api.dto.Post
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.db.toDto
import ru.netology.nmedia.db.toEntity
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.nmedia.api.dto.Media
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: PostDao
) : PostRepository {

    override fun getPosts(): Flow<List<Post>> =
        dao.getAll().map { entities ->
            entities.map { it.toDto() }
        }

    override suspend fun sync() {
        try {
            val posts = api.getPosts()
            dao.insertAll(posts.map { it.toEntity() })
        } catch (_: Exception) {
        }
    }

    override suspend fun likePost(id: Long): Post {
        val updated = api.likePost(id)
        dao.insertAll(listOf(updated.toEntity()))
        return updated
    }

    override suspend fun deletePost(id: Long) {
        api.deletePost(id)
        dao.removeById(id)
    }

    override suspend fun createPost(post: Post): Post {
        val created = api.createPost(post)
        dao.insertAll(listOf(created.toEntity()))
        return created
    }

    override suspend fun uploadMedia(file: File): Media {
        val response = api.uploadMedia(
            MultipartBody.Part.createFormData(
                "file", file.name, file.asRequestBody()
            )
        )
        if (!response.isSuccessful) error("Upload failed")
        return response.body() ?: error("Body is null")
    }

    override suspend fun updatePost(post: Post): Post {
        val updated = api.updatePost(post.id, post)
        dao.insertAll(listOf(updated.toEntity()))
        return updated
    }
}
