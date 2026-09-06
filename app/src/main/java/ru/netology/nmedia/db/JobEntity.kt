package ru.netology.nmedia.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.api.dto.Job

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey val id: Long,
    val userId: Long, // Important for filtering
    val name: String,
    val position: String,
    val start: String,
    val finish: String?
)

fun JobEntity.toDto() = Job(
    id = id,
    name = name,
    position = position,
    start = start,
    finish = finish
)

fun Job.toEntity(userId: Long) = JobEntity(
    id = id,
    userId = userId,
    name = name,
    position = position,
    start = start,
    finish = finish
)
