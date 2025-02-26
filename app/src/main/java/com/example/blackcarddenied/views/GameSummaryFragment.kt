package com.example.blackcarddenied.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.ayoapps.blackcarddenied.R
import com.ayoapps.blackcarddenied.databinding.FragmentGameSummaryBinding
import com.ayoapps.blackcarddenied.viewmodels.GameMainViewModel
import com.ayoapps.blackcarddenied.views.GameMainFragment


class GameSummaryFragment : Fragment() {
    // Use 'activityViewModels()' to get the shared ViewModel
    private val gameMainViewModel: GameMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using data binding
        val binding = FragmentGameSummaryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.gameViewModel = gameMainViewModel // Bind the shared ViewModel to the UI

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the next round button click listener (example of navigation)
        view.findViewById<Button>(R.id.nextRoundButton).setOnClickListener {
            val destinationFragment = GameMainFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, destinationFragment)
                .addToBackStack(null)
                .commit()
        }

        val gradeTextTitle: TextView = view.findViewById(R.id.txtGradeTitle)
        val gradeText: TextView = view.findViewById(R.id.txtGrade)

        if (gameMainViewModel.gameData.value?.gameOver == true){
            gradeText.visibility = View.VISIBLE
            gradeTextTitle.visibility = View.VISIBLE
            gradeText.text = calculateGrade(gameMainViewModel.gameData.value!!.totalScore.toInt())
        } else {
            gradeText.visibility = View.GONE
            gradeTextTitle.visibility = View.GONE
        }
    }

    private fun calculateGrade(grade: Int): String{
        when (grade){
            in 2250..3000 -> return "A"
            in 1750..2249 -> return "B"
            in 1250..1749 -> return "C"
            in 750..1249 -> return "D"
        }

        return "F"
    }
}