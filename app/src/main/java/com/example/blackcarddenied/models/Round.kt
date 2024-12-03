package com.example.blackcarddenied.models

open class Round(
    private val questions: List<Question>
) {
    private var currentQuestionIndex = 0
    private var score = 0
    val fetchQuestion = GetQuestion()

    fun getCurrentQuestion(): Question? {
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
        return "ROUND"
    }

    fun answerQuestion(userAnswer: String, questionValue: Int): Boolean {
        val currentQuestion = getCurrentQuestion()
        return if (currentQuestion != null) {
            val isCorrect = userAnswer == currentQuestion.correctAnswer
            if (isCorrect) score += questionValue
            currentQuestionIndex++
            isCorrect
        } else {
            false // No more questions to answer
        }
    }

    fun getScore(): Int = score

    fun isFinished(): Boolean = currentQuestionIndex >= questions.size
}