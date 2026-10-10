package com.example.ytindex

import android.app.Application
import android.util.Log

/**
 * Kelas [Application] utama untuk inisialisasi lingkungan aplikasi YT Index.
 */
class YTIndexApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Pasang logging uncaught exception agar stacktrace error tercatat jelas di logcat
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("YTIndexApp", "FATAL CRASH di thread ${thread.name}: ${throwable.message}", throwable)
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}
