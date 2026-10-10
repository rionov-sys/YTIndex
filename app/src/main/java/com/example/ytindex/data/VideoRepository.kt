package com.example.ytindex.data

import android.text.Html
import com.example.ytindex.BuildConfig
import java.time.Instant
import java.time.temporal.ChronoUnit

class VideoRepository(
    private val api: YouTubeApi = YouTubeApi.create()
) {

    /**
     * Cari video berdasarkan kata kunci.
     * - hanya video yang diunggah dalam 1 tahun terakhir
     * - diurutkan berdasarkan relevansi (urutan dari YouTube dipertahankan)
     * - jumlah hasil diatur lewat [limit] (batas API: 1..50)
     */
    suspend fun search(query: String, limit: Int): List<VideoResult> {
        require(BuildConfig.YOUTUBE_API_KEY.isNotBlank()) {
            "API key kosong. Isi YOUTUBE_API_KEY di local.properties."
        }

        val oneYearAgo = Instant.now()
            .minus(365, ChronoUnit.DAYS)
            .truncatedTo(ChronoUnit.SECONDS)
            .toString() // format RFC 3339, mis. 2025-10-10T09:00:00Z

        val response = api.search(
            query = query.trim(),
            maxResults = limit.coerceIn(1, 50),
            publishedAfter = oneYearAgo,
            apiKey = BuildConfig.YOUTUBE_API_KEY
        )

        return response.items.mapNotNull { item ->
            val id = item.id.videoId ?: return@mapNotNull null
            val thumbs = item.snippet.thumbnails
            VideoResult(
                videoId = id,
                // Judul dari API berisi entitas HTML (&amp;, &#39;, dst) -> didecode
                title = Html.fromHtml(item.snippet.title, Html.FROM_HTML_MODE_LEGACY).toString(),
                channel = Html.fromHtml(item.snippet.channelTitle, Html.FROM_HTML_MODE_LEGACY).toString(),
                publishedDate = item.snippet.publishedAt.take(10),
                thumbnailUrl = thumbs.medium?.url
                    ?: thumbs.high?.url
                    ?: thumbs.default?.url
                    ?: "",
                url = "https://www.youtube.com/watch?v=$id"
            )
        }
    }
}
