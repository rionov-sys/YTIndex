package com.example.ytindex.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ytindex.data.SearchHistoryDto
import com.example.ytindex.data.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HistoryUiState(
    val isLoading: Boolean = false,
    val historyList: List<SearchHistoryDto> = emptyList(),
    val errorMessage: String? = null,
    val showDeleteConfirmDialog: Boolean = false
)

/**
 * ViewModel untuk mengelola riwayat pencarian pengguna.
 */
class HistoryViewModel(
    private val historyRepository: HistoryRepository = HistoryRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryUiState())
    val state: StateFlow<HistoryUiState> = _state.asStateFlow()

    init {
        loadHistory()
    }

    /**
     * Memuat riwayat pencarian dari database Supabase.
     */
    fun loadHistory() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            runCatching {
                historyRepository.getHistory()
            }.fold(
                onSuccess = { list ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            historyList = list,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Gagal memuat riwayat pencarian."
                        )
                    }
                }
            )
        }
    }

    fun openDeleteConfirmDialog() {
        _state.update { it.copy(showDeleteConfirmDialog = true) }
    }

    fun closeDeleteConfirmDialog() {
        _state.update { it.copy(showDeleteConfirmDialog = false) }
    }

    /**
     * Menghapus seluruh riwayat pencarian pengguna.
     */
    fun clearAllHistory() {
        closeDeleteConfirmDialog()
        viewModelScope.launch {
            historyRepository.clearAllHistory().onSuccess {
                _state.update { it.copy(historyList = emptyList()) }
            }
        }
    }

    /**
     * Menghapus satu entri riwayat.
     */
    fun deleteItem(id: String) {
        viewModelScope.launch {
            historyRepository.deleteHistoryItem(id).onSuccess {
                _state.update { current ->
                    current.copy(historyList = current.historyList.filter { it.id != id })
                }
            }
        }
    }
}
