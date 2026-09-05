package ru.netology.nmedia.api.dto

data class Event(
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val datetime: String,
    val type: String,
    val likedByMe: Boolean,
    val likes: Int,
    val participants: Int,
    val speakerIds: List<Long>?,
    val attachment: Attachment?
)
