package com.example.blackcarddenied.models


data class GameData(
    var _questionsLoaded: Boolean = false,
    var _roundName: String = "ROUND"
){

    var questionsLoaded: Boolean
        get() = _questionsLoaded
        set(value) {
            _questionsLoaded = value
        }

    var roundName: String
        get() = _roundName
        set(value) {
            _roundName = value
        }
}


