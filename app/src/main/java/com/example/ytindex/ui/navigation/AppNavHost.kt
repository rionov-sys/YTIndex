package com.example.ytindex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ytindex.data.repository.AuthRepository
import com.example.ytindex.ui.MainScreen
import com.example.ytindex.ui.auth.AuthScreen

/**
 * NavHost utama aplikasi dengan penjaga autentikasi (*Auth Guard*).
 */
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    authRepository: AuthRepository = remember { AuthRepository() }
) {
    val startDestination = if (authRepository.isAuthenticated) {
        Screen.Main.route
    } else {
        Screen.Auth.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Main.route) {
            MainScreen(
                onLoggedOut = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
