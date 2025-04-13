package com.example.memorygamerevolucionarios.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.memorygamerevolucionarios.screens.MainMenuScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main_menu") {
        composable("main_menu") {
            MainMenuScreen(navController = navController)
        }
    }
}

