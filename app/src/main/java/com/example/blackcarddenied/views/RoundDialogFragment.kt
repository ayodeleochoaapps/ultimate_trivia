package com.example.blackcarddenied.views

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ayoapps.blackcarddenied.R
import com.ayoapps.blackcarddenied.databinding.FragmentRoundDialogBinding
import com.ayoapps.blackcarddenied.viewmodels.GameMainViewModel

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
/*        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawableResource(Color)) // Optional transparent background
        }*/
    }

}