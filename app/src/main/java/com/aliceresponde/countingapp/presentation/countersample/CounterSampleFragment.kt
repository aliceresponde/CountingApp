package com.aliceresponde.countingapp.presentation.countersample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.aliceresponde.countingapp.R
import com.aliceresponde.countingapp.databinding.FragmentCounterSampleBinding
import com.aliceresponde.countingapp.presentation.common.BaseAppFragment
import com.aliceresponde.countingapp.presentation.common.getStringArray
import com.aliceresponde.countingapp.presentation.common.viewBinding

class CounterSampleFragment : BaseAppFragment(R.layout.fragment_counter_sample) {

    private val binding by viewBinding(FragmentCounterSampleBinding::bind)

    private val drinksAdapter: SampleAdapter by lazy {
        SampleAdapter(getStringArray(R.array.drinks), ::backWithSelectedCounter)
    }
    private val foodAdapter: SampleAdapter by lazy {
        SampleAdapter(getStringArray(R.array.food), ::backWithSelectedCounter)
    }
    private val miscAdapter: SampleAdapter by lazy {
        SampleAdapter(getStringArray(R.array.misc), ::backWithSelectedCounter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    override fun setupUi() {
        super.setupUi()
        binding.apply {
            drinksRecycler.adapter = drinksAdapter
            foodRecycler.adapter = foodAdapter
            miscRecycler.adapter = miscAdapter
            backView.setOnClickListener { view ->
                view?.let { Navigation.findNavController(it).popBackStack() }
            }
        }
    }

    private fun backWithSelectedCounter(title: String) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set("counter_title", title)
        findNavController().navigateUp()
    }
}