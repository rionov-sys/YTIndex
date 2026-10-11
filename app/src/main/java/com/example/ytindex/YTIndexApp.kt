package com.example.ytindex

import android.app.Application
import com.example.ytindex.data.local.CrashHandler
import com.example.ytindex.data.local.SettingsDataStore

/**
 * Kelas [Application] utama untuk inisialisasi lingkungan aplikasi YT Index.
 * Menginisialisasi komponen berumur seumur aplikasi (SettingsDataStore, CrashHandler).
 */
class YTIndexApp : Application() {

    lateinit var settingsDataStore: SettingsDataStore
        private set

    override fun onCreate() {
        super.onCreate()
        _instance = this
        settingsDataStore = SettingsDataStore.getInstance(this)
        // Pasang perekam crash otomatis untuk menyimpan stack trace saat force close
        CrashHandler.install(this)
    }

    companion object {
        private var _instance: YTIndexApp? = null
        val instance: YTIndexApp?
            get() = _instance
    }
}
