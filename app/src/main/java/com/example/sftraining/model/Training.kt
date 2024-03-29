package com.example.sftraining.model

data class Training(
    val exers: List<Exer> = emptyList(),
    val timeMin: Int = exers.map { it.execTimeMin }.sum(),
    val name: String,
    val creator: User
)