package com.example.memorygamerevolucionarios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.memorygamerevolucionarios.navigation.AppNavigation
import com.example.memorygamerevolucionarios.ui.theme.MemoryGameRevolucionariosTheme
import com.example.memorygamerevolucionarios.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemoryGameRevolucionariosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val gameViewModel: GameViewModel = viewModel()
                    AppNavigation(navController = navController, gameViewModel = gameViewModel)
                }
            }
        }
    }
}

