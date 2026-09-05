package ru.netology.nmedia.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.api.dto.Post
import ru.netology.nmedia.api.dto.Attachment

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int,
    val attachmentUrl: String?,
    val attachmentType: String?
)

fun PostEntity.toPost() = Post(
    id = id,
    authorId = authorId,
    author = author,
    authorAvatar = authorAvatar,
    content = content,
    published = published,
    likedByMe = likedByMe,
    likes = likes,
    attachment = if (attachmentUrl != null) Attachment(attachmentUrl, attachmentType ?: "image") else null
)

fun Post.toEntity() = PostEntity(
    id = id,
    authorId = authorId,
    author = author,
    authorAvatar = authorAvatar,
    content = content,
    published = published,
    likedByMe = likedByMe,
    likes = likes,
    attachmentUrl = attachment?.url,
    attachmentType = attachment?.type
)
