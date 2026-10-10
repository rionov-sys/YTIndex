package com.example.ytindex.ui.bookmarks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ytindex.ui.components.EmptyStateView
import com.example.ytindex.ui.components.ErrorStateView
import com.example.ytindex.ui.components.LoadingIndicatorView
import com.example.ytindex.ui.components.VideoCard

/**
 * Layar tab Tersimpan untuk menampilkan video yang telah dibookmark oleh pengguna.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    vm: BookmarksViewModel
) {
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.loadBookmarks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Video Tersimpan",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> {
                    LoadingIndicatorView(message = "Memuat daftar video tersimpan...")
                }

                state.errorMessage != null -> {
                    ErrorStateView(
                        errorMessage = state.errorMessage ?: "",
                        onRetry = vm::loadBookmarks
                    )
                }

                state.bookmarks.isEmpty() -> {
                    EmptyStateView(
                        title = "Belum Ada Video Tersimpan",
                        subtitle = "Simpan video favorit Anda dengan mengetuk ikon bookmark pada kartu hasil pencarian.",
                        icon = Icons.Outlined.BookmarkBorder
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.bookmarks,
                            key = { it.videoId }
                        ) { video ->
                            VideoCard(
                                video = video,
                                isBookmarked = true,
                                onBookmarkToggle = { vm.removeBookmark(video.videoId) }
                            )
                        }
                    }
                }
            }
        }
    }
}
