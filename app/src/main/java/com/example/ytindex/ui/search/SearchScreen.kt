package com.example.ytindex.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ytindex.ui.components.EmptyStateView
import com.example.ytindex.ui.components.ErrorStateView
import com.example.ytindex.ui.components.LoadingIndicatorView
import com.example.ytindex.ui.components.VideoCard

/**
 * Layar utama pencarian video YouTube berdasarkan kata kunci dan slider batasan hasil.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    vm: SearchViewModel
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "YT Index",
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
            // Kontrol Panel Pencarian (Search Input & Slider Limit)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = state.query,
                        onValueChange = vm::onQueryChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Kata kunci") },
                        placeholder = { Text("mis. belajar membuat aplikasi android") },
                        leadingIcon = {
                            Icon(Icons.Filled.Search, contentDescription = null)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                focusManager.clearFocus()
                                vm.search()
                            }
                        )
                    )

                    // Slider Batas Hasil (1–50)
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Batas hasil pencarian",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${state.limit} video",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Slider(
                            value = state.limit.toFloat(),
                            onValueChange = { vm.onLimitChange(it.toInt()) },
                            valueRange = 1f..50f,
                            steps = 48
                        )
                    }

                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            vm.search()
                        },
                        enabled = !state.loading && state.query.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            text = "Cari Video (1 Tahun Terakhir)",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Hasil Pencarian / Status
            when {
                state.loading -> {
                    LoadingIndicatorView(message = "Mencari video dan memeriksa cache...")
                }

                state.error != null -> {
                    ErrorStateView(
                        errorMessage = state.error ?: "",
                        onRetry = vm::search
                    )
                }

                state.hasSearched && state.results.isEmpty() -> {
                    EmptyStateView(
                        title = "Tidak ada video ditemukan",
                        subtitle = "Coba gunakan kata kunci lain yang lebih umum.",
                        icon = Icons.Outlined.SearchOff
                    )
                }

                state.results.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(
                            items = state.results,
                            key = { _, item -> item.videoId }
                        ) { index, item ->
                            val isBookmarked = state.bookmarkedIds.contains(item.videoId)
                            VideoCard(
                                video = item,
                                rank = index + 1,
                                isBookmarked = isBookmarked,
                                onBookmarkToggle = { vm.toggleBookmark(item) }
                            )
                        }
                    }
                }

                else -> {
                    EmptyStateView(
                        title = "Mulai Pencarian",
                        subtitle = "Ketik kata kunci untuk menemukan video YouTube terbaru 1 tahun terakhir.",
                        icon = Icons.Filled.Search
                    )
                }
            }
        }
    }
}
