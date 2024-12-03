package com.example.blackcarddenied.models

import android.util.Log
import com.ayoapps.blackcarddenied.BuildConfig
import com.ayoapps.blackcarddenied.UiState
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GetQuestion {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    suspend fun fetchQuestion(prompt: String, difficulty: String): String {
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

    suspend fun fetchRandomQuestions(): String {
        return try {
            val response = generativeModel.generateContent(
                content {
                    text(
                        "return 10 questions about math, science, music, movies, tv shows, art, decades, history,\n" +
                                "animals, geography, current events, pop culture, food or sports with 4 options and also " +
                                "return the correct answer with a difficulty of very easy, easy, medium, hard and very hard. " +
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
}