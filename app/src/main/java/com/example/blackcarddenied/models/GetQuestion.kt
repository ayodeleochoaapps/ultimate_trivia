package com.example.blackcarddenied.models

import android.util.Log
import com.ayoapps.blackcarddenied.BuildConfig
import com.ayoapps.blackcarddenied.UiState
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private val _uiState: MutableStateFlow<UiState> =
    MutableStateFlow(UiState.Initial)
val uiState: StateFlow<UiState> =
    _uiState.asStateFlow()

val gameData = GameData()


class GetQuestion {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    suspend fun fetchQuestion(prompt: String, difficulty: String): String {
        _uiState.value = UiState.Loading
        return try {
            val response = generativeModel.generateContent(
                content {
                    text(
                        "return a question about $prompt with 4 options and also return the correct answer with the difficulty of $difficulty. " +
                                "Return in the same JSON format every time."
                    )
                }
            )
            println(response.text)
            response.text ?: throw Exception("Empty response from model")

        } catch (e: Exception) {
            throw Exception("Error fetching question: ${e.localizedMessage}", e)
        }
    }

    suspend fun fetch10Questions(): String {
        return try {
            val response = generativeModel.generateContent(
                content {
                    text(
                        "return 10 unique questions that have not been returned previously from any of the following subjects: math, science, music, movies, tv shows, art, decades, history" +
                                "animals, geography, current events, pop culture, food or sports with 4 options and also " +
                                "return the correct answer with a difficulty of either very easy, easy, medium, hard and very hard. " +
                                "Return in the proper JSON Array format returning nothing before the brackets, with keys question, options, answer, difficulty and category."
                    )
                }
            )
            println(response.text)
            response.text ?: throw Exception("Empty response from model")

        } catch (e: Exception) {
            throw Exception("Error fetching question: ${e.localizedMessage}", e)
        }
    }

    suspend fun fetch10QuestionsBuildUp(): String {
        return try {
            val response = generativeModel.generateContent(
                content {
                    text(
                        "return 15 unique questions that have not been returned previously from any of the following subjects: math, science, music, movies, tv shows, art, decades, history" +
                                "animals, geography, current events, pop culture, food or sports with 4 options and also " +
                                "return the correct answer. Return the questions in this order: 1 very easy question, 2 easy questions, 4 medium questions, 2 hard questions, 1 very hard question. " +
                                "Return in the proper JSON Array format returning nothing before the brackets, with keys question, options, answer, difficulty and category."
                    )
                }
            )
            println(response.text)
            response.text ?: throw Exception("Empty response from model")

        } catch (e: Exception) {
            throw Exception("Error fetching question: ${e.localizedMessage}", e)
        }
    }

/*    suspend fun fetchRandomQuestions(prompt: String, difficulty: String): String {
        _uiState.value = UiState.Loading
        return try {
            val response = generativeModel.generateContent(
                content {
                    text(
                        "return a question about $prompt with 4 options and also return the correct answer with the difficulty of $difficulty. " +
                                "with 4 options and return in the proper JSON Array format returning nothing before the brackets, with keys question, options, answer, difficulty and category."
                    )
                }
            )
            println(response.text)
            response.text ?: throw Exception("Empty response from model")

        } catch (e: Exception) {
            throw Exception("Error fetching question: ${e.localizedMessage}", e)
        }
    }*/
}