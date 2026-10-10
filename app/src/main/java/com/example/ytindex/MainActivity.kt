package com.example.ytindex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ytindex.data.local.AppThemeMode
import com.example.ytindex.data.local.SettingsDataStore
import com.example.ytindex.ui.navigation.AppNavHost
import com.example.ytindex.ui.theme.YTIndexTheme

/**
 * Entry point utama aplikasi Android YT Index.
 * Mengamati tema pengguna secara dinamis dari [SettingsDataStore] dan menampilkan [AppNavHost].
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settingsDataStore = SettingsDataStore(applicationContext)

        setContent {
            val themeMode by settingsDataStore.themeMode.collectAsStateWithLifecycle(
                initialValue = AppThemeMode.SYSTEM
            )

            YTIndexTheme(themeMode = themeMode) {
                AppNavHost()
            }
        }
    }
}
