package com.ayoapps.blackcarddenied.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayoapps.blackcarddenied.UiState
import com.example.blackcarddenied.models.GetQuestion
import com.example.blackcarddenied.models.Question
import com.example.blackcarddenied.models.RandomRound
import com.example.blackcarddenied.models.Round
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class GameMainViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    var currentRound = 1
    var totalQuestions = 0
    var randomRound = RandomRound(emptyList())
    private val getQuestion = GetQuestion()
    var questions = emptyList<Question>()
    var round = Round(questions)

    // LiveData to hold the text that will be displayed on the TextView
    private val _roundName = MutableLiveData<String>()
    val roundName: LiveData<String> get() = _roundName

    fun loadQuestion(prompt: String, difficulty: String){
        _uiState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val questionJson = getQuestion.fetchQuestion(prompt, difficulty).trimIndent()
                val jsonString = """
        {
            "question": "What is the capital of France?",
            "options": ["Berlin", "Paris", "Madrid", "Rome"],
            "correctAnswer": "Paris",
            "difficulty": "Easy"
        }
    """
                val question = Json.decodeFromString<Question>(jsonString)
                Log.d(this::class.simpleName, question.toString())
                _uiState.value = UiState.Success(questionJson)
            } catch (e: Exception) {
                e.localizedMessage?.let { Log.d(this::class.simpleName, it) }
                _uiState.value = UiState.Error(e.localizedMessage ?: "Error retrieving AI Question")
            }
        }
    }

    fun loadRandomQuestions(){
        while (questions.size < 10){

        }
    }

    fun pickARound(){
        round = RandomRound(questions)
        viewModelScope.launch {
            getQuestions(round)
            setInitialValues()
        }
    }

    suspend fun getQuestions(selectedRound: Round){
       val questionsJson = selectedRound.getQuestions()
        val questionsJsonTrimmed = questionsJson.replace("json", "")
        val questionsJsonTrimmed2 = questionsJsonTrimmed.replace("```", "")
        Log.d("questionsss", questionsJsonTrimmed2)

        val gson = Gson()
        val questions = gson.fromJson(questionsJsonTrimmed2, Array<Question>::class.java).toList()

        println(questions)
    }

    fun setInitialValues() {
        _roundName.value = round.getRoundName()
    }
}