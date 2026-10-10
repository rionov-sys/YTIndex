package com.example.ytindex

import com.example.ytindex.data.BookmarkDto
import com.example.ytindex.data.SearchFunctionRequest
import com.example.ytindex.data.SearchFunctionResponse
import com.example.ytindex.data.VideoResult
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ResponseMappingTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun testSearchFunctionRequestSerialization() {
        val request = SearchFunctionRequest(query = "belajar android", limit = 15)
        val encoded = json.encodeToString(SearchFunctionRequest.serializer(), request)
        assertTrue(encoded.contains("\"query\":\"belajar android\""))
        assertTrue(encoded.contains("\"limit\":15"))
    }

    @Test
    fun testSearchFunctionResponseDeserialization() {
        val rawJson = """
            {
                "items": [
                    {
                        "videoId": "abc1234",
                        "title": "Tutorial Jetpack Compose",
                        "channel": "Kanal Android",
                        "publishedAt": "2026-04-12T10:00:00Z",
                        "thumbnailUrl": "https://i.ytimg.com/vi/abc1234/mqdefault.jpg",
                        "url": "https://www.youtube.com/watch?v=abc1234"
                    }
                ],
                "cached": true
            }
        """.trimIndent()

        val parsed = json.decodeFromString(SearchFunctionResponse.serializer(), rawJson)
        assertTrue(parsed.cached)
        assertEquals(1, parsed.items.size)

        val item = parsed.items.first()
        assertEquals("abc1234", item.videoId)
        assertEquals("Tutorial Jetpack Compose", item.title)
        assertEquals("Kanal Android", item.channel)
        assertEquals("2026-04-12", item.publishedDate)
        assertEquals("https://www.youtube.com/watch?v=abc1234", item.url)
        assertFalse(item.isBookmarked)
    }

    @Test
    fun testBookmarkDtoMapping() {
        val dto = BookmarkDto(
            id = "test-uuid",
            userId = "user-123",
            videoId = "vid-999",
            title = "Video Keren",
            channel = "Channel Mantap",
            thumbnailUrl = "https://thumb.url",
            publishedAt = "2026-01-01T00:00:00Z",
            url = "https://youtube.com/watch?v=vid-999",
            createdAt = "2026-10-10T12:00:00Z"
        )

        assertEquals("vid-999", dto.videoId)
        assertEquals("Video Keren", dto.title)
    }
}
