package com.example.memorygamerevolucionarios.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.memorygamerevolucionarios.data.PreferencesManager
import com.example.memorygamerevolucionarios.model.Card
import com.example.memorygamerevolucionarios.model.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencesManager = PreferencesManager(application)
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
            )
        }
    }

    fun flipCard(card: Card) {
        val currentState = _gameState.value

        if (flippedCards.size == 2 || card.isMatched || card.isFlipped) return

        val updatedCards = currentState.cards.map {
            if (it.id == card.id) it.copy(isFlipped = true) else it
        }

        _gameState.update { it.copy(cards = updatedCards) }

        val flippedCard = updatedCards.first { it.id == card.id }
        flippedCards.add(flippedCard)

        if (flippedCards.size == 2) {
            checkForMatch()
        }
    }

    private fun checkForMatch() {
        val (card1, card2) = flippedCards
        val currentState = _gameState.value
        val currentPlayer = currentState.players[currentState.currentPlayerIndex]
        if (card1.name == card2.name) {

            val updatedCards = currentState.cards.map {
                if (it.id == card1.id || it.id == card2.id) it.copy(isMatched = true) else it
            }

            _gameState.update { it.copy(cards = updatedCards) }

            updateScoreForMatch(card1.color)
        } else {
            updateScoreForMismatch(card1.color)
            viewModelScope.launch {
                delay(1000)
                val updatedCards = currentState.cards.map {
                    if (it.id == card1.id || it.id == card2.id) it.copy(isFlipped = false) else it
                }

                _gameState.update { it.copy(cards = updatedCards) }
                switchPlayer()
            }
        }

        flippedCards.clear()

        checkGameOver()
    }

    private fun updateScoreForMatch(cardColor: String) {
        val currentState = _gameState.value
        val currentPlayerIndex = currentState.currentPlayerIndex
        val currentPlayer = currentState.players[currentPlayerIndex]

        var pointsToAdd = 0

        // Apply scoring rules for matches
        when (cardColor) {
            "am" -> pointsToAdd = 1 // Yellow cards: 1 point
            "pr" -> pointsToAdd = 50 // Black cards: 50 points
            else -> {
                // Check if card color matches player's color
                if (cardColor == currentPlayer.color) {
                    pointsToAdd = 5 // Player's color: 5 points
                } else {
                    // Opponent's color: 1 point
                    pointsToAdd = 1
                }
            }
        }

        // Update player's score
        val updatedPlayers = currentState.players.mapIndexed { index, player ->
            if (index == currentPlayerIndex) {
                player.copy(score = player.score + pointsToAdd)
            } else player
        }

        _gameState.update {
            it.copy(
                players = updatedPlayers,
                lastScoreChange = pointsToAdd,
                showScoreAnimation = true
            )
        }

        // Hide score animation after delay
        viewModelScope.launch {
            delay(1500)
            _gameState.update { it.copy(showScoreAnimation = false) }
        }
    }

    private fun updateScoreForMismatch(cardColor: String) {
        val currentState = _gameState.value
        val currentPlayerIndex = currentState.currentPlayerIndex
        val currentPlayer = currentState.players[currentPlayerIndex]
        val opponentIndex = (currentPlayerIndex + 1) % currentState.players.size
        val opponent = currentState.players[opponentIndex]

        var pointsToDeduct = 0

        when (cardColor) {
            "pr" -> pointsToDeduct = 50
            else -> {
                if (cardColor == opponent.color) {
                    pointsToDeduct = 2
                }
            }
        }

        if (pointsToDeduct > 0) {
            val currentScore = currentPlayer.score
            val newScore = maxOf(0, currentScore - pointsToDeduct)
            val actualDeduction = currentScore - newScore

            // Update player's score
            val updatedPlayers = currentState.players.mapIndexed { index, player ->
                if (index == currentPlayerIndex) {
                    player.copy(score = newScore)
                } else player
            }

            _gameState.update {
                it.copy(
                    players = updatedPlayers,
                    lastScoreChange = -actualDeduction,
                    showScoreAnimation = actualDeduction > 0
                )
            }

            // Hide score animation after delay
            if (actualDeduction > 0) {
                viewModelScope.launch {
                    delay(1500)
                    _gameState.update { it.copy(showScoreAnimation = false) }
                }
            }
        }
    }

    private fun switchPlayer() {
        val currentState = _gameState.value
        val currentPlayerIndex = currentState.currentPlayerIndex
        var nextPlayerIndex = (currentPlayerIndex + 1) % currentState.players.size

        while (currentState.players[nextPlayerIndex].isEliminated &&
            currentState.players.count { !it.isEliminated } > 1) {
            nextPlayerIndex = (nextPlayerIndex + 1) % currentState.players.size
        }

        _gameState.update { it.copy(currentPlayerIndex = nextPlayerIndex) }
    }

    private fun checkGameOver() {
        val currentState = _gameState.value
        val allMatched = currentState.cards.all { it.isMatched }
        val onlyOnePlayerLeft = currentState.players.count { !it.isEliminated } == 1

        if (allMatched || onlyOnePlayerLeft) {
            _gameState.update { it.copy(gameOver = true) }
            gameTimerJob?.cancel()
            saveScores()
        }
    }
    private fun saveScores() {
        val currentState = _gameState.value
        currentState.players.forEach { player ->
            preferencesManager.addScore(player)
        }
    }

    fun getHighScores(): List<Player> {
        return preferencesManager.getHighScores()
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
        val lastScoreChange: Int = 0,
        val showScoreAnimation: Boolean = false
    ) {
        val currentPlayer: Player?
            get() = players.getOrNull(currentPlayerIndex)

    }
}

