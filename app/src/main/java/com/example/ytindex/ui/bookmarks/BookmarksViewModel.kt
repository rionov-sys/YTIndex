package com.example.ytindex.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ytindex.data.BookmarkDto
import com.example.ytindex.data.VideoResult
import com.example.ytindex.data.repository.BookmarkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookmarksUiState(
    val isLoading: Boolean = false,
    val bookmarks: List<VideoResult> = emptyList(),
    val errorMessage: String? = null
)

/**
 * ViewModel untuk mengelola daftar video tersimpan (bookmark).
 */
class BookmarksViewModel(
    private val bookmarkRepository: BookmarkRepository = BookmarkRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(BookmarksUiState())
    val state: StateFlow<BookmarksUiState> = _state.asStateFlow()

    init {
        loadBookmarks()
    }

    /**
     * Memuat daftar bookmark dari database Supabase.
     */
    fun loadBookmarks() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            runCatching {
                bookmarkRepository.getBookmarks()
            }.fold(
                onSuccess = { dtoList ->
                    val domainList = dtoList.map { it.toVideoResult() }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            bookmarks = domainList,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Gagal memuat bookmark."
                        )
                    }
                }
            )
        }
    }

    /**
     * Menghapus video dari daftar bookmark pengguna.
     */
    fun removeBookmark(videoId: String) {
        viewModelScope.launch {
            bookmarkRepository.removeBookmark(videoId).onSuccess {
                _state.update { current ->
                    current.copy(bookmarks = current.bookmarks.filter { it.videoId != videoId })
                }
            }
        }
    }

    private fun BookmarkDto.toVideoResult(): VideoResult = VideoResult(
        videoId = videoId,
        title = title ?: "",
        channel = channel ?: "",
        publishedAt = publishedAt ?: "",
        publishedDate = (publishedAt ?: "").take(10),
        thumbnailUrl = thumbnailUrl ?: "",
        url = url ?: "https://www.youtube.com/watch?v=$videoId",
        isBookmarked = true
    )
}
