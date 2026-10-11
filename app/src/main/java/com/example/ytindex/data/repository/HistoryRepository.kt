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
     * Mengambil daftar riwayat pencarian pengguna, terurut dari yang paling baru.
     */
    suspend fun getHistory(): List<SearchHistoryDto> {
        val client = postgrest ?: return emptyList()
        return try {
            client.from("search_history")
                .select {
                    order(column = "created_at", order = Order.DESCENDING)
                }
                .decodeList<SearchHistoryDto>()
        } catch (e: Throwable) {
            emptyList()
        }
    }

    /**
     * Menghapus seluruh riwayat pencarian milik pengguna saat ini.
     */
    suspend fun clearAllHistory(): Result<Unit> = runCatching {
        val client = postgrest ?: error("Layanan database belum siap.")
        // RLS memastikan delete hanya menghapus baris milik user saat ini
        client.from("search_history").delete {
            filter {
                neq("id", "00000000-0000-0000-0000-000000000000")
            }
        }
    }

    /**
     * Menghapus satu item riwayat pencarian berdasarkan ID.
     */
    suspend fun deleteHistoryItem(id: String): Result<Unit> = runCatching {
        val client = postgrest ?: error("Layanan database belum siap.")
        client.from("search_history").delete {
            filter {
                eq("id", id)
            }
        }
    }
}
