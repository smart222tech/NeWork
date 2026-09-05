package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.api.dto.Event

interface EventRepository {
    val data: Flow<List<Event>>
    suspend fun getAll()
    suspend fun likeById(id: Long)
    suspend fun participate(id: Long)
}
