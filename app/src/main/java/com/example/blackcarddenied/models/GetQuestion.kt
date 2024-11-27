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
}