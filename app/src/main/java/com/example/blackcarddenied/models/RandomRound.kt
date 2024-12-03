package com.example.blackcarddenied.models

class RandomRound(
    questions: List<Question>
) : Round(questions) {

    override suspend fun getQuestions(): String{
        return fetchQuestion.fetchRandomQuestions()
    }

    override fun getRoundName(): String {
        return "RANDOM ROUND"
    }
}