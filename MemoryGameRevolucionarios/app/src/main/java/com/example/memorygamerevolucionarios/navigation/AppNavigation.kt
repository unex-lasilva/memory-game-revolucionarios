package com.example.memorygamerevolucionarios.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.memorygamerevolucionarios.screens.BoardSizeScreen
import com.example.memorygamerevolucionarios.screens.GameBoardScreen
import com.example.memorygamerevolucionarios.screens.MainMenuScreen
import com.example.memorygamerevolucionarios.screens.PlayerRegistrationScreen
import com.example.memorygamerevolucionarios.screens.ScoresScreen
import com.example.memorygamerevolucionarios.viewmodel.GameViewModel

@Composable
fun AppNavigation(navController: NavHostController, gameViewModel: GameViewModel) {
    NavHost(navController = navController, startDestination = "main_menu") {
        composable("main_menu") {
            MainMenuScreen(navController = navController)
        }

        composable("board_size") {
            BoardSizeScreen(navController = navController)
        }

        composable(
            "player_registration/{boardSize}",
            arguments = listOf(navArgument("boardSize") { type = NavType.IntType })
        ) { backStackEntry ->
            val boardSize = backStackEntry.arguments?.getInt("boardSize") ?: 4
            PlayerRegistrationScreen(
                navController = navController,
                boardSize = boardSize
            )
        }

        composable("scores") {
            ScoresScreen(navController = navController, gameViewModel = gameViewModel)
        }

        composable(
            "game_board/{boardSize}/{player1Name}/{player2Name}",
            arguments = listOf(
                navArgument("boardSize") { type = NavType.IntType },
                navArgument("player1Name") { type = NavType.StringType },
                navArgument("player2Name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val boardSize = backStackEntry.arguments?.getInt("boardSize") ?: 4
            val player1Name = backStackEntry.arguments?.getString("player1Name") ?: "PARTICIPANTE01"
            val player2Name = backStackEntry.arguments?.getString("player2Name") ?: "PARTICIPANTE02"

            GameBoardScreen(
                navController = navController,
                gameViewModel = gameViewModel,
                boardSize = boardSize,
                player1Name = player1Name,
                player2Name = player2Name
            )
        }
    }
}

