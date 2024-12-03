package com.example.blackcarddenied.models

data class Question(
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val difficulty: String,
    val category: String
)
