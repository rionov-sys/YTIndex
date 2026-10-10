package com.example.ytindex.data

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApi {

    // Biaya kuota: 100 unit per panggilan, berapa pun nilai maxResults.
    @GET("youtube/v3/search")
    suspend fun search(
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("order") order: String = "relevance",
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int,
        @Query("publishedAfter") publishedAfter: String,
        @Query("relevanceLanguage") relevanceLanguage: String = "id",
        @Query("regionCode") regionCode: String = "ID",
        @Query("key") apiKey: String
    ): SearchResponse

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun create(): YouTubeApi =
            Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/")
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(YouTubeApi::class.java)
    }
}
