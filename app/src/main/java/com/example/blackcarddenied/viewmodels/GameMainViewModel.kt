package com.ayoapps.blackcarddenied.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ayoapps.blackcarddenied.R
import com.ayoapps.blackcarddenied.UiState
import com.ayoapps.blackcarddenied.views.GameMainFragment
import com.example.blackcarddenied.models.BuildUpRound
import com.example.blackcarddenied.models.GameData
import com.example.blackcarddenied.models.GetQuestion
import com.example.blackcarddenied.models.Question
import com.example.blackcarddenied.models.QuicknessRound
import com.example.blackcarddenied.models.RandomRound
import com.example.blackcarddenied.models.Round
import com.example.blackcarddenied.models.Score
import com.google.common.reflect.TypeToken
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.round
import kotlin.random.Random


class GameMainViewModel(application: Application) : AndroidViewModel(application){
    private val appContext = getApplication<Application>().applicationContext
/*    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()*/
private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    val gameMainFragment = GameMainFragment()
    private val getQuestion = GetQuestion()
    // var questions = emptyList<Question>()
   // var round = Round(questions)
    lateinit var questions: List<Question>
    lateinit var round: Round
    var randomPointTotals = mutableListOf<Int>()
    val gameData = MutableLiveData<GameData>().apply { value = GameData() }

    private var currentQuestion: Question? = null
    var correctSound = MediaPlayer.create(appContext, R.raw.correct)
    var incorrectSound = MediaPlayer.create(appContext, R.raw.incorrect)

    private val countdownTimeAmount: Long = 10000
    private var timeRemaing: Long = 0
    private var countdownTimer: CountDownTimer? = null
    private val getQuest = GetQuestion()

    lateinit var remainingRounds: MutableList<Round>
    var buildUpCurrentValue = 25
    val progressBarValue = MutableLiveData<Int>()

    // Write a message to the database
    val database = Firebase.database
    val scoresReference = database.getReference("scores")

    private fun saveFinalScore(score: Int){
        val newScoreRef = scoresReference.push() // Generate a unique key
        val key = newScoreRef.key  // Get the generated key

        if (key != null) {
            val scoreData = Score(key, score) // Create a Score object

            newScoreRef.setValue(scoreData)
                .addOnSuccessListener {
                    Log.d(this::class.simpleName, "New score added successfully: $scoreData")
                }
                .addOnFailureListener { e ->
                    Log.e(this::class.simpleName, "New score not saved - Error: $e")
                }
        }
    }

