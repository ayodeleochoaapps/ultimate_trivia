package com.example.blackcarddenied.models

import android.content.Context
import com.ayoapps.blackcarddenied.R


class QuicknessRound(
    private val context: Context,
    questions: List<Question>
) : Round(questions) {

    override suspend fun getQuestions(): String{
        return fetchQuestion.fetch10Questions()
    }

    override fun getRoundName(): String {
        return context.getString(R.string.quickness_round)
    }

    override fun getRoundDescription(): String {
        return context.getString(R.string.quickness_round_desc)
    }
}