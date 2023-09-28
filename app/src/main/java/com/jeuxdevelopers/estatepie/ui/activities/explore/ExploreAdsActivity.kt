package com.jeuxdevelopers.estatepie.ui.activities.explore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jeuxdevelopers.estatepie.databinding.ActivityExploreAdsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreAdsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityExploreAdsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}