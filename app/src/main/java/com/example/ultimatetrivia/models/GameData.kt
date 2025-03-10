package com.example.ultimatetrivia.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

data class GameData(
    var _questionsLoaded: Boolean = false,
    var _roundName: String = "",
    var _currentPointTotal: Number = 0,
    var _clockTime: String = "5",
    var _roundDescription: String = "",
    var _currentQuestion: String = "",
    var _currentCategory: String = "",
    var _answerA: String = "",
    var _answerB: String = "",
    var _answerC: String = "",
    var _answerD: String = "",
    var _currentScore: Number = 0,
    var _totalScore: Number = 0,
    var _remainingRounds: MutableList<Round> = mutableListOf(),
    var _gameOver: Boolean = false,
    var _roundsCompleted: Number = 0,
    var _percentile: Number = 0

) : BaseObservable() {

    var questionsLoaded: Boolean
        @Bindable get() = _questionsLoaded
        set(value) {
            _questionsLoaded = value
            notifyChange()
        }

    var roundName: String
        @Bindable get() = _roundName
        set(value) {
            _roundName = value
            notifyChange()
        }

    var currentPointTotal: Number
        @Bindable get() = _currentPointTotal
        set(value) {
            _currentPointTotal = value
            notifyChange()
        }

    var clockTime: String
        @Bindable get() = _clockTime
        set(value) {
            _clockTime = value
            notifyChange()
        }

    var roundDescription: String
        @Bindable get() = _roundDescription
        set(value) {
            _roundDescription = value
            notifyChange()
        }

    var currentQuestion: String
        @Bindable get() = _currentQuestion
        set(value) {
            _currentQuestion = value
            notifyChange()
        }

    var currentCategory: String
        @Bindable get() = _currentCategory
        set(value) {
            _currentCategory = value
            notifyChange()
        }

    var answerA: String
        @Bindable get() = _answerA
        set(value) {
            _answerA = value
            notifyChange()
        }

    var answerB: String
        @Bindable get() = _answerB
        set(value) {
            _answerB = value
            notifyChange()
        }

    var answerC: String
        @Bindable get() = _answerC
        set(value) {
            _answerC = value
            notifyChange()
        }

    var answerD: String
        @Bindable get() = _answerD
        set(value) {
            _answerD = value
            notifyChange()
        }

    var currentScore: Number
        @Bindable get() = _currentScore
        set(value) {
            _currentScore = value
            notifyChange()
        }

    var totalScore: Number
        @Bindable get() = _totalScore
        set(value) {
            _totalScore = value
            notifyChange()
        }

    var remainingRounds: MutableList<Round>
        @Bindable get() = _remainingRounds
        set(value) {
            _remainingRounds = value
            notifyChange()
        }

    var gameOver: Boolean
        @Bindable get() = _gameOver
        set(value) {
            _gameOver = value
            notifyChange()
        }

    var roundsCompleted: Number
        @Bindable get() = _roundsCompleted
        set(value) {
            _roundsCompleted = value
            notifyChange()
        }

    var percentile: Number
        @Bindable get() = _percentile
        set(value) {
            _percentile = value
            notifyChange()
        }
}


