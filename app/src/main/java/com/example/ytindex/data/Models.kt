package com.example.ytindex.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Payload permintaan untuk Supabase Edge Function "youtube-search".
 */
@Serializable
data class SearchFunctionRequest(
    val query: String,
    val limit: Int
)

/**
 * Respons dari Supabase Edge Function "youtube-search".
 */
@Serializable
data class SearchFunctionResponse(
    val items: List<VideoResult> = emptyList(),
    val cached: Boolean = false
)

/**
 * Model data video yang digunakan pada UI dan daftar hasil pencarian.
 */
@Serializable
data class VideoResult(
    val videoId: String,
    val title: String,
    val channel: String,
    val publishedAt: String = "",
    val publishedDate: String = publishedAt.take(10),
    val thumbnailUrl: String = "",
    val url: String = "https://www.youtube.com/watch?v=$videoId",
    val isBookmarked: Boolean = false
)

/**
 * DTO untuk tabel "bookmarks" di database Supabase PostgreSQL.
 */
@Serializable
data class BookmarkDto(
    val id: String? = null,
    @SerialName("user_id") val userId: String? = null,
    @SerialName("video_id") val videoId: String,
    val title: String? = null,
    val channel: String? = null,
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,
    @SerialName("published_at") val publishedAt: String? = null,
    val url: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)

/**
 * DTO untuk tabel "search_history" di database Supabase PostgreSQL.
 */
@Serializable
data class SearchHistoryDto(
    val id: String? = null,
    @SerialName("user_id") val userId: String? = null,
    val query: String,
    @SerialName("result_limit") val resultLimit: Int? = null,
    @SerialName("created_at") val createdAt: String? = null
)
