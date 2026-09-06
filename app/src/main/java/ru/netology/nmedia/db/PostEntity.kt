package ru.netology.nmedia.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.api.dto.Post
import ru.netology.nmedia.api.dto.Attachment
import ru.netology.nmedia.api.dto.Coords

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val published: String,
    @Embedded val coords: Coords?,
    val link: String?,
    val likedByMe: Boolean,
    val likes: Int,
    val attachmentUrl: String?,
    val attachmentType: String?
)

fun PostEntity.toDto() = Post(
    id = id,
    authorId = authorId,
    author = author,
    authorAvatar = authorAvatar,
    content = content,
    published = published,
    coords = coords,
    link = link,
    likedByMe = likedByMe,
    likes = likes,
    attachment = if (attachmentUrl != null) Attachment(attachmentUrl, attachmentType ?: "IMAGE") else null
)

fun Post.toEntity() = PostEntity(
    id = id,
    authorId = authorId,
    author = author,
    authorAvatar = authorAvatar,
    content = content,
    published = published,
    coords = coords,
    link = link,
    likedByMe = likedByMe,
    likes = likes,
    attachmentUrl = attachment?.url,
    attachmentType = attachment?.type
)
