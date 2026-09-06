package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.api.dto.Job
import ru.netology.nmedia.db.JobDao
import ru.netology.nmedia.db.toDto
import ru.netology.nmedia.db.toEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: JobDao
) : JobRepository {
    private val _data = MutableStateFlow<List<Job>>(emptyList())
    override val data: Flow<List<Job>> = _data

    override suspend fun getJobs(userId: Long) {
        try {
            val response = api.getJobs(userId)
            _data.value = response
            // Caching for jobs could be more complex (by userId), 
            // for now we update the shared flow.
            dao.insertAll(response.map { it.toEntity(userId) })
        } catch (e: Exception) {}
    }

    override suspend fun saveJob(job: Job) {
        val saved = api.createJob(job)
        _data.value = _data.value + saved
        // We don't have the user ID here easily, ideally it comes from Auth
    }

    override suspend fun deleteJob(id: Long) {
        api.deleteJob(id)
        _data.value = _data.value.filter { it.id != id }
    }
}
