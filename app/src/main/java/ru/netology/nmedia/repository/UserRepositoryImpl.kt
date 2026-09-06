package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.api.dto.User
import ru.netology.nmedia.db.UserDao
import ru.netology.nmedia.db.toDto
import ru.netology.nmedia.db.toEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: UserDao
) : UserRepository {
    override val data: Flow<List<User>> = dao.getAll().map { entities ->
        entities.map { it.toDto() }
    }

    override suspend fun getAll() {
        try {
            val response = api.getUsers()
            dao.insertAll(response.map { it.toEntity() })
        } catch (e: Exception) {}
    }
}
