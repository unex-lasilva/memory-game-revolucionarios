package com.example.memorygamerevolucionarios.data

import android.content.Context
import android.content.SharedPreferences
import com.example.memorygamerevolucionarios.model.Player
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MangaRosaMemoryPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveHighScores(players: List<Player>) {
        val json = gson.toJson(players)
        sharedPreferences.edit().putString("high_scores", json).apply()
    }

    fun getHighScores(): List<Player> {
        val json = sharedPreferences.getString("high_scores", null)
        return if (json != null) {
            val type = object : TypeToken<List<Player>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun addScore(player: Player) {
        val currentScores = getHighScores().toMutableList()
        currentScores.add(player)
        currentScores.sortByDescending { it.score }
        if (currentScores.size > 10) {
            currentScores.removeAt(currentScores.lastIndex)
        }
        saveHighScores(currentScores)
    }
}
