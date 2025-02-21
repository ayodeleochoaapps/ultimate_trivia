package com.example.blackcarddenied.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.ayoapps.blackcarddenied.R
import com.ayoapps.blackcarddenied.databinding.FragmentGameSummaryBinding
import com.ayoapps.blackcarddenied.viewmodels.GameMainViewModel
import com.ayoapps.blackcarddenied.views.GameMainFragment

class GameSummaryFragment : Fragment() {
    private val gameMainViewModel: GameMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGameSummaryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.gameViewModel = gameMainViewModel // Bind the shared ViewModel to the UI

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the close button
        view.findViewById<Button>(R.id.nextRoundButton).setOnClickListener {
            val destinationFragment = GameMainFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, destinationFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}