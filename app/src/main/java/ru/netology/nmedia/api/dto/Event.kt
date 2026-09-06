package ru.netology.nmedia.api.dto

data class Event(
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val datetime: String,
    val published: String? = null,
    val coords: Coords? = null,
    val type: String,
    val likedByMe: Boolean,
    val likes: Int,
    val participants: Int,
    val speakerIds: List<Long>? = emptyList(),
    val participatedByMe: Boolean = false,
    val attachment: Attachment?
)
