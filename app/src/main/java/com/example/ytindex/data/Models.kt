package com.example.ytindex.data

import kotlinx.serialization.Serializable

// ---- Respons mentah dari YouTube Data API v3 (search.list) ----

@Serializable
data class SearchResponse(
    val items: List<SearchItem> = emptyList()
)

@Serializable
data class SearchItem(
    val id: VideoId = VideoId(),
    val snippet: Snippet = Snippet()
)

@Serializable
data class VideoId(
    val videoId: String? = null
)

@Serializable
data class Snippet(
    val title: String = "",
    val channelTitle: String = "",
    val publishedAt: String = "",
    val thumbnails: Thumbnails = Thumbnails()
)

@Serializable
data class Thumbnails(
    val medium: Thumb? = null,
    val high: Thumb? = null,
    val default: Thumb? = null
)

@Serializable
data class Thumb(
    val url: String = ""
)

// ---- Model yang dipakai UI ----

data class VideoResult(
    val videoId: String,
    val title: String,
    val channel: String,
    val publishedDate: String, // contoh: 2026-03-14
    val thumbnailUrl: String,
    val url: String
)
