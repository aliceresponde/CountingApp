package com.aliceresponde.countingapp.presentation.welcome

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.aliceresponde.countingapp.R
import com.aliceresponde.countingapp.databinding.FragmentWelcomeBinding
import com.aliceresponde.countingapp.presentation.common.BaseAppFragment
import com.aliceresponde.countingapp.presentation.common.viewBinding


class WelcomeFragment : BaseAppFragment(R.layout.fragment_welcome) {
    private val binding by viewBinding(FragmentWelcomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    override fun setupUi() {
        super.setupUi()
        binding.apply {
            startButton.setOnClickListener {
                val action = WelcomeFragmentDirections.actionWelcomeFragmentToMainFragment()
                findNavController().navigate(action)
            }
        }
    }
}