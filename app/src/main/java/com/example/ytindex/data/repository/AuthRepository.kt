package com.example.ytindex.data.repository

import com.example.ytindex.data.remote.SupabaseClientProvider
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

/**
 * Repository untuk menangani alur autentikasi pengguna Supabase Auth.
 */
class AuthRepository(
    private val client: SupabaseClient = SupabaseClientProvider.client
) {
    private val auth: Auth
        get() = client.auth

    /**
     * Aliran status sesi pengguna (Authenticated, NotAuthenticated, Loading, dll).
     */
    val sessionStatus: Flow<SessionStatus>
        get() = auth.sessionStatus

    /**
     * Mengambil email pengguna yang sedang aktif, atau null jika belum login.
     */
    val currentEmail: String?
        get() = auth.currentUserOrNull()?.email

    /**
     * Memeriksa apakah pengguna saat ini telah terautentikasi.
     */
    val isAuthenticated: Boolean
        get() = auth.currentUserOrNull() != null

    /**
     * Mendaftar akun baru dengan email dan kata sandi.
     */
    suspend fun signUp(email: String, pass: String): Result<Unit> = runCatching {
        auth.signUpWith(Email) {
            this.email = email.trim()
            this.password = pass
        }
    }

    /**
     * Masuk ke akun yang sudah terdaftar dengan email dan kata sandi.
     */
    suspend fun signIn(email: String, pass: String): Result<Unit> = runCatching {
        auth.signInWith(Email) {
            this.email = email.trim()
            this.password = pass
        }
    }

    /**
     * Keluar dari sesi akun saat ini dan menghapus token lokal.
     */
    suspend fun signOut(): Result<Unit> = runCatching {
        auth.signOut()
    }
}
