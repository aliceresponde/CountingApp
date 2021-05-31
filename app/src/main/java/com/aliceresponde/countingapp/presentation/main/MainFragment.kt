package com.aliceresponde.countingapp.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aliceresponde.countingapp.R
import com.aliceresponde.countingapp.databinding.FragmentMainBinding
import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.presentation.common.BaseAppFragment
import com.aliceresponde.countingapp.presentation.common.EventObserver
import com.aliceresponde.countingapp.presentation.common.viewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : BaseAppFragment(R.layout.fragment_main), CounterAdapterListeners {

    private val binding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels()
    private val adapter: CounterAdapter by lazy { CounterAdapter(callback = this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupObservers()
        viewModel.synData(networkConnection.isConnected())
    }

    override fun setupUi() {
        super.setupUi()
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
            createCounterBtn.setOnClickListener {
                navigateToCreateCounterFragment()
            }
            swipeToRefresh.setOnRefreshListener {
                this@MainFragment.viewModel.synData(networkConnection.isConnected())
                swipeToRefresh.isRefreshing = false
            }
            swipeToRefresh.setColorSchemeResources(R.color.orangeColor)

            retry.setOnClickListener { this@MainFragment.viewModel.synData(networkConnection.isConnected()) }
            cruzBtn.setOnClickListener { this@MainFragment.viewModel.clearCurrentSelection() }
            deleteView.setOnClickListener { showDeleteCounterDialog() }
            shareBtn.setOnClickListener {
                shareCounter()
                this@MainFragment.viewModel.clearCurrentSelection()
            }
        }
    }


    override fun setupObservers() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Counter>("key")
            ?.observe(viewLifecycleOwner) { newCounter -> viewModel.addNewCounter(newCounter) }

        viewModel.apply {
            counters.observe(viewLifecycleOwner) {
                binding.countersLabel.text = getString(R.string.items, viewModel.countCounters())
                binding.totalCounters.text = getString(R.string.times, viewModel.getCountersTimes())
                adapter.update(it)
            }

            selectedCounters.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) adapter.selectCounter(it)
                else adapter.removeSelectedCounters()
            }

            deleteInternetError.observe(viewLifecycleOwner) {
                showDeleteInternetErrorDialog()
            }

            increaseCounterInternetError.observe(viewLifecycleOwner, EventObserver {
                showIncreaseCounterErrorDialog(it)
            })

            decreaseCounterInternetError.observe(viewLifecycleOwner, EventObserver {
                showDecreaseCounterErrorDialog(it)
            })
        }
    }


    private fun shareCounter() {
        viewModel.selectedCounters.value?.let {
            if (it.isNotEmpty()) {
                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, it.last().toString())
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.share)))
            }
        }
    }

    private fun showIncreaseCounterErrorDialog(counter: Counter) {
        activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            builder.setTitle(
                getString(
                    R.string.dialog_modify_counter_error,
                    counter.title,
                    counter.count + 1
                )
            )
                .setMessage(R.string.dialog_delete_counter_error_message)
                .setPositiveButton(getString(R.string.dismiss)) { dialog, _ -> dialog?.dismiss() }
                .setNegativeButton(getString(R.string.retry)) { dialog, _ ->
                    dialog?.dismiss()
                    viewModel.increaseCounter(counter, networkConnection.isConnected())
                }
            val dialog = builder.create()
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    private fun showDecreaseCounterErrorDialog(counter: Counter) {
        activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            builder.setTitle(
                getString(
                    R.string.dialog_modify_counter_error,
                    counter.title,
                    counter.count - 1
                )
            )
                .setMessage(R.string.dialog_delete_counter_error_message)
                .setPositiveButton(getString(R.string.dismiss)) { dialog, _ -> dialog?.dismiss() }
                .setNegativeButton(getString(R.string.retry)) { dialog, _ ->
                    dialog?.dismiss()
                    viewModel.decreaseCounter(counter, networkConnection.isConnected())
                }
            val dialog = builder.create()
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    private fun showDeleteInternetErrorDialog() {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.dialog_delete_counter_error_title))
            builder.setMessage(
                getString(
                    R.string.dialog_delete_counter_error_message
                )
            )
                .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog?.dismiss() }
            val dialog = builder.create()
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    private fun showDeleteCounterDialog() {
        activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            builder.setMessage(getString(R.string.delete_counter_title))
                .setPositiveButton(getString(R.string.delete)) { dialog, _ ->
                    dialog?.dismiss()
                    viewModel.deleteSelectedCounter(networkConnection.isConnected())
                }
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog?.dismiss()
                }
            val dialog = builder.create()
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    override fun onIncreaseCounter(counter: Counter, position: Int) {
        viewModel.increaseCounter(counter, networkConnection.isConnected())
    }

    override fun onDecreaseCounterClicked(counter: Counter, position: Int) {
        viewModel.decreaseCounter(counter, networkConnection.isConnected())
    }

    override fun onSelectedItem(counter: Counter, position: Int) {
        viewModel.addSelectedCounter(counter)
    }

    override fun onFiltered(list: List<Counter>) {
        viewModel.onFilterResult(list)
    }

    private fun navigateToCreateCounterFragment() {
        val action = MainFragmentDirections.actionMainFragmentToCreateItemkFragment()
        findNavController().navigate(action)
    }
}
