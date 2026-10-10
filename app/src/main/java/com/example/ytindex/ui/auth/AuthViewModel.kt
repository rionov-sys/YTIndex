package com.example.ytindex.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ytindex.data.repository.AuthRepository
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoginMode: Boolean = true,
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isAuthenticated: Boolean = false
)

/**
 * ViewModel untuk mengelola interaksi autentikasi login dan pendaftaran pengguna.
 */
class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    init {
        // Pantau status sesi Supabase untuk mendeteksi login otomatis
        viewModelScope.launch {
            authRepository.sessionStatus.collect { status ->
                val authenticated = status is SessionStatus.Authenticated
                _state.update { it.copy(isAuthenticated = authenticated) }
            }
        }
    }

    fun onEmailChange(newEmail: String) {
        _state.update { it.copy(email = newEmail, errorMessage = null) }
    }

    fun onPasswordChange(newPassword: String) {
        _state.update { it.copy(password = newPassword, errorMessage = null) }
    }

    fun toggleAuthMode() {
        _state.update {
            it.copy(
                isLoginMode = !it.isLoginMode,
                errorMessage = null
            )
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun submit() {
        val current = _state.value
        val email = current.email.trim()
        val password = current.password

        if (email.isBlank() || !email.contains("@")) {
            _state.update { it.copy(errorMessage = "Format alamat email tidak valid.") }
            return
        }

        if (password.length < 6) {
            _state.update { it.copy(errorMessage = "Kata sandi minimal harus 6 karakter.") }
            return
        }

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = if (current.isLoginMode) {
                authRepository.signIn(email, password)
            } else {
                authRepository.signUp(email, password)
            }

            result.fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    val userFriendlyMsg = mapAuthError(error)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = userFriendlyMsg
                        )
                    }
                }
            )
        }
    }

    private fun mapAuthError(throwable: Throwable): String {
        val msg = throwable.message?.lowercase() ?: ""
        return when {
            msg.contains("invalid login credentials") || msg.contains("invalid_credentials") ->
                "Email atau kata sandi salah. Silakan periksa kembali."
            msg.contains("user already registered") ->
                "Email ini sudah terdaftar. Silakan masuk menggunakan tab Masuk."
            msg.contains("signup is disabled") || msg.contains("signups not allowed") ->
                "Pendaftaran akun baru dinonaktifkan oleh administrator."
            msg.contains("network") || msg.contains("unable to resolve host") ->
                "Tidak ada koneksi internet. Silakan periksa jaringan Anda."
            else ->
                throwable.message ?: "Terjadi kesalahan autentikasi."
        }
    }
}
