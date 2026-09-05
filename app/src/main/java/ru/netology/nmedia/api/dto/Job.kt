package ru.netology.nmedia.api.dto

data class Job(
    val id: Long,
    val name: String,
    val position: String,
    val start: String,
    val finish: String?
)
