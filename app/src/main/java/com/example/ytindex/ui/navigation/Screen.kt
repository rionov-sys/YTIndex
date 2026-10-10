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
    object Auth : Screen("auth")
    object Main : Screen("main")
}

/**
 * Definisi tab utama pada bilah navigasi bawah (Bottom Navigation Bar).
 */
sealed class BottomNavTab(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Search : BottomNavTab(
        route = "tab_search",
        title = "Cari",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search
    )

    object Bookmarks : BottomNavTab(
        route = "tab_bookmarks",
        title = "Tersimpan",
        selectedIcon = Icons.Filled.Bookmark,
        unselectedIcon = Icons.Outlined.BookmarkBorder
    )

    object History : BottomNavTab(
        route = "tab_history",
        title = "Riwayat",
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History
    )

    object Settings : BottomNavTab(
        route = "tab_settings",
        title = "Pengaturan",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )

    companion object {
        val items = listOf(Search, Bookmarks, History, Settings)
    }
}
