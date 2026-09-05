package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.api.dto.Job

interface JobRepository {
    val data: Flow<List<Job>>
    suspend fun getJobs(userId: Long)
    suspend fun saveJob(job: Job)
    suspend fun deleteJob(id: Long)
}
