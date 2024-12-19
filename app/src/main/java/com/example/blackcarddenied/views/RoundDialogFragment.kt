package com.example.blackcarddenied.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.ayoapps.blackcarddenied.R
import com.ayoapps.blackcarddenied.databinding.FragmentGameMainBinding
import com.ayoapps.blackcarddenied.databinding.FragmentRoundDialogBinding
import com.ayoapps.blackcarddenied.viewmodels.GameMainViewModel
import com.example.blackcarddenied.models.GameData

class RoundDialogFragment : DialogFragment() {

    private val gameMainViewModel: GameMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRoundDialogBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.gameViewModel = gameMainViewModel // Bind the shared ViewModel to the UI

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the close button
        view.findViewById<Button>(R.id.startButton).setOnClickListener {
            gameMainViewModel.startRound()
            dismiss() // Close the dialog
        }
    }
}