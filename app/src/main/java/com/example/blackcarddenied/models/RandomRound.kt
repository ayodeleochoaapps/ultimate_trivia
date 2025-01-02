package com.example.blackcarddenied.models

import android.content.Context
import com.ayoapps.blackcarddenied.R


class RandomRound(
    private val context: Context,
    questions: List<Question>
) : Round(questions) {

    override suspend fun getQuestions(): String{
        return fetchQuestion.fetchRandomQuestions()
    }

    override fun getRoundName(): String {
        return context.getString(R.string.random_round)
    }

    override fun getRoundDescription(): String {
        return context.getString(R.string.random_round_desc)
    }
}