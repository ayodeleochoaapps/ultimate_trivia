package com.example.blackcarddenied.views

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.ayoapps.blackcarddenied.R
import com.ayoapps.blackcarddenied.databinding.FragmentGameSummaryBinding
import com.ayoapps.blackcarddenied.viewmodels.GameMainViewModel
import com.ayoapps.blackcarddenied.views.GameMainFragment
import com.ayoapps.blackcarddenied.views.WelcomeActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GameSummaryFragment : Fragment() {
    // Use 'activityViewModels()' to get the shared ViewModel
    private val gameMainViewModel: GameMainViewModel by activityViewModels()
    private lateinit var binding: FragmentGameSummaryBinding
    private var totalScore = 0

    private val aArray = arrayOf("Are you even human? Because that was some AI-level trivia domination!",
        "You must have a PhD in random facts! Or just a very lucky guessing streak.",
        "If trivia were a sport, you'd be the undisputed world champion!",
        "The encyclopedia just called. It wants its brain back!",
        "I bow to your superior knowledge. Also, do you do tutoring?",
        "The A stands for Authentically Awesome!",
        "They modeled all that AI stuff after you.")

    private val bArray = arrayOf("You're just one fun fact away from total greatness!",
        "B for ‘Brilliant’… or ‘Barely Missed A+’—your call!",
        "Not bad! But I bet you still don’t know where your left sock went.",
        "This was a strong performance, but I can tell you googled some of those.",
        "Your brain is 80% trivia and 20% snack cravings—solid ratio!",
        "You're one smart cookie, as far as cookie's intelligence goes.")

    private val cArray = arrayOf("You’re like a ‘smart-ish’ cookie! Not quite a genius, but still delicious!",
        "You passed! Your knowledge is officially above average. Not bad, champ!",
        "Somewhere, a middle school teacher is nodding in approval.",
        "Hey, C’s get degrees! And also mild disappointment.",
        "You’re like a WiFi signal—strong sometimes, but weak when it matters!")

    private val dArray = arrayOf("You didn’t fail! You just... took the scenic route to success.",
        "Your brain has potential… it’s just buffering.",
        "Let’s call this ‘room for improvement’ instead of a tragedy.",
        "I see what you were going for… but the quiz saw otherwise.",
        "Good effort! But let’s be real, that was 40% knowledge, 60% lucky guesses.",
        "The D stands for Dumb, Dumber and Dumberer")

    private val fArray = arrayOf("You have successfully disappointed your imaginary trivia coach.",
        "It’s okay, Einstein also failed a few tests… though probably not like this.",
        "Good news: There’s no way to score lower next time!",
        "You might have won in spirit, but the scoreboard says otherwise.",
        "Don’t worry, trivia isn’t for everyone. Maybe try... arm wrestling?")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using data binding
        binding = FragmentGameSummaryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.gameViewModel = gameMainViewModel // Bind the shared ViewModel to the UI

        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the next round button click listener (example of navigation)
        view.findViewById<Button>(R.id.nextRoundButton).setOnClickListener {
            if (gameMainViewModel.gameData.value?.gameOver == true){
                val intent = Intent(activity, WelcomeActivity::class.java)
                startActivity(intent)
            } else {
                val destinationFragment = GameMainFragment()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, destinationFragment)
                    .addToBackStack(null)
                    .commit()
            }

        }

/*        val gradeTextTitle: TextView = view.findViewById(R.id.txtGradeTitle)
        val gradeText: TextView = view.findViewById(R.id.txtGrade)*/

/*        if (gameMainViewModel.gameData.value?.gameOver == true){
            gradeText.visibility = View.VISIBLE
            gradeTextTitle.visibility = View.VISIBLE
            gradeText.text = calculateGrade(gameMainViewModel.gameData.value!!.totalScore.toInt())
        } else {
            gradeText.visibility = View.GONE
            gradeTextTitle.visibility = View.GONE
        }*/

        totalScore = gameMainViewModel.gameData.value?.totalScore?.toInt()!!
        val roundsCompleted = gameMainViewModel.gameData.value?.roundsCompleted?.toInt()

        val percentage = (totalScore?.toDouble()?.div(1000 * roundsCompleted!!)?.times(100))?.toInt()
        Log.d(javaClass.simpleName, "totalScore: " + totalScore.toString())
        Log.d(javaClass.simpleName, "roundsCompleted: " + roundsCompleted.toString())
        Log.d(javaClass.simpleName, "percentage: " + percentage.toString())
     //   Log.d(javaClass.simpleName, "totalScore / roundsCompleted * 1000: " + (totalScore / roundsCompleted * 1000))


            if (percentage != null) {
                binding.circuarProgressBar.progress = percentage
                binding.percentageScore.text = calculateGrade(percentage)
                binding.scoreComment.text = getGradeComment(percentage)
            }

        if (gameMainViewModel.gameData.value!!.gameOver == true){
            viewLifecycleOwner.lifecycleScope.launch {
                val percentile = gameMainViewModel.processFinalScore(totalScore) // ✅ Now properly waits for completion
                val percentileStatement = Html.fromHtml(
                    getString(R.string.percentile_statement, percentile.toString())
                )

                Log.d(javaClass.simpleName, "firedb GameSummaryFragment percentile: $percentile")
                binding.scoreComment.text = percentileStatement
            }
        }


/*        viewLifecycleOwner.lifecycleScope.launch {
            gameMainViewModel.processFinalScore(totalScore)

            withContext(Dispatchers.Main) {
                val percent = gameMainViewModel.gameData.value?.percentile ?: 0
                Log.d(javaClass.simpleName, "firedb Summary percent: " + percent)
                binding.scoreComment.text =
                    "Your score is ranked among the top $percent percentile of all Ultimate Trivia contestants"
            }
        }*/

/*        Log.d(javaClass.simpleName, "firedb gameOverStaus: ${gameMainViewModel.gameData.value?.gameOver} ")
      //      if (gameMainViewModel.gameData.value?.gameOver == true) {
                Log.d(javaClass.simpleName, "firedb gameOver ")
                if (totalScore != null) {
                    Log.d(javaClass.simpleName, "firedb totalScore not null ")
                    GlobalScope.launch(Dispatchers.IO)  {
                        gameMainViewModel.saveFinalScore(totalScore)
                        Log.d(javaClass.simpleName, "firedb saveFinalScore called")

                        gameMainViewModel.calculatePercentile()
                        Log.d(javaClass.simpleName, "firedb calculatePercentile called")

                        withContext(Dispatchers.Main){
                            val percent = gameMainViewModel.gameData.value!!.percentile

                            Log.d(javaClass.simpleName, "firedb percent: " + percent)

                            binding.scoreComment.text =
                                "Your score is ranked among the top $percent percentile of all Ultimate Trivia contestants"
                        }
                    }
                }*/

    //    }


    }

    private fun calculateGrade(percentage: Int): String{
        when (percentage){
            in 85..100 -> return "A"
            in 70..85 -> return "B"
            in 60..70 -> return "C"
            in 50..60 -> return "D"
        }

        return "F"
    }

    private fun getGradeComment(percentage: Int): String{
        when (percentage){
            in 85..100 -> return aArray.random()
            in 70..84 -> return bArray.random()
            in 60..69 -> return cArray.random()
            in 50..59 -> return dArray.random()
            else -> return fArray.random()
        }

        return "Who cares"
    }
}