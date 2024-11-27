package com.ayoapps.blackcarddenied.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayoapps.blackcarddenied.BuildConfig
import com.ayoapps.blackcarddenied.UiState
import com.example.blackcarddenied.models.GetQuestion
import com.example.blackcarddenied.models.Question
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameMainViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    var currentRound = 1
    var totalQuestions = 0
    private val getQuestion = GetQuestion()

    fun loadQuestion(prompt: String, difficulty: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val question = getQuestion.fetchQuestion(prompt, difficulty)
                _uiState.value = UiState.Success(question)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Error retrieving AI Question")
            }
        }
    }
}