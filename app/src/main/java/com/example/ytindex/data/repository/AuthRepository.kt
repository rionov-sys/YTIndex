package com.example.ytindex.data.repository

import com.example.ytindex.data.remote.SupabaseClientProvider
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Repository untuk menangani alur autentikasi pengguna Supabase Auth.
 * Aman terhadap null/kegagalan startup dan mengeksekusi panggilan secara defensif.
 */
class AuthRepository(
    private val clientSupplier: () -> SupabaseClient? = { SupabaseClientProvider.getClientOrNull() }
) {
    /**
     * Konstruktor pendukung untuk pengujian unit yang menyuplai [SupabaseClient] secara langsung.
     */
    constructor(client: SupabaseClient) : this({ client })

    private val auth: Auth?
        get() = try {
            clientSupplier()?.auth
        } catch (e: Throwable) {
            null
        }

    /**
     * Aliran status sesi pengguna (Authenticated, NotAuthenticated, Initializing, dll).
     * Jika Supabase belum siap, mengembalikan status [SessionStatus.NotAuthenticated].
     */
    val sessionStatus: Flow<SessionStatus>
        get() = auth?.sessionStatus ?: flowOf(SessionStatus.NotAuthenticated(false))

    /**
     * Mengambil email pengguna yang sedang aktif, atau null jika belum login.
     */
    val currentEmail: String?
        get() = try {
            auth?.currentUserOrNull()?.email
        } catch (e: Throwable) {
            null
        }

    /**
     * Memeriksa apakah pengguna saat ini telah terautentikasi.
     */
    val isAuthenticated: Boolean
        get() = try {
            auth?.currentUserOrNull() != null
        } catch (e: Throwable) {
            false
        }

    /**
     * Mendaftar akun baru dengan email dan kata sandi.
     */
    suspend fun signUp(email: String, pass: String): Result<Unit> = runCatching {
        val activeAuth = auth ?: error("Layanan autentikasi Supabase belum siap.")
        activeAuth.signUpWith(Email) {
            this.email = email.trim()
            this.password = pass
        }
    }

    /**
     * Masuk ke akun yang sudah terdaftar dengan email dan kata sandi.
     */
    suspend fun signIn(email: String, pass: String): Result<Unit> = runCatching {
        val activeAuth = auth ?: error("Layanan autentikasi Supabase belum siap.")
        activeAuth.signInWith(Email) {
            this.email = email.trim()
            this.password = pass
        }
    }

    /**
     * Keluar dari sesi akun saat ini dan menghapus token lokal.
     */
    suspend fun signOut(): Result<Unit> = runCatching {
        auth?.signOut() ?: Unit
    }
}
