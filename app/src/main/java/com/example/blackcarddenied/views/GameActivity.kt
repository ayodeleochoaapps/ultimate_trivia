package com.ayoapps.blackcarddenied.views

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ayoapps.blackcarddenied.R

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        supportActionBar?.hide()  // Hide ActionBar programmatically


        // Check if this is the first time the activity is being created
        if (savedInstanceState == null) {
            // Create an instance of the fragment
            val fragment = GameMainFragment()

            // Use the FragmentManager to start a transaction
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Replace the container with the fragment
                .commit() // Commit the transaction
        }
    }
}