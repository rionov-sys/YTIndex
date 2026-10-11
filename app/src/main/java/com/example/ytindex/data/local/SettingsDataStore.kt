package com.example.ytindex.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ytindex_settings")

/**
 * Pilihan tema visual aplikasi.
 */
enum class AppThemeMode(val title: String) {
    SYSTEM("Ikuti Sistem"),
    LIGHT("Mode Terang"),
    DARK("Mode Gelap")
}

/**
 * Penyimpanan pengaturan lokal aplikasi berbasis Jetpack DataStore Preferences.
 */
class SettingsDataStore(private val context: Context) {

    companion object {
        private val KEY_DEFAULT_LIMIT = intPreferencesKey("default_result_limit")
        private val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
        const val DEFAULT_LIMIT = 10

        @Volatile
        private var INSTANCE: SettingsDataStore? = null

        fun getInstance(context: Context): SettingsDataStore {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SettingsDataStore(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    val defaultResultLimit: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[KEY_DEFAULT_LIMIT] ?: DEFAULT_LIMIT
    }

    val themeMode: Flow<AppThemeMode> = context.dataStore.data.map { preferences ->
        val raw = preferences[KEY_THEME_MODE] ?: AppThemeMode.SYSTEM.name
        try {
            AppThemeMode.valueOf(raw)
        } catch (_: Exception) {
            AppThemeMode.SYSTEM
        }
    }

    suspend fun setDefaultResultLimit(limit: Int) {
        val clamped = limit.coerceIn(1, 50)
        context.dataStore.edit { preferences ->
            preferences[KEY_DEFAULT_LIMIT] = clamped
        }
    }

    suspend fun setThemeMode(mode: AppThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[KEY_THEME_MODE] = mode.name
        }
    }
}
