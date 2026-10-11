package com.example.ytindex.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ytindex.ui.bookmarks.BookmarksScreen
import com.example.ytindex.ui.bookmarks.BookmarksViewModel
import com.example.ytindex.ui.components.BottomNavBar
import com.example.ytindex.ui.history.HistoryScreen
import com.example.ytindex.ui.history.HistoryViewModel
import com.example.ytindex.ui.navigation.BottomNavTab
import com.example.ytindex.ui.search.SearchScreen
import com.example.ytindex.ui.search.SearchViewModel
import com.example.ytindex.ui.settings.SettingsScreen
import com.example.ytindex.ui.settings.SettingsViewModel

/**
 * Layar kontainer utama aplikasi setelah pengguna login, memuat bilah navigasi bawah (4 tab).
 */
@Composable
fun MainScreen(
    onLoggedOut: () -> Unit,
    searchVm: SearchViewModel = viewModel(),
    bookmarksVm: BookmarksViewModel = viewModel(),
    historyVm: HistoryViewModel = viewModel(),
    settingsVm: SettingsViewModel = viewModel()
) {
    var selectedTab by rememberSaveable { mutableStateOf(BottomNavTab.Search.route) }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab.route
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                BottomNavTab.Search.route -> {
                    SearchScreen(vm = searchVm)
                }

                BottomNavTab.Bookmarks.route -> {
                    BookmarksScreen(vm = bookmarksVm)
                }

                BottomNavTab.History.route -> {
                    HistoryScreen(
                        vm = historyVm,
                        onQuerySelected = { query ->
                            selectedTab = BottomNavTab.Search.route
                            searchVm.searchWithQuery(query)
                        }
                    )
                }

                BottomNavTab.Settings.route -> {
                    SettingsScreen(
                        vm = settingsVm,
                        onLogoutSuccess = onLoggedOut
                    )
                }
            }
        }
    }
}
