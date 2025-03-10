package com.ayoapps.ultimatetrivia.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ayoapps.ultimatetrivia.BuildConfig
import com.ayoapps.ultimatetrivia.R
import com.ayoapps.ultimatetrivia.viewmodels.GameMainViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity() {

    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var gameViewModel: GameMainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        supportActionBar?.hide()  // Hide ActionBar programmatically

        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@GameActivity) {}
        }

        gameViewModel = ViewModelProvider(this).get(GameMainViewModel::class.java)

        gameViewModel.showAd.observe(this) { shouldTrigger ->
            Log.d(javaClass.simpleName, "Admob: shouldTrigger: $shouldTrigger")
            if (shouldTrigger) {
                showAdMobAd() // Call the function in Activity
            }
        }

        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(javaClass.simpleName, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                Log.d(javaClass.simpleName, "Ad dismissed fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(javaClass.simpleName, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(javaClass.simpleName, "Ad showed fullscreen content.")
            }
        }

        // Check if this is the first time the activity is being created
        if (savedInstanceState == null) {
            // Create an instance of the fragment
            val fragment = GameMainFragment()

            // Use the FragmentManager to start a transaction
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    fragment
                ) // Replace the container with the fragment
                .commit() // Commit the transaction
        }
    }

    fun loadAdMobAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            BuildConfig.AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError.toString().let { Log.d(javaClass.simpleName, "Admob:" + it) }
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(javaClass.simpleName, "Admob: Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }
            })
    }

    fun showAdMobAd(){
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Log.d(javaClass.simpleName, "The interstitial ad wasn't ready yet.")
        }
    }
}