package com.example.memorygamerevolucionarios.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(
    val name: String,
    val color: String,
    var score: Int = 0,
    var isEliminated: Boolean = false
) : Parcelable
