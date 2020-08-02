package com.aliceresponde.countingapp.presentation.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aliceresponde.countingapp.R
import com.aliceresponde.countingapp.databinding.FragmentWelcomeBinding


class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWelcomeBinding.inflate(inflater, container, false).apply {
            startButton.setOnClickListener {
                val action = WelcomeFragmentDirections.actionWelcomeFragmentToMainFragment()
                findNavController().navigate(action)
            }
        }

        return binding.root
    }
}