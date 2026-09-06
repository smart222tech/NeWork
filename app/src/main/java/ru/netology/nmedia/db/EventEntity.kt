package ru.netology.nmedia.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.api.dto.Event
import ru.netology.nmedia.api.dto.Attachment
import ru.netology.nmedia.api.dto.Coords

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val datetime: String,
    @Embedded val coords: Coords?,
    val type: String,
    val likedByMe: Boolean,
    val likes: Int,
    val participants: Int,
    val attachmentUrl: String?,
    val attachmentType: String?
)

fun EventEntity.toDto() = Event(
    id = id,
    authorId = authorId,
    author = author,
    authorAvatar = authorAvatar,
    content = content,
    datetime = datetime,
    coords = coords,
    type = type,
    likedByMe = likedByMe,
    likes = likes,
    participants = participants,
    attachment = if (attachmentUrl != null) Attachment(attachmentUrl, attachmentType ?: "IMAGE") else null
)

fun Event.toEntity() = EventEntity(
    id = id,
    authorId = authorId,
    author = author,
    authorAvatar = authorAvatar,
    content = content,
    datetime = datetime,
    coords = coords,
    type = type,
    likedByMe = likedByMe,
    likes = likes,
    participants = participants,
    attachmentUrl = attachment?.url,
    attachmentType = attachment?.type
)
