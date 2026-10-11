package com.example.ytindex.data.repository

import com.example.ytindex.data.BookmarkDto
import com.example.ytindex.data.VideoResult
import com.example.ytindex.data.remote.SupabaseClientProvider
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order

/**
 * Repository untuk operasi CRUD pada tabel "bookmarks" di Supabase PostgREST.
 * Aturan Row Level Security (RLS) otomatis memastikan pengguna hanya mengakses datanya sendiri.
 */
class BookmarkRepository(
    private val clientSupplier: () -> SupabaseClient? = { SupabaseClientProvider.getClientOrNull() }
) {
    constructor(client: SupabaseClient) : this({ client })

    private val postgrest: Postgrest?
        get() = try {
            clientSupplier()?.postgrest
        } catch (e: Throwable) {
            null
        }

    /**
     * Mengambil seluruh daftar bookmark milik pengguna saat ini, terurut dari yang terbaru.
     */
    suspend fun getBookmarks(): List<BookmarkDto> {
        val client = postgrest ?: return emptyList()
        return try {
            client.from("bookmarks")
                .select {
                    order(column = "created_at", order = Order.DESCENDING)
                }
                .decodeList<BookmarkDto>()
        } catch (e: Throwable) {
            emptyList()
        }
    }

    /**
     * Mengambil himpunan ID video yang telah dibookmark oleh pengguna.
     */
    suspend fun getBookmarkedVideoIds(): Set<String> {
        val list = getBookmarks()
        return list.map { it.videoId }.toSet()
    }

    /**
     * Menambahkan video ke daftar bookmark pengguna.
     */
    suspend fun addBookmark(video: VideoResult): Result<Unit> = runCatching {
        val client = postgrest ?: error("Layanan database belum siap.")
        val dto = BookmarkDto(
            videoId = video.videoId,
            title = video.title,
            channel = video.channel,
            thumbnailUrl = video.thumbnailUrl,
            publishedAt = video.publishedAt,
            url = video.url
        )
        client.from("bookmarks").insert(dto)
    }

    /**
     * Menghapus video dari bookmark berdasarkan ID video.
     */
    suspend fun removeBookmark(videoId: String): Result<Unit> = runCatching {
        val client = postgrest ?: error("Layanan database belum siap.")
        client.from("bookmarks").delete {
            filter {
                eq("video_id", videoId)
            }
        }
    }
}
