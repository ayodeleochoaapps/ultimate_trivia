package com.example.blackcarddenied.models

data class Question(
    val question: String,
    val options: List<String>,
    val answer: String,
    val difficulty: String,
    val category: String
)
