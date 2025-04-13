package com.example.memorygamerevolucionarios.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.memorygamerevolucionarios.model.Player
import com.example.memorygamerevolucionarios.ui.theme.GradientEnd
import com.example.memorygamerevolucionarios.ui.theme.GradientStart
import com.example.memorygamerevolucionarios.viewmodel.GameViewModel

@Composable
fun ScoresScreen(navController: NavController, gameViewModel: GameViewModel) {
    val highScores = gameViewModel.getHighScores().sortedByDescending { it.score }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(GradientStart, GradientEnd)
                )
            )
            .padding(16.dp)
    ) {
        Text(
            text = "Historico de Pontuações",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (highScores.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhuma pontuação salva ainda",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        } else {
            LazyColumn {
                items(highScores) { player ->
                    ScoreItem(player = player)
                }
            }
        }
    }
}

@Composable
fun ScoreItem(player: Player) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(getColorForPlayer(player.color))
            )

            Text(
                text = player.name,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Text(
            text = player.score.toString(),
            fontSize = 18.sp,
            color = Color.White
        )
    }
}
