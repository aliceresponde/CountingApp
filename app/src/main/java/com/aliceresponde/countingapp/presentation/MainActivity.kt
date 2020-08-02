package com.aliceresponde.countingapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aliceresponde.countingapp.R
import com.aliceresponde.countingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}