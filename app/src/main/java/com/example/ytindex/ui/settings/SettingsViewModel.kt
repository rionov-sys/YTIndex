package com.example.ytindex.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ytindex.YTIndexApp
import com.example.ytindex.data.local.AppThemeMode
import com.example.ytindex.data.local.SettingsDataStore
import com.example.ytindex.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val userEmail: String? = null,
    val defaultLimit: Int = 10,
    val currentTheme: AppThemeMode = AppThemeMode.SYSTEM,
    val isLoggingOut: Boolean = false,
    val isLoggedOut: Boolean = false
)

/**
 * ViewModel untuk mengelola konfigurasi preferensi pengguna dan aksi logout.
 */
class SettingsViewModel(
    private val settingsDataStore: SettingsDataStore? = YTIndexApp.instance?.settingsDataStore,
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        // Ambil email akun
        _state.update { it.copy(userEmail = authRepository.currentEmail) }

        // Pantau pengaturan default limit dari DataStore
        viewModelScope.launch {
            settingsDataStore?.defaultResultLimit?.collect { limit ->
                _state.update { it.copy(defaultLimit = limit) }
            }
        }

        // Pantau tema dari DataStore
        viewModelScope.launch {
            settingsDataStore?.themeMode?.collect { mode ->
                _state.update { it.copy(currentTheme = mode) }
            }
        }
    }

    fun onDefaultLimitChange(newLimit: Int) {
        viewModelScope.launch {
            settingsDataStore?.setDefaultResultLimit(newLimit)
        }
    }

    fun onThemeChange(mode: AppThemeMode) {
        viewModelScope.launch {
            settingsDataStore?.setThemeMode(mode)
        }
    }

    fun logout() {
        _state.update { it.copy(isLoggingOut = true) }
        viewModelScope.launch {
            authRepository.signOut().fold(
                onSuccess = {
                    _state.update { it.copy(isLoggingOut = false, isLoggedOut = true) }
                },
                onFailure = {
                    _state.update { it.copy(isLoggingOut = false, isLoggedOut = true) }
                }
            )
        }
    }
}
