package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.api.dto.Job
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobRepositoryImpl @Inject constructor(
    private val api: ApiService
) : JobRepository {
    private val _data = MutableStateFlow<List<Job>>(emptyList())
    override val data: Flow<List<Job>> = _data

    override suspend fun getJobs(userId: Long) {
        try {
            val response = api.getJobs(userId)
            _data.value = response
        } catch (e: Exception) {}
    }

    override suspend fun saveJob(job: Job) {
        val saved = api.createJob(job)
        _data.value = _data.value + saved
    }

    override suspend fun deleteJob(id: Long) {
        api.deleteJob(id)
        _data.value = _data.value.filter { it.id != id }
    }
}
