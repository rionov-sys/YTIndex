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
 * Membaca URL dan Anon Key dari [BuildConfig], mengonfigurasi engine OkHttp,
 * serta menginstal plugin Auth, Postgrest, dan Functions.
 */
object SupabaseClientProvider {

    private const val TAG = "SupabaseClientProvider"

    val client: SupabaseClient by lazy {
        require(BuildConfig.SUPABASE_URL.isNotBlank() && BuildConfig.SUPABASE_ANON_KEY.isNotBlank()) {
            "SUPABASE_URL dan SUPABASE_ANON_KEY tidak boleh kosong. Periksa file local.properties."
        }

        try {
            Log.d(TAG, "Inisialisasi SupabaseClient...")
            createSupabaseClient(
                supabaseUrl = BuildConfig.SUPABASE_URL,
                supabaseKey = BuildConfig.SUPABASE_ANON_KEY
            ) {
                httpEngine = OkHttp.create()

                install(Auth)
                install(Postgrest)
                install(Functions)
            }
        } catch (t: Throwable) {
            Log.e(TAG, "Gagal membuat SupabaseClient: ${t.message}", t)
            throw t
        }
    }
}
