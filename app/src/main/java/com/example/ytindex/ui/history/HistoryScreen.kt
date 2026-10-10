package com.example.ytindex.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ytindex.ui.components.EmptyStateView
import com.example.ytindex.ui.components.ErrorStateView
import com.example.ytindex.ui.components.LoadingIndicatorView

/**
 * Layar tab Riwayat untuk menampilkan pencarian terdahulu dan fitur pencarian ulang.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    vm: HistoryViewModel,
    onQuerySelected: (String) -> Unit
) {
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.loadHistory()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Riwayat Pencarian",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                actions = {
                    if (state.historyList.isNotEmpty()) {
                        IconButton(onClick = vm::openDeleteConfirmDialog) {
                            Icon(
                                imageVector = Icons.Filled.DeleteSweep,
                                contentDescription = "Hapus Semua Riwayat",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
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
                    LoadingIndicatorView(message = "Memuat riwayat pencarian...")
                }

                state.errorMessage != null -> {
                    ErrorStateView(
                        errorMessage = state.errorMessage ?: "",
                        onRetry = vm::loadHistory
                    )
                }

                state.historyList.isEmpty() -> {
                    EmptyStateView(
                        title = "Belum Ada Riwayat",
                        subtitle = "Riwayat pencarian Anda akan otomatis tersimpan di sini.",
                        icon = Icons.Outlined.History
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.historyList,
                            key = { it.id ?: it.query + it.createdAt }
                        ) { historyItem ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onQuerySelected(historyItem.query) },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.History,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = historyItem.query,
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    fontWeight = FontWeight.SemiBold
                                                ),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            val date = (historyItem.createdAt ?: "").take(10)
                                            val limitInfo = if (historyItem.resultLimit != null) {
                                                " • Batas: ${historyItem.resultLimit} video"
                                            } else ""
                                            Text(
                                                text = "$date$limitInfo",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                        }
                                    }

                                    if (historyItem.id != null) {
                                        IconButton(onClick = { vm.deleteItem(historyItem.id) }) {
                                            Icon(
                                                imageVector = Icons.Filled.Close,
                                                contentDescription = "Hapus",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Dialog Konfirmasi Hapus Semua
        if (state.showDeleteConfirmDialog) {
            AlertDialog(
                onDismissRequest = vm::closeDeleteConfirmDialog,
                title = { Text("Hapus Semua Riwayat?") },
                text = { Text("Seluruh riwayat kata kunci pencarian Anda akan dihapus permanen dari akun.") },
                confirmButton = {
                    TextButton(onClick = vm::clearAllHistory) {
                        Text("Hapus", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = vm::closeDeleteConfirmDialog) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}
