package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.netology.nmedia.api.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.nmedia.api.dto.Event
import ru.netology.nmedia.api.dto.Media
import ru.netology.nmedia.db.EventDao
import ru.netology.nmedia.db.toDto
import ru.netology.nmedia.db.toEntity
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: EventDao
) : EventRepository {
    override val data: Flow<List<Event>> = dao.getAll().map { entities ->
        entities.map { it.toDto() }
    }

    override suspend fun getAll() {
        try {
            val response = api.getEvents()
            dao.insertAll(response.map { it.toEntity() })
        } catch (e: Exception) {}
    }

    override suspend fun likeById(id: Long) {
        val updated = api.likeEvent(id)
        dao.insertAll(listOf(updated.toEntity()))
    }

    override suspend fun participate(id: Long) {
        val updated = api.participateEvent(id)
        dao.insertAll(listOf(updated.toEntity()))
    }

    override suspend fun save(event: Event) {
        val saved = api.createEvent(event)
        dao.insertAll(listOf(saved.toEntity()))
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
}
