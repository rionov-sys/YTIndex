package com.example.ytindex.data.local

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import com.example.ytindex.BuildConfig
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Perekam kerusakan aplikasi (*Crash Reporter*) mandiri tanpa library pihak ketiga.
 * Menangkap seluruh [UncaughtException], mencatat stack trace lengkap, versi aplikasi,
 * dan timestamp ke [SharedPreferences] menggunakan `commit()` sinkron sebelum aplikasi dimatikan.
 */
object CrashHandler {
    private const val PREFS_NAME = "ytindex_crash_reporter"
    private const val KEY_CRASH_REPORT = "last_crash_report"

    fun install(application: Application) {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                val sw = StringWriter()
                val pw = PrintWriter(sw)
                throwable.printStackTrace(pw)
                val stackTrace = sw.toString()

                val timeStr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                val report = buildString {
                    appendLine("=== YT INDEX CRASH REPORT ===")
                    appendLine("Waktu: $timeStr")
                    appendLine("Versi: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
                    appendLine("Perangkat: ${Build.MANUFACTURER} ${Build.MODEL} (Android API ${Build.VERSION.SDK_INT})")
                    appendLine("Thread: ${thread.name} (id: ${thread.id})")
                    appendLine("Exception: ${throwable.javaClass.name}: ${throwable.message}")
                    appendLine()
                    appendLine("--- STACK TRACE ---")
                    appendLine(stackTrace)
                }

                val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                prefs.edit().putString(KEY_CRASH_REPORT, report).commit()
                Log.e("CrashHandler", "Crash berhasil disimpan:\n$report")
            } catch (e: Throwable) {
                Log.e("CrashHandler", "Gagal menyimpan crash report", e)
            } finally {
                defaultHandler?.uncaughtException(thread, throwable)
            }
        }
    }

    fun getCrashReport(context: Context): String? {
        return try {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.getString(KEY_CRASH_REPORT, null)
        } catch (e: Throwable) {
            null
        }
    }

    fun clearCrashReport(context: Context) {
        try {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().remove(KEY_CRASH_REPORT).commit()
        } catch (e: Throwable) {
            Log.e("CrashHandler", "Gagal menghapus crash report", e)
        }
    }
}
