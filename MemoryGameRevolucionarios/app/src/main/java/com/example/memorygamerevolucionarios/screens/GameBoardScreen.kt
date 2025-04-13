package com.example.memorygamerevolucionarios.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.memorygamerevolucionarios.model.Card
import com.example.memorygamerevolucionarios.model.Player
import com.example.memorygamerevolucionarios.ui.components.CardFace
import com.example.memorygamerevolucionarios.ui.components.FlipCard
import com.example.memorygamerevolucionarios.ui.theme.CardBack
import com.example.memorygamerevolucionarios.ui.theme.CardBlack
import com.example.memorygamerevolucionarios.ui.theme.CardBlue
import com.example.memorygamerevolucionarios.ui.theme.CardRed
import com.example.memorygamerevolucionarios.ui.theme.CardYellow
import com.example.memorygamerevolucionarios.ui.theme.GradientEnd
import com.example.memorygamerevolucionarios.ui.theme.GradientStart
import com.example.memorygamerevolucionarios.viewmodel.GameViewModel

@Composable
fun GameBoardScreen(
    navController: NavController,
    gameViewModel: GameViewModel,
    boardSize: Int,
    player1Name: String,
    player2Name: String
) {
    val gameState by gameViewModel.gameState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        gameViewModel.initializeGame(boardSize, listOf(player1Name, player2Name))
    }

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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "",
            )

            Text(
                text = "Jogador Atual: ${gameState.currentPlayer?.name ?: ""}",
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            gameState.players.getOrNull(0)?.let { player ->
                PlayerScoreView(player = player)
            }

            gameState.players.getOrNull(1)?.let { player ->
                PlayerScoreView(player = player)
            }
        }

        Spacer(modifier = Modifier.height(46.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(gameState.boardSize),
            modifier = Modifier.weight(1f)
        ) {
            items(gameState.cards) { card ->
                MemoryCard(
                    card = card,
                    onCardClick = { gameViewModel.flipCard(card) }
                )
            }
        }
    }
    if (gameState.gameOver) {
        GameOverDialog(
            players = gameState.players,
            onPlayAgain = { navController.navigate("board_size") },
            onMainMenu = { navController.navigate("main_menu") }
        )
    }
}

@Composable
fun PlayerScoreView(player: Player) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(getColorForPlayer(player.color))
        )

        Text(
            text = "${player.name}: ${player.score}",
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun MemoryCard(card: Card, onCardClick: () -> Unit) {
    val cardFace = remember(card.isFlipped, card.isMatched) {
        if (card.isFlipped || card.isMatched) CardFace.Back else CardFace.Front
    }

    Box(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
    ) {
        FlipCard(
            cardFace = cardFace,
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = !card.isMatched && !card.isFlipped) { onCardClick() },
            front = {
                // Carta sem mostrar o valor
                Card(
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardBack
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "?",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                }
            },
            back = {
                // Carta Virada mostrando a cor
                Card(
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = getCardColor(card)
                    ),
                    modifier = Modifier.alpha(if (card.isMatched) 0.5f else 1f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = card.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = getTextColorForCard(card)
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun GameOverDialog(
    players: List<Player>,
    onPlayAgain: () -> Unit,
    onMainMenu: () -> Unit
) {
    val winner = players.maxByOrNull { it.score }

    AlertDialog(
        onDismissRequest = { },
        title = { Text("O Jogo Acabou") },
        text = {
            Column {
                if (winner != null) {
                    Text("${winner.name} ganhou!")
                } else {
                    Text("Vish empatou!")
                }

                Spacer(modifier = Modifier.height(16.dp))

                players.forEach { player ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(getColorForPlayer(player.color))
                        )

                        Text(
                            text = "${player.name}: ${player.score}",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onPlayAgain) {
                Text("Jogar de novo")
            }
        },
        dismissButton = {
            Button(onClick = onMainMenu) {
                Text("Voltar para o Menu")
            }
        }
    )
}

@Composable
fun getTextColorForCard(card: Card): Color {
    return when (card.color) {
        "pr" -> Color.White
        "am" -> Color.Black
        else -> Color.White
    }
}

@Composable
fun getCardColor(card: Card): Color {
    return when (card.color) {
        "az" -> CardBlue
        "ve" -> CardRed
        "am" -> CardYellow
        "pr" -> CardBlack
        else -> CardBack
    }
}

@Composable
fun getColorForPlayer(color: String): Color {
    return when (color) {
        "az" -> CardBlue
        "ve" -> CardRed
        "am" -> CardYellow
        "pr" -> CardBlack
        else -> CardBack
    }
}

