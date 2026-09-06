package ru.netology.nmedia.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.dto.Post
import ru.netology.nmedia.repository.PostRepository
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            repository.getPosts().collect { list ->
                _posts.value = list
            }
        }
        load()
    }

    fun load() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.sync()
            } catch (e: Exception) {
                // error handling
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun like(post: Post) {
        viewModelScope.launch {
            try {
                val updated = repository.likePost(post.id)
                _posts.value = _posts.value.map { if (it.id == updated.id) updated else it }
            } catch (_: Exception) {
            }
        }
    }

    fun delete(post: Post) {
        viewModelScope.launch {
            try {
                repository.deletePost(post.id)
                _posts.value = _posts.value.filter { it.id != post.id }
            } catch (_: Exception) {
            }
        }
    }

    fun save(content: String, file: File? = null) {
        viewModelScope.launch {
            try {
                val attachment = file?.let {
                    val media = repository.uploadMedia(it)
                    ru.netology.nmedia.api.dto.Attachment(media.url, "image")
                }
                val post = Post(
                    id = 0,
                    authorId = 0,
                    author = "",
                    authorAvatar = null,
                    content = content,
                    published = "",
                    likedByMe = false,
                    likes = 0,
                    attachment = attachment,
                    coords = null,
                    link = null,
                    mentionIds = emptyList()
                )
                repository.createPost(post)
                load() // Reload to get the new post with real IDs
            } catch (_: Exception) {
            }
        }
    }
}
