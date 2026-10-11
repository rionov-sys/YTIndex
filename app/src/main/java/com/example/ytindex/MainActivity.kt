package com.example.ytindex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.activity.viewModels
import com.example.ytindex.data.local.AppThemeMode
import com.example.ytindex.data.local.CrashHandler
import com.example.ytindex.data.remote.SupabaseClientProvider
import com.example.ytindex.ui.components.ConfigMissingScreen
import com.example.ytindex.ui.components.CrashReportScreen
import com.example.ytindex.ui.navigation.AppNavHost
import com.example.ytindex.ui.settings.SettingsViewModel
import com.example.ytindex.ui.theme.YTIndexTheme

/**
 * Entry point utama aplikasi Android YT Index.
 * Memeriksa adanya laporan kerusakan (crash report) atau ketidaksesuaian konfigurasi Supabase
 * sebelum memuat navigasi utama [AppNavHost].
 */
class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val settingsState by settingsViewModel.state.collectAsStateWithLifecycle()
            val themeMode = settingsState.currentTheme

            // 1. Cek apakah ada crash report dari peluncuran sebelumnya
            val savedCrashReport = remember {
                mutableStateOf(CrashHandler.getCrashReport(this@MainActivity))
            }

            YTIndexTheme(themeMode = themeMode) {
                val crashReport = savedCrashReport.value
                when {
                    crashReport != null -> {
                        // Tampilkan layar crash report sebelum apapun
                        CrashReportScreen(
                            reportText = crashReport,
                            onDismiss = {
                                CrashHandler.clearCrashReport(this@MainActivity)
                                savedCrashReport.value = null
                            }
                        )
                    }
                    !SupabaseClientProvider.isConfigurationValid() -> {
                        // Tampilkan layar konfigurasi belum diisi
                        ConfigMissingScreen()
                    }
                    else -> {
                        // Aplikasi normal
                        AppNavHost()
                    }
                }
            }
        }
    }
}
