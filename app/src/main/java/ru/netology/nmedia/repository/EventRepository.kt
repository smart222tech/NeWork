package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.api.dto.Event
import ru.netology.nmedia.api.dto.Media
import java.io.File

interface EventRepository {
    val data: Flow<List<Event>>
    suspend fun getAll()
    suspend fun likeById(id: Long)
    suspend fun participate(id: Long)
    suspend fun save(event: Event)
    suspend fun uploadMedia(file: File): Media
}
