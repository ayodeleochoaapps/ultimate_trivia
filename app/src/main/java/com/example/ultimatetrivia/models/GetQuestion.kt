package com.example.ultimatetrivia.models

import com.ayoapps.ultimatetrivia.BuildConfig
import com.ayoapps.ultimatetrivia.UiState
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow

private val _uiState: MutableStateFlow<UiState> =
    MutableStateFlow(UiState.Initial)

val gameData = GameData()
val categoriesList = arrayOf(
    "math",
    "science",
    "music",
    "movies",
    "tv shows",
    "art",
    "decades",
    "history",
    "animals",
    "geography",
    "current events",
    "pop culture",
    "food",
    "sports",
    "random facts",
    "famous people",
    "world records",
    "astronomy",
    "medicine",
    "celebrities",
    "animation",
    "video games",
    "literature",
    "board games",
    "mythology",
    "inventions",
    "fashion",
    "food",
    "slang",
    "memes",
    "social media",
    "comics"
)

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
                        "return 10 unique questions that have not been returned previously from any of the following subjects: " +
                                "${categoriesList.random()}, ${categoriesList.random()}, ${categoriesList.random()}, ${categoriesList.random()}, ${categoriesList.random()}" +
                                "${categoriesList.random()}, ${categoriesList.random()}, ${categoriesList.random()}, ${categoriesList.random()}, ${categoriesList.random()}" +
                                "with 4 options and also return the correct answer with a difficulty of either very easy, easy, medium, hard and very hard. " +
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
                        "return 10 unique questions that have not been returned previously from any of the following subjects: " +
                                "${categoriesList.random()}, ${categoriesList.random()}, ${categoriesList.random()}, ${categoriesList.random()}, ${categoriesList.random()}, " +
                                "${categoriesList.random()}, ${categoriesList.random()}, ${categoriesList.random()}, ${categoriesList.random()}, ${categoriesList.random()}" +
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
}