    private suspend fun calculatePercentile(): Int = suspendCoroutine { continuation ->
        scoresReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalDatabaseScore = 0
                var scores = 0
                var scoresBelow = 0
                val totalScore = gameData.value?.totalScore?.toInt() ?: 0

                for (userSnapshot in snapshot.children) {
                    val score = userSnapshot.child("score").getValue(Int::class.java) ?: 0
                    totalDatabaseScore += score
                    scores++
                    if (score <= totalScore) {
                        scoresBelow++
                    }
                }

                val percentile = if (scores > 0) (scoresBelow.toDouble() / scores.toDouble()) * 100 else 0.0
                gameData.value?.percentile = percentile.toInt()

                Log.d(javaClass.simpleName, "firedb calculated percentile: ${gameData.value?.percentile}")

                // Resume coroutine with calculated percentile
                continuation.resume(percentile.toInt())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(javaClass.simpleName, "Error: ${error.message}")
                continuation.resume(0) // Resume with 0 in case of an error
            }
        })
    }

    suspend fun processFinalScore(totalScore: Int): Int {
        saveFinalScore(totalScore) // ✅ Waits for completion
        return calculatePercentile() // ✅ Waits for percentile calculation before returning
    }


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

    fun initializeRounds(){
        remainingRounds = gameData.value?.remainingRounds ?: mutableListOf()

        // If rounds not set then it's the first round and it needs to be initialized
        if (remainingRounds.isEmpty()){
            remainingRounds = mutableListOf(RandomRound(appContext), QuicknessRound(appContext), BuildUpRound(appContext))
        }

        round = remainingRounds.random()
        if (round.getRoundName() == appContext.getString(R.string.random_round)){
            generateRandomPoints()
        }
        remainingRounds.remove(round)
        Log.d("${this::class.simpleName} gameData remainingRounds", remainingRounds.size.toString())
        if (remainingRounds.size == 0){
            gameData.value?.gameOver = true
        }

        gameData.value?.remainingRounds = remainingRounds
        setInitialValues()
    }

    fun pickARound(){
        Log.d("${this::class.simpleName} gameData", "pickARound called")

        viewModelScope.launch(Dispatchers.IO) {
            getQuestions()


/*            withContext(Dispatchers.Main) {
                setInitialValues()
            }*/

            gameData.value?.questionsLoaded = true
            gameData.value?.let { Log.d("${this::class.simpleName} questionsLoaded", it.questionsLoaded.toString()) }

        }
    }

    suspend fun getQuestions(){
        val questionsJson = if (round.getRoundName() == appContext.getString(R.string.build_up_round)){
            getQuest.fetch10QuestionsBuildUp()
        } else {
            getQuest.fetch10Questions()
        }
        Log.d(this::class.simpleName, "questions returned: $questionsJson")
        val questionsJsonTrimmed = questionsJson.replace("json", "")
        val questionsJsonTrimmed2 = questionsJsonTrimmed.replace("```", "")
        val gson = Gson()
        questions = gson.fromJson(questionsJsonTrimmed2, Array<Question>::class.java).toList()
        Log.d(this::class.simpleName, "questions count: ${questions.size}")
         //round = remainingRounds.random()

        Log.d(this::class.simpleName, questions.toString())

        round.setQuestions(questions)
    }

    private fun setInitialValues() {
        Log.d("${this::class.simpleName} gameData", "setInitialValues called")
       // round = RandomRound(appContext, questions)
       /* _gameData.value?.apply {
            roundName = round.getRoundName()
        }
        _gameData.value = _gameData.value // Trigger LiveData observer*/
        gameData.value?.roundName = round.getRoundName()
        gameData.value?.roundDescription = round.getRoundDescription()
        gameData.value?.let { Log.d("${this::class.simpleName} gameData", it.roundName) }

        _uiState.value = UiState.Initial
       // gameData.value?.clockTime = "10"
    }

    fun generateRandomPoints(){
        var sum = 0

        // Generate the first 9 random numbers divisible by 5
        for (i in 1..9) {
            val randomAmount = Random.nextInt(10, 31) * 5 // Multiples of 5 in range [50, 150]
            randomPointTotals.add(randomAmount)
            sum += randomAmount
        }

        // Calculate the 10th number
        val remaining = 1000 - sum
        if (remaining in 50..150 && remaining % 5 == 0) {
            randomPointTotals.add(remaining)
        } else {
            // Adjust values to ensure the last number fits constraints
            while (true) {
                val adjustIndex = Random.nextInt(9) // Choose a random index from the first 9
                val adjustment = Random.nextInt(-20, 21) * 5 // Adjustment to maintain divisibility
                val adjustedValue = randomPointTotals[adjustIndex] + adjustment

                if (adjustedValue in 50..150) {
                    randomPointTotals[adjustIndex] = adjustedValue
                    break
                }
            }
            randomPointTotals.add(1000 - randomPointTotals.sum())
        }

/*        _gameData.value?.apply {
            currentPointTotal = randomPointTotals.first()
            Log.d(this::class.simpleName, "currentPointTotal: $currentPointTotal")
        }
        _gameData.value = _gameData.value // Trigger LiveData observer*/
     //   gameData.value?.let { Log.d("gameData", it.roundName) }
        gameData.value?.currentPointTotal = randomPointTotals.first()
        Log.d(this::class.simpleName, "pointTotals amounts: $randomPointTotals")
    }

    fun startRound() {
        displayNextQuestion()
    }

    private fun displayNextQuestion() {
        Log.d(this::class.simpleName, "displayNextQuestion called...")
        currentQuestion = round.getCurrentQuestion()
        if (currentQuestion != null && !round.isFinished()) {
            // Display the question on the UI
            gameData.value?.currentQuestion = currentQuestion!!.question
            gameData.value?.currentCategory = currentQuestion!!.category
            gameData.value?.answerA = currentQuestion!!.options[0]
            gameData.value?.answerB = currentQuestion!!.options[1]
            gameData.value?.answerC = currentQuestion!!.options[2]
            gameData.value?.answerD = currentQuestion!!.options[3]
            updateUI()

            //Start Timer
            startCountdown(countdownTimeAmount)
        }
    }

    fun checkAnswer(view: View){
        if (view is Button) {
            val userAnswer = view.text.toString()
            Log.d(this::class.simpleName, "userAnswer: $userAnswer")

            Log.d(this::class.simpleName, "timeRemaining: $timeRemaing")

            // Handle the answer logic here
            val isCorrect = round.answerQuestion(userAnswer, getQuestionPointTotal(round))
            if (round.getRoundName() == appContext.getString(R.string.build_up_round)){
                updateBuildUpValue(isCorrect)
            }
            // Play sound to indicate if user's answer is correct or incorrect
            if (isCorrect){
                correctSound.start()
            } else {
                incorrectSound.start()
            }

            Log.d(this::class.simpleName, "currentScore: ${round.getScore()}")
            if (round.isFinished()) {
              //  showRoundSummary(binding)
                Log.d(this::class.simpleName, "Round Over")
                val currentScore = round.getScore()
                gameData.value?.currentScore = currentScore
                val totalScore = gameData.value?.totalScore?.toInt()
                val finalScore = totalScore?.plus(currentScore)
                if (finalScore != null) {
                    gameData.value?.totalScore = finalScore
                }
                countdownTimer?.cancel() // Cancel any existing timer
                updateUiState(UiState.Finish)
                gameData.value?.questionsLoaded = false
                val previousRoundsCompleted = gameData.value?.roundsCompleted?.toInt()
                if (previousRoundsCompleted != null) {
                    gameData.value?.roundsCompleted = previousRoundsCompleted + 1
                }
            } else {
                displayNextQuestion()
            }
        }
    }

    // Get value of current question according to type of Round
    private fun getQuestionPointTotal(currentRound: Round): Int{
        when (currentRound){
            is RandomRound -> return randomPointTotals[round.getQuestionIndex()]
            is QuicknessRound -> return round((timeRemaing/100).toDouble()).toInt()
            is BuildUpRound -> return buildUpCurrentValue
        }
        // If all else fails
        return 100
    }

    private fun updateBuildUpValue(isCorrect: Boolean){
        Log.d(this::class.simpleName, "buildUpCurrentValue called")
        if (isCorrect){
            if (buildUpCurrentValue < 125){
                buildUpCurrentValue += 25
            }
        } else {
            if (buildUpCurrentValue > 25){
                buildUpCurrentValue -= 25
            }
        }
        Log.d(this::class.simpleName, "buildUpCurrentValue: $buildUpCurrentValue")
    }

    fun updateUiState(uiState: UiState){
        _uiState.value = uiState
    }

    private fun updateUI(){
        if (round.getRoundName() == appContext.getString(R.string.random_round)){
            gameData.value?.currentPointTotal = randomPointTotals[round.getQuestionIndex()]
        } else if (round.getRoundName() == appContext.getString(R.string.build_up_round)){
            Log.d(this::class.simpleName, "roundName: ${round.getRoundName()}")
            gameData.value?.currentPointTotal = buildUpCurrentValue
        }

        gameData.value?.currentCategory = currentQuestion!!.category
        gameData.value?.currentScore = round.getScore()
    }

    fun startCountdown(durationMillis: Long) {
        countdownTimer?.cancel() // Cancel any existing timer

        countdownTimer = object : CountDownTimer(durationMillis, 10) { // Tick every 10ms for hundredths of a second
            override fun onTick(millisUntilFinished: Long) {

/*                val seconds = millisUntilFinished / 1000
                val hundredths = (millisUntilFinished % 1000) / 10
                gameData.value?.clockTime = "$seconds:$hundredths"*/
                timeRemaing = millisUntilFinished

                if (round.getRoundName() == appContext.getString(R.string.quickness_round)){
                    gameData.value?.currentPointTotal = round((timeRemaing/100).toDouble()).toInt()
                }

                // Update progress bar
                val progress = ((millisUntilFinished.toFloat() / durationMillis) * 100).toInt()
                progressBarValue.postValue(progress)
                //   binding.txtClock.text = "$seconds : $hundredths"
            }

            override fun onFinish() {
              //  binding.txtClock.text = "00.00" // Display 0 when the countdown finishes
              //  gameData.value?.clockTime = "00.00"
                round.answerQuestion("", 0)
                incorrectSound.start()

                if (round.isFinished()){
                    Log.d(this::class.simpleName, "Round Over")
                    val currentScore = round.getScore()
                    gameData.value?.currentScore = currentScore
                    val totalScore = gameData.value?.totalScore?.toInt()
                    val finalScore = totalScore?.plus(currentScore)
                    if (finalScore != null) {
                        gameData.value?.totalScore = finalScore
                    }
                    countdownTimer?.cancel() // Cancel any existing timer
                    updateUiState(UiState.Finish)
                    gameData.value?.questionsLoaded = false
                    val previousRoundsCompleted = gameData.value?.roundsCompleted?.toInt()
                    if (previousRoundsCompleted != null) {
                        gameData.value?.roundsCompleted = previousRoundsCompleted + 1
                    }
                } else {
                    round.answerQuestion("", 0)
                    displayNextQuestion()
                }

                progressBarValue.postValue(0)
            }
        }.start()
    }
}