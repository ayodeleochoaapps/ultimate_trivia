package com.example.blackcarddenied.models

import android.util.Log

open class Round(
    private val questions: List<Question>
) {
    private var currentQuestionIndex = 0
    private var score = 0
    val fetchQuestion = GetQuestion()

    fun getCurrentQuestion(): Question? {
        Log.d(this::class.simpleName, "questions.size ${questions.size}")
        return if (currentQuestionIndex < questions.size) {
            questions[currentQuestionIndex]
        } else {
            null // No more questions
        }
    }

    open suspend fun getQuestions(): String{
      return  fetchQuestion.fetchQuestion("sports", "easy")
    }

    open fun getRoundName(): String{
        return "ROUND Name"
    }

    open fun getRoundDescription(): String{
        return "ROUND Description"
    }

    fun answerQuestion(userAnswer: String, questionValue: Int): Boolean {
        val currentQuestion = getCurrentQuestion()
        return if (currentQuestion != null) {
            val isCorrect = userAnswer == currentQuestion.answer
            if (isCorrect) score += questionValue
            currentQuestionIndex++
            isCorrect
        } else {
            false // No more questions to answer
        }
    }

    fun getScore(): Int = score

    fun addToScore(addedPoints: Int){
        score += addedPoints
    }

    fun getQuestionIndex(): Int = currentQuestionIndex

    fun isFinished(): Boolean = currentQuestionIndex >= questions.size
}