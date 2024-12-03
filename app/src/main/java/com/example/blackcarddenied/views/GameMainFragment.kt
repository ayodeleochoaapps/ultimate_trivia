package com.ayoapps.blackcarddenied.views

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.ayoapps.blackcarddenied.R
import com.ayoapps.blackcarddenied.databinding.FragmentGameMainBinding
import com.ayoapps.blackcarddenied.viewmodels.GameMainViewModel
import com.example.blackcarddenied.models.RandomRound
import com.example.blackcarddenied.views.RoundDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

/*        val binding: FragmentGameMainBinding = view?.let { DataBindingUtil.bind(it) }!!
        //  binding.lifecycleOwner = this

        gameMainViewModel.pickARound()

        // Observing LiveData from the ViewModel
        gameMainViewModel.roundName.observe(viewLifecycleOwner, Observer { newText ->
            // Update the TextView when the data changes
            binding.txtRoundName.text = newText
        })*/

        RoundDialogFragment().show(parentFragmentManager, "MyDialog")

        return inflater.inflate(R.layout.fragment_game_main, container, false)
    }
}