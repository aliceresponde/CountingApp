package com.aliceresponde.countingapp.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.aliceresponde.countingapp.R
import com.aliceresponde.countingapp.databinding.FragmentMainBinding
import com.aliceresponde.countingapp.domain.model.Counter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment(), CounterAdapterListeners {

    private val viewModel: MainViewModel by viewModels()
    private val adapter: CounterAdapter by lazy { CounterAdapter(callback = this) }
    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.apply {
            viewModel = this@MainFragment.viewModel
            lifecycleOwner = this@MainFragment
            countersList.adapter = adapter
//            toolbarLayout.toolbar.inflateMenu(R.menu.menu_main)
            createCounterBtn.setOnClickListener { navigateToCreateCounterFragment() }
//            swipeToRefresh.setOnRefreshListener { viewModel.getAllCounters() }
        }

        viewModel.counters.observe(viewLifecycleOwner, Observer { adapter.update(it) })
        viewModel.getAllCounters()
        return binding.root
    }

    override fun onPlusClicked(counter: Counter, position: Int) {
//        viewModel.increaseCounter(counter)
    }

    override fun onDecreaseClicked(counter: Counter, position: Int) {
//        viewModel.decreaseCounter(counter, position)
    }

    override fun onDelete(counter: Counter, position: Int) {
//        viewModel.delete(counter)
    }

    private fun navigateToCreateCounterFragment() {
        val action = MainFragmentDirections.actionMainFragmentToCreateItemkFragment()
        findNavController().navigate(action)
    }

//    fun hideToolBar() {
//        binding.toolbarLayout.toolbar.visibility = GONE
//    }
//
//    fun showToolBar() {
//        binding.toolbarLayout.toolbar.visibility = VISIBLE
//    }
}
