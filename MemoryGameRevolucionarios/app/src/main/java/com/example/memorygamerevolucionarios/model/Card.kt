package com.example.memorygamerevolucionarios.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Card(
    val id: Int,
    val name: String,
    var isMatched: Boolean = false,
    var isFlipped: Boolean = false
) : Parcelable {
    val color: String
        get() = name.take(2).lowercase()
}
