package com.example.ytindex.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Definisi rute layar dan navigasi di dalam aplikasi YT Index.
 */
sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object Main : Screen("main")
}

/**
 * Definisi tab utama pada bilah navigasi bawah (Bottom Navigation Bar).
 * Menggunakan enum class untuk mencegah masalah circular static initialization.
 */
enum class BottomNavTab(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    Search(
        route = "tab_search",
        title = "Cari",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search
    ),

    Bookmarks(
        route = "tab_bookmarks",
        title = "Tersimpan",
        selectedIcon = Icons.Filled.Bookmark,
        unselectedIcon = Icons.Outlined.BookmarkBorder
    ),

    History(
        route = "tab_history",
        title = "Riwayat",
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History
    ),

    Settings(
        route = "tab_settings",
        title = "Pengaturan",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    );

    companion object {
        val items: List<BottomNavTab>
            get() = entries
    }
}
