package com.example.ytindex

import android.app.Application
import com.example.ytindex.data.local.CrashHandler

/**
 * Kelas [Application] utama untuk inisialisasi lingkungan aplikasi YT Index.
 */
class YTIndexApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Pasang perekam crash otomatis untuk menyimpan stack trace saat force close
        CrashHandler.install(this)
    }
}
