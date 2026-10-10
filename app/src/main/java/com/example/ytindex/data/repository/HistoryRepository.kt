package com.example.ytindex.data.repository

import com.example.ytindex.data.SearchHistoryDto
import com.example.ytindex.data.remote.SupabaseClientProvider
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order

/**
 * Repository untuk mengelola riwayat pencarian pengguna pada tabel "search_history" di Supabase.
 */
class HistoryRepository(
    private val client: SupabaseClient = SupabaseClientProvider.client
) {
    private val postgrest: Postgrest
        get() = client.postgrest

    /**
     * Mengambil daftar riwayat pencarian pengguna, terurut dari yang paling baru.
     */
    suspend fun getHistory(): List<SearchHistoryDto> {
        return postgrest.from("search_history")
            .select {
                order(column = "created_at", order = Order.DESCENDING)
            }
            .decodeList<SearchHistoryDto>()
    }

    /**
     * Menghapus seluruh riwayat pencarian milik pengguna saat ini.
     */
    suspend fun clearAllHistory(): Result<Unit> = runCatching {
        // RLS memastikan delete hanya menghapus baris milik user saat ini
        postgrest.from("search_history").delete {
            filter {
                neq("id", "00000000-0000-0000-0000-000000000000")
            }
        }
    }

    /**
     * Menghapus satu item riwayat pencarian berdasarkan ID.
     */
    suspend fun deleteHistoryItem(id: String): Result<Unit> = runCatching {
        postgrest.from("search_history").delete {
            filter {
                eq("id", id)
            }
        }
    }
}
