package ru.netology.nmedia.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.api.dto.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Long,
    val login: String,
    val name: String,
    val avatar: String?
)

fun UserEntity.toDto() = User(
    id = id,
    login = login,
    name = name,
    avatar = avatar
)

fun User.toEntity() = UserEntity(
    id = id,
    login = login,
    name = name,
    avatar = avatar
)
