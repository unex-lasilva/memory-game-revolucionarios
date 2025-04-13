package com.example.memorygamerevolucionarios.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun RulesDialog(
    onDismiss: () -> Unit
) {
    val rulePages = listOf(
        "📌 Como jogar:\n- Escolha o tamanho do tabuleiro: 4x4, 6x6, 8x8 ou 10x10.\n- Vire duas cartas por vez e tente encontrar os pares.\n- Cada carta tem uma cor de fundo: vermelho, azul, amarelo ou preto.",
        "🎨 Regras das cores:\n- Sempre haverá pelo menos uma carta com fundo preto.\n- Metade das cartas terão fundo azul ou vermelho.\n- As demais cartas terão fundo amarelo.",
        "👥 Sobre os jogadores:\n- Cada jogador recebe uma cor (vermelho ou azul) no início da partida.\n- Seu nome será registrado. Se não escolher um nome, ele será 'PARTICIPANTE01' ou 'PARTICIPANTE02'.\n- Você começa com 0 pontos e pode ganhar ou perder pontos durante o jogo.",
        "🏆 Como marcar pontos:\n- Encontrar um par de cartas amarelas: +1 ponto.\n- Encontrar um par com sua cor: +5 pontos.\n- Encontrar um par com a cor do adversário:\n  - Se errar, perde 2 pontos.\n  - Se acertar, ganha apenas 1 ponto.",
        "❗ Atenção:\n- A pontuação nunca pode ser negativa. Se perder mais pontos do que tem, ficará com 0.\n- Se errar ao tentar encontrar o par de uma carta preta, você perde 50 pontos.\n- Se acertar o par da carta preta são 50 pontos!\n\n🔥 Boa sorte e divirta-se!"
    )

    var currentPage by remember { mutableIntStateOf(0) }

    Dialog(onDismissRequest = onDismiss) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF6200EE),
                            Color(0xFF3700B3)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Título e indicador de página
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Regras",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${currentPage + 1}/${rulePages.size}",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }

                // Divisor
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.5f),
                    thickness = 1.dp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Conteúdo da regra
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = rulePages[currentPage],
                        modifier = Modifier.padding(16.dp),
                        color = Color.White,
                        fontSize = 12.sp,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botões
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            if (currentPage > 0) {
                                currentPage--
                            }
                        },
                        enabled = currentPage > 0
                    ) {
                        Text(text = "Anterior")
                    }

                    Button(
                        onClick = {
                            if (currentPage < rulePages.size - 1) {
                                currentPage++
                            } else {
                                onDismiss()
                            }
                        }
                    ) {
                        Text(text = if (currentPage < rulePages.size - 1) "Próximo" else "Fechar")
                    }
                }
            }
        }
    }
}