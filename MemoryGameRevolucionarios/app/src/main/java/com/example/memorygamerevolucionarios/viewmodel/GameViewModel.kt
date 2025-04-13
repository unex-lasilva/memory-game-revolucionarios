package com.example.memorygamerevolucionarios.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.memorygamerevolucionarios.model.Card
import com.example.memorygamerevolucionarios.model.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var flippedCards = mutableListOf<Card>()
    private var gameTimerJob: kotlinx.coroutines.Job? = null

    fun initializeGame(boardSize: Int, playerNames: List<String>) {
        val totalPairs = boardSize * boardSize / 2
        val totalRedBlue = totalPairs / 2
        val redPairs = totalRedBlue / 2
        val bluePairs = totalRedBlue - redPairs
        val blackPairs = 1
        val yellowPairs = totalPairs - (redPairs + bluePairs + blackPairs)

        val cardNames = mutableListOf<String>()

        fun formatNumber(i: Int) = String.format("%02d", i)

        for (i in 1..bluePairs) {
            cardNames.add("Az${formatNumber(i)}")
        }

        for (i in 1..redPairs) {
            cardNames.add("Ve${formatNumber(i)}")
        }

        for (i in 1..yellowPairs) {
            cardNames.add("Am${formatNumber(i)}")
        }

        cardNames.add("Pr01")

        val shuffledCardNames = (cardNames + cardNames).shuffled()

        val cards = shuffledCardNames.mapIndexed { index, name ->
            Card(id = index, name = name)
        }

        val players = playerNames.mapIndexed { index, name ->
            Player(name, if (index == 0) "az" else "ve")
        }

        _gameState.update {
            it.copy(
                cards = cards,
                players = players,
                currentPlayerIndex = 0,
                boardSize = boardSize,
                gameOver = false,
                elapsedTimeSeconds = 0
            )
        }
    }

    fun flipCard(card: Card) {
        val currentState = _gameState.value

        if (flippedCards.size == 2 || card.isMatched || card.isFlipped) return

        // Aqui vem a logica de virar a carta
    }

    private fun checkForMatch() {
        val (card1, card2) = flippedCards
        val currentState = _gameState.value

       // Comparar as cartas pra saber se são iguais ou diferentes
    }

    private fun updateScore(color: String) {
        val currentState = _gameState.value
        val currentPlayerIndex = currentState.currentPlayerIndex

        // Atualizar o score do jogador
    }

    private fun switchPlayer() {
        val currentState = _gameState.value
        val currentPlayerIndex = currentState.currentPlayerIndex
        var nextPlayerIndex = (currentPlayerIndex + 1) % currentState.players.size

       // Interação de trocar a vez dos jogadores
    }

    private fun checkGameOver() {
        val currentState = _gameState.value
        val allMatched = currentState.cards.all { it.isMatched }

        // Ver se o jogo já acabou

    }


    override fun onCleared() {
        super.onCleared()
        gameTimerJob?.cancel()
    }

    data class GameState(
        val cards: List<Card> = emptyList(),
        val players: List<Player> = emptyList(),
        val currentPlayerIndex: Int = 0,
        val boardSize: Int = 4,
        val gameOver: Boolean = false,
        val elapsedTimeSeconds: Int = 0
    ) {
        val currentPlayer: Player?
            get() = players.getOrNull(currentPlayerIndex)

    }
}

