package com.example.memorygamerevolucionarios.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.memorygamerevolucionarios.ui.theme.GradientEnd
import com.example.memorygamerevolucionarios.ui.theme.GradientStart

@Composable
fun BoardSizeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(GradientStart, GradientEnd)
                )
            )
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Escolha o tamanho do tabuleiro",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.ui.graphics.Color.White
        )

        Spacer(modifier = Modifier.height(64.dp))

        Button(
            onClick = { navController.navigate("player_registration/4") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "4x4", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("player_registration/6") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "6x6", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("player_registration/8") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "8x8", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("player_registration/10") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "10x10", fontSize = 18.sp)
        }
    }
}

