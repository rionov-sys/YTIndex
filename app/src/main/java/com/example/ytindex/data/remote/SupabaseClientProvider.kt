package com.example.ytindex.data.remote

import android.util.Log
import com.example.ytindex.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.engine.okhttp.OkHttp

/**
 * Singleton provider untuk instance [SupabaseClient] di seluruh aplikasi.
 * Dibuat secara lazy dan tidak pernah melempar crash saat inisialisasi startup.
 */
object SupabaseClientProvider {

    private const val TAG = "SupabaseClientProvider"

    /**
     * Memeriksa apakah konfigurasi Supabase valid:
     * - SUPABASE_URL tidak kosong dan diawali dengan "https://"
     * - SUPABASE_ANON_KEY tidak kosong
     */
    fun isConfigurationValid(): Boolean {
        val url = BuildConfig.SUPABASE_URL.trim()
        val key = BuildConfig.SUPABASE_ANON_KEY.trim()
        return url.isNotBlank() && url.startsWith("https://", ignoreCase = true) && key.isNotBlank()
    }

    @Volatile
    private var instance: SupabaseClient? = null

    /**
     * Mengembalikan instance [SupabaseClient] jika konfigurasi valid dan inisialisasi berhasil,
     * atau null jika belum valid atau terjadi error. Tidak melempar unhandled crash.
     */
    fun getClientOrNull(): SupabaseClient? {
        if (!isConfigurationValid()) {
            Log.w(TAG, "Konfigurasi Supabase belum valid: URL harus diawali https:// dan Anon Key tidak boleh kosong.")
            return null
        }

        instance?.let { return it }

        return synchronized(this) {
            instance?.let { return it }

            try {
                Log.d(TAG, "Membangun SupabaseClient secara lazy...")
                val created = createSupabaseClient(
                    supabaseUrl = BuildConfig.SUPABASE_URL.trim(),
                    supabaseKey = BuildConfig.SUPABASE_ANON_KEY.trim()
                ) {
                    httpEngine = OkHttp.create()

                    install(Auth)
                    install(Postgrest)
                    install(Functions)
                }
                instance = created
                created
            } catch (t: Throwable) {
                Log.e(TAG, "Gagal membuat SupabaseClient: ${t.message}", t)
                null
            }
        }
    }

    /**
     * Properti accessor yang mengembalikan instance [SupabaseClient].
     */
    val client: SupabaseClient
        get() = getClientOrNull() ?: error("SupabaseClient belum siap atau konfigurasi belum valid.")
}
