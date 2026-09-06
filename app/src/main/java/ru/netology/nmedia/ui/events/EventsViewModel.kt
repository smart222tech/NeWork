package ru.netology.nmedia.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.dto.Event
import ru.netology.nmedia.repository.EventRepository
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {
    val data = repository.data.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        load()
    }

    fun load() = viewModelScope.launch {
        repository.getAll()
    }

    fun like(event: Event) = viewModelScope.launch {
        repository.likeById(event.id)
    }

    fun participate(event: Event) = viewModelScope.launch {
        repository.participate(event.id)
    }

    fun save(content: String, type: String, datetime: String, file: File? = null) {
        viewModelScope.launch {
            try {
                val attachment = file?.let {
                    val media = repository.uploadMedia(it)
                    ru.netology.nmedia.api.dto.Attachment(media.url, "image")
                }
                val event = Event(
                    id = 0,
                    authorId = 0,
                    author = "",
                    authorAvatar = null,
                    content = content,
                    datetime = datetime,
                    type = type,
                    likedByMe = false,
                    likes = 0,
                    participants = 0,
                    speakerIds = null,
                    attachment = attachment,
                    coords = null
                )
                repository.save(event)
                load()
            } catch (e: Exception) {}
        }
    }
}
