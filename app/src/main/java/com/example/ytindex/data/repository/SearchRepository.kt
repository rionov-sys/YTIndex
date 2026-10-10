package com.example.ytindex.data.repository

import com.example.ytindex.data.SearchFunctionRequest
import com.example.ytindex.data.SearchFunctionResponse
import com.example.ytindex.data.VideoResult
import com.example.ytindex.data.remote.SupabaseClientProvider
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.functions.functions

import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

/**
 * Pengecualian khusus saat kuota harian YouTube Data API telah habis (HTTP 429).
 */
class QuotaExceededException(message: String) : Exception(message)

private val searchJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

/**
 * Repository untuk memanggil Supabase Edge Function "youtube-search".
 * Menggunakan token sesi pengguna aktif dan menangani mapping error kuota YouTube API.
 */
class SearchRepository(
    private val client: SupabaseClient = SupabaseClientProvider.client
) {
    private val functions: Functions
        get() = client.functions

    /**
     * Memanggil edge function "youtube-search" untuk mengambil daftar video.
     *
     * @param query Kata kunci pencarian (1-100 karakter).
     * @param limit Jumlah hasil pencarian yang diinginkan (1-50).
     * @return Daftar [VideoResult] terurut berdasarkan relevansi.
     */
    suspend fun searchVideos(query: String, limit: Int): List<VideoResult> {
        val cleanQuery = query.trim()
        require(cleanQuery.isNotBlank()) { "Kata kunci pencarian tidak boleh kosong." }
        val clampedLimit = limit.coerceIn(1, 50)

        try {
            val requestBody = SearchFunctionRequest(query = cleanQuery, limit = clampedLimit)
            val response = functions.invoke("youtube-search", requestBody)
            val bodyString = response.bodyAsText()
            val parsed = searchJson.decodeFromString<SearchFunctionResponse>(bodyString)
            return parsed.items
        } catch (e: Exception) {
            val msg = e.message ?: ""
            if (msg.contains("429") ||
                msg.contains("quotaExceeded", ignoreCase = true) ||
                msg.contains("QUOTA_EXCEEDED", ignoreCase = true)
            ) {
                throw QuotaExceededException(
                    "Batas kuota harian YouTube Data API telah tercapai (quotaExceeded). Silakan coba lagi besok."
                )
            }
            throw e
        }
    }
}
