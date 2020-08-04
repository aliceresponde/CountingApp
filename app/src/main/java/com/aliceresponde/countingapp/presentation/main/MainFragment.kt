package com.aliceresponde.countingapp.presentation.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
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
            searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter.filter.filter(newText)
                    return false
                }
            })
            createCounterBtn.setOnClickListener { navigateToCreateCounterFragment() }
            swipeToRefresh.setOnRefreshListener {
                this@MainFragment.viewModel.getAllCounters()
                swipeToRefresh.isRefreshing = false
            }
            cruzBtn.setOnClickListener { this@MainFragment.viewModel.clearCurrentSelection() }
            deleteView.setOnClickListener { showDeleteCounterDialog() }
        }

        viewModel.counters.observe(viewLifecycleOwner, Observer { adapter.update(it) })
        viewModel.selectedCounters.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) adapter.selectCounter(it.last())
            else adapter.removeSelectedCounters()
        })
        viewModel.getAllCounters()
        return binding.root
    }

    private fun showDeleteCounterDialog() {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            val last = viewModel.selectedCounters.value!!.last()
            builder.setMessage(
                getString(
                    R.string.delete_counter_title,
                    last.title ?: "")
            )
                .setPositiveButton(getString(R.string.delete)) { dialog, id ->
                    dialog.dismiss()
                    viewModel.deleteSelectedCounter()
                }
                .setNegativeButton(getString(R.string.cancel)) { dialog, id -> dialog.dismiss() }
            val dialog = builder.create()
            dialog.setCancelable(false)
            dialog.show()


        }

    }

    override fun onPlusClicked(counter: Counter, position: Int) {
//        viewModel.increaseCounter(counter)
    }

    override fun onDecreaseClicked(counter: Counter, position: Int) {
//        viewModel.decreaseCounter(counter, position)
    }

    override fun onSelectedItem(counter: Counter, position: Int) {
        viewModel.addSelectedItem(counter)
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
