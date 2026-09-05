package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.api.dto.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val api: ApiService
) : EventRepository {
    private val _data = MutableStateFlow<List<Event>>(emptyList())
    override val data: Flow<List<Event>> = _data

    override suspend fun getAll() {
        try {
            val response = api.getEvents()
            _data.value = response
        } catch (e: Exception) {}
    }

    override suspend fun likeById(id: Long) {
        val updated = api.likeEvent(id)
        _data.value = _data.value.map { if (it.id == id) updated else it }
    }

    override suspend fun participate(id: Long) {
        val updated = api.participateEvent(id)
        _data.value = _data.value.map { if (it.id == id) updated else it }
    }
}
