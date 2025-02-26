package com.example.blackcarddenied.models

import android.content.Context
import com.ayoapps.blackcarddenied.R


class BuildUpRound(
    private val context: Context,
    questions: List<Question>
) : Round(questions) {

    constructor(context: Context) : this(context, emptyList())

    override suspend fun getQuestions(): String{
        return fetchQuestion.fetch10QuestionsBuildUp()
    }

    override fun getRoundName(): String {
        return context.getString(R.string.build_up_round)
    }

    override fun getRoundDescription(): String {
        return context.getString(R.string.build_up_round_desc)
    }
}