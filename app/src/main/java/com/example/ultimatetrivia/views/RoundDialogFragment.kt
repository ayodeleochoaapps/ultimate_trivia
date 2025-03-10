package com.example.ultimatetrivia.views

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ayoapps.ultimatetrivia.R
import com.ayoapps.ultimatetrivia.databinding.FragmentRoundDialogBinding
import com.ayoapps.ultimatetrivia.viewmodels.GameMainViewModel

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

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            val metrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(metrics)

            val width = (metrics.widthPixels * 0.9).toInt()  // 90% of screen width
            val height = (metrics.heightPixels * 0.8).toInt() // 70% of screen height

            window.setLayout(width, height)
            window.setBackgroundDrawableResource(R.drawable.rounded_button_white_blue)
        }
    }
}