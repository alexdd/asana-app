package com.asana.timer.data

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Asana(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val durationSeconds: Int,
    val description: String = ""
)

@Serializable
data class AsanaSequence(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val asanas: List<Asana>
)

