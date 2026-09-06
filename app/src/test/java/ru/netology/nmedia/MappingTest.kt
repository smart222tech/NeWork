package ru.netology.nmedia

import org.junit.Test
import org.junit.Assert.*
import ru.netology.nmedia.api.dto.Post
import ru.netology.nmedia.db.toEntity

class MappingTest {
    @Test
    fun testPostToEntityMapping() {
        val post = Post(
            id = 1,
            authorId = 2,
            author = "Author",
            authorAvatar = "Avatar",
            content = "Content",
            published = "2023-01-01T12:00:00Z",
            likedByMe = true,
            likes = 10,
            attachment = null
        )
        
        val entity = post.toEntity()
        
        assertEquals(post.id, entity.id)
        assertEquals(post.author, entity.author)
        assertEquals(post.content, entity.content)
        assertEquals(post.likes, entity.likes)
    }
}
