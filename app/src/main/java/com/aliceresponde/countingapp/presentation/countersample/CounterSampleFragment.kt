package com.aliceresponde.countingapp.presentation.countersample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aliceresponde.countingapp.R
import com.aliceresponde.countingapp.databinding.FragmentCounterSampleBinding
import com.aliceresponde.countingapp.presentation.common.getStringArray

class CounterSampleFragment : Fragment() {

    private val drinksAdapter: SampleAdapter by lazy {
        SampleAdapter(getStringArray(R.array.drinks), ::backWithSelectedCounter)
    }
    private val foodAdapter: SampleAdapter by lazy {
        SampleAdapter(getStringArray(R.array.food), ::backWithSelectedCounter)
    }
    private val miscAdapter: SampleAdapter by lazy {
        SampleAdapter(getStringArray(R.array.misc), ::backWithSelectedCounter)
    }

    lateinit var binding: FragmentCounterSampleBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCounterSampleBinding.inflate(inflater, container, false).apply {
            drinksRecycler.adapter = drinksAdapter
            foodRecycler.adapter = foodAdapter
            miscRecycler.adapter = miscAdapter
        }

        return binding.root
    }

    private fun backWithSelectedCounter(title: String) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set("counter_title", title)
        findNavController().navigateUp()
    }
}