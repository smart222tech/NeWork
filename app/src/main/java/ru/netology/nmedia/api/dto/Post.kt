package ru.netology.nmedia.api.dto

import com.google.gson.annotations.SerializedName

data class Post(
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val published: String,
    val coords: Coords? = null,
    val link: String? = null,
    val mentionIds: List<Long> = emptyList(),
    val mentionedMe: Boolean = false,
    val likedByMe: Boolean,
    val likes: Int,
    val attachment: Attachment?
)

data class Coords(
    val lat: Double,
    @SerializedName("long")
    val longitude: Double,
)

data class Attachment(
    val url: String,
    val type: String
)
