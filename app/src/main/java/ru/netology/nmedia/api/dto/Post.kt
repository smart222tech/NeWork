package ru.netology.nmedia.api.dto

data class Post(
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int,
    val attachment: Attachment?
)

data class Attachment(
    val url: String,
    val type: String
)
