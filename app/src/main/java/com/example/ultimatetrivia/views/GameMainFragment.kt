package com.ayoapps.ultimatetrivia.views

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.ayoapps.ultimatetrivia.R
import com.ayoapps.ultimatetrivia.UiState
import com.ayoapps.ultimatetrivia.databinding.FragmentGameMainBinding
import com.ayoapps.ultimatetrivia.viewmodels.GameMainViewModel
import com.example.ultimatetrivia.views.GameSummaryFragment
import com.example.ultimatetrivia.views.RoundDialogFragment

class GameMainFragment : Fragment() {

    private lateinit var binding: FragmentGameMainBinding
    private val gameMainViewModel: GameMainViewModel by activityViewModels()
    private var countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val roundDialogFragment = RoundDialogFragment()
        roundDialogFragment.isCancelable = false
        roundDialogFragment.show(parentFragmentManager, "RoundDialogFragment")

        gameMainViewModel.initializeRounds()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentGameMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner // Set lifecycle owner for LiveData updates
        binding.gameViewModel = gameMainViewModel
        binding.gameData = gameMainViewModel.gameData.value

        gameMainViewModel.pickARound()

        gameMainViewModel.progressBarValue.observe(viewLifecycleOwner) { progress ->
            binding.timerProgressBar.progress = progress
        }

        (activity as GameActivity).loadAdMobAd()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameMainViewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            Log.d(this::class.simpleName, "uiState: $uiState")
            // Update UI based on the new state
            if (uiState == UiState.Finish) {
                showSummary()
                gameMainViewModel.updateUiState(UiState.Initial)
            }
        }
    }

    private fun showSummary() {
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