package com.example.ytindex.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ytindex.YTIndexApp
import com.example.ytindex.data.VideoResult
import com.example.ytindex.data.local.SettingsDataStore
import com.example.ytindex.data.repository.BookmarkRepository
import com.example.ytindex.data.repository.QuotaExceededException
import com.example.ytindex.data.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

data class SearchUiState(
    val query: String = "",
    val limit: Int = 10,
    val loading: Boolean = false,
    val results: List<VideoResult> = emptyList(),
    val bookmarkedIds: Set<String> = emptySet(),
    val error: String? = null,
    val hasSearched: Boolean = false
)

/**
 * ViewModel untuk layar pencarian video YouTube.
 */
class SearchViewModel(
    private val searchRepository: SearchRepository = SearchRepository(),
    private val bookmarkRepository: BookmarkRepository = BookmarkRepository(),
    private val settingsDataStore: SettingsDataStore? = YTIndexApp.instance?.settingsDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    init {
        // Ambil batas hasil pencarian default dari SettingsDataStore jika tersedia
        viewModelScope.launch {
            if (settingsDataStore != null) {
                val defaultLimit = settingsDataStore.defaultResultLimit.first()
                _state.update { it.copy(limit = defaultLimit) }
            }
            loadBookmarkedIds()
        }
    }

    fun onQueryChange(value: String) {
        _state.update { it.copy(query = value, error = null) }
    }

    fun onLimitChange(value: Int) {
        _state.update { it.copy(limit = value.coerceIn(1, 50)) }
    }

    /**
     * Memuat daftar ID video yang sudah disimpan pengguna.
     */
    fun loadBookmarkedIds() {
        viewModelScope.launch {
            runCatching {
                bookmarkRepository.getBookmarkedVideoIds()
            }.onSuccess { ids ->
                _state.update { it.copy(bookmarkedIds = ids) }
            }
        }
    }

    /**
     * Menjalankan pencarian video.
     */
    fun search() {
        val current = _state.value
        if (current.query.isBlank() || current.loading) return

        _state.update { it.copy(loading = true, error = null) }

        viewModelScope.launch {
            try {
                val videos = searchRepository.searchVideos(current.query, current.limit)
                // Segarkan bookmark IDs
                val bookmarked = runCatching { bookmarkRepository.getBookmarkedVideoIds() }.getOrDefault(emptySet())
                _state.update {
                    it.copy(
                        loading = false,
                        results = videos,
                        bookmarkedIds = bookmarked,
                        hasSearched = true,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        loading = false,
                        results = emptyList(),
                        hasSearched = true,
                        error = e.toUserFriendlyMessage()
                    )
                }
            }
        }
    }

    /**
     * Menjalankan pencarian langsung dengan query tertentu (misal diketuk dari tab Riwayat).
     */
    fun searchWithQuery(newQuery: String) {
        _state.update { it.copy(query = newQuery) }
        search()
    }

    /**
     * Menyimpan atau menghapus bookmark video.
     */
    fun toggleBookmark(video: VideoResult) {
        val currentBookmarked = _state.value.bookmarkedIds
        val isCurrentlyBookmarked = currentBookmarked.contains(video.videoId)

        viewModelScope.launch {
            if (isCurrentlyBookmarked) {
                bookmarkRepository.removeBookmark(video.videoId).onSuccess {
                    _state.update { it.copy(bookmarkedIds = it.bookmarkedIds - video.videoId) }
                }
            } else {
                bookmarkRepository.addBookmark(video).onSuccess {
                    _state.update { it.copy(bookmarkedIds = it.bookmarkedIds + video.videoId) }
                }
            }
        }
    }

    private fun Exception.toUserFriendlyMessage(): String = when (this) {
        is QuotaExceededException ->
            message ?: "Batas kuota harian YouTube Data API telah tercapai (quotaExceeded). Silakan coba lagi besok."
        is IOException ->
            "Tidak ada koneksi internet. Silakan periksa jaringan Anda."
        is IllegalArgumentException ->
            message ?: "Input pencarian tidak valid."
        else ->
            message ?: "Terjadi kesalahan saat mencari video."
    }
}
