package com.example.ytindex.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ytindex.data.VideoRepository
import com.example.ytindex.data.VideoResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

data class SearchUiState(
    val query: String = "",
    val limit: Int = 10,
    val loading: Boolean = false,
    val results: List<VideoResult> = emptyList(),
    val error: String? = null,
    val hasSearched: Boolean = false
)

class SearchViewModel(
    private val repository: VideoRepository = VideoRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    fun onQueryChange(value: String) = _state.update { it.copy(query = value) }

    fun onLimitChange(value: Int) = _state.update { it.copy(limit = value.coerceIn(1, 50)) }

    fun search() {
        val current = _state.value
        if (current.query.isBlank() || current.loading) return

        _state.update { it.copy(loading = true, error = null) }

        viewModelScope.launch {
            try {
                val videos = repository.search(current.query, current.limit)
                _state.update {
                    it.copy(loading = false, results = videos, hasSearched = true)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(loading = false, results = emptyList(), hasSearched = true, error = e.toMessage())
                }
            }
        }
    }

    private fun Exception.toMessage(): String = when (this) {
        is HttpException -> when (code()) {
            403 -> "Akses ditolak atau kuota harian YouTube API habis (403). Coba lagi besok atau cek API key."
            400 -> "Permintaan tidak valid (400). Periksa API key dan kata kunci."
            else -> "Server YouTube mengembalikan error ${code()}."
        }
        is IOException -> "Tidak ada koneksi internet."
        is IllegalArgumentException -> message ?: "Input tidak valid."
        else -> "Terjadi kesalahan: ${message ?: "tidak diketahui"}"
    }
}
