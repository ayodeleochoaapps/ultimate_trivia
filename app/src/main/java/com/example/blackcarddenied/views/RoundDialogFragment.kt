package com.example.blackcarddenied.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.ayoapps.blackcarddenied.R
import com.ayoapps.blackcarddenied.viewmodels.GameMainViewModel

class RoundDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_round_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameMainViewModel = GameMainViewModel()

        // Set up the close button
        view.findViewById<Button>(R.id.startButton).setOnClickListener {
            gameMainViewModel.pickARound()
            dismiss() // Close the dialog
        }
    }
}