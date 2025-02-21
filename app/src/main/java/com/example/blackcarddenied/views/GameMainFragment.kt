package com.ayoapps.blackcarddenied.views

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ayoapps.blackcarddenied.R
import com.ayoapps.blackcarddenied.UiState
import com.ayoapps.blackcarddenied.databinding.FragmentGameMainBinding
import com.ayoapps.blackcarddenied.viewmodels.GameMainViewModel
import com.example.blackcarddenied.models.GameData
import com.example.blackcarddenied.models.RandomRound
import com.example.blackcarddenied.views.GameSummaryFragment
import com.example.blackcarddenied.views.RoundDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.fragment.app.FragmentManager

class GameMainFragment : Fragment() {

    private val subjects = arrayOf("math", "science", "music", "movies", "tv shows", "art", "decades", "history",
        "animals", "geography", "current events", "pop culture", "food", "sports")
    private val difficulty = arrayOf("very easy", "easy", "medium", "hard", "very hard")

    private lateinit var binding: FragmentGameMainBinding
    //private val gameData = GameData()
  //  private lateinit var gameMainViewModel: GameMainViewModel
    private val gameMainViewModel: GameMainViewModel by activityViewModels()

    private var countdownTimer: CountDownTimer? = null



    companion object {
        fun newInstance() = GameMainFragment()
    }

  //  private val gameMainViewModel: GameMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // gameMainViewModel.completeReset()
        val roundDialogFragment = RoundDialogFragment()
        roundDialogFragment.show(parentFragmentManager, "RoundDialogFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      //  gameMainViewModel = ViewModelProvider(this).get(GameMainViewModel::class.java)

        binding = FragmentGameMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner // Set lifecycle owner for LiveData updates
        binding.gameViewModel = gameMainViewModel
        binding.gameData = gameMainViewModel.gameData.value



        // Update the round name for testing
       // gameData.roundName = "Round 1"

        gameMainViewModel.pickARound()
        gameMainViewModel.generateRandomPoints()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameMainViewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            Log.d(this::class.simpleName, "uiState: $uiState")
            // Update UI based on the new state
            if (uiState == UiState.Finish){
                showSummary()
                gameMainViewModel.updateUiState(UiState.Initial)
            }
        }
    }

    private fun showSummary(){
        val destinationFragment = GameSummaryFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, destinationFragment)
            .addToBackStack("gameToSummary")
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        countdownTimer?.cancel() // Cancel the timer to avoid memory leaks
    }
}