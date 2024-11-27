package com.ayoapps.blackcarddenied.views

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ayoapps.blackcarddenied.R
import com.ayoapps.blackcarddenied.viewmodels.GameMainViewModel
import com.example.blackcarddenied.views.RoundDialogFragment

class GameMainFragment : Fragment() {

    private val subjects = arrayOf("math", "science", "music", "movies", "tv shows", "art", "decades", "history",
        "animals", "geography", "current events", "pop culture", "food", "sports")
    private val difficulty = arrayOf("very easy", "easy", "medium", "hard", "very hard")

    companion object {
        fun newInstance() = GameMainFragment()
    }

    private val gameMainViewModel: GameMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       val question1 = gameMainViewModel.loadQuestion(subjects.random(), difficulty.random())
       Log.d("question1", question1.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        RoundDialogFragment().show(parentFragmentManager, "MyDialog")

        return inflater.inflate(R.layout.fragment_game_main, container, false)
    }
}