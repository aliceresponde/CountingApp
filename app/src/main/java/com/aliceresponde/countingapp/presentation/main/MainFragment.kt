package com.aliceresponde.countingapp.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.aliceresponde.countingapp.R
import com.aliceresponde.countingapp.data.remote.NetworkConnection
import com.aliceresponde.countingapp.databinding.FragmentMainBinding
import com.aliceresponde.countingapp.domain.model.Counter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : Fragment(), CounterAdapterListeners {

    private val viewModel: MainViewModel by viewModels()
    private val adapter: CounterAdapter by lazy { CounterAdapter(callback = this) }
    private lateinit var binding: FragmentMainBinding

    @Inject
    lateinit var networkConnection: NetworkConnection

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
                this@MainFragment.viewModel.getAllCounters(networkConnection.isConnected())
                swipeToRefresh.isRefreshing = false
            }
            swipeToRefresh.setColorSchemeResources(R.color.orangeColor)

            retry.setOnClickListener {
                this@MainFragment.viewModel.getAllCounters(networkConnection.isConnected())
            }
            cruzBtn.setOnClickListener { this@MainFragment.viewModel.clearCurrentSelection() }
            deleteView.setOnClickListener { showDeleteCounterDialog() }
            shareBtn.setOnClickListener {
                shareCounter()
                this@MainFragment.viewModel.clearCurrentSelection()
            }
        }

        setupObservers()

        viewModel.getAllCounters(networkConnection.isConnected())
        return binding.root
    }

    private fun setupObservers() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Counter>("key")?.observe(viewLifecycleOwner, Observer {
            newCounter -> viewModel.addNewCounter(newCounter)
        })
        viewModel.counters.observe(viewLifecycleOwner, Observer {
            binding.countersLabel.text = getString(R.string.items, viewModel.countCounters())
            binding.totalCounters.text = getString(R.string.times, viewModel.getCountersTimes())
            adapter.update(it)
        })

        viewModel.selectedCounters.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) adapter.selectCounter(it.last())
            else adapter.removeSelectedCounters()
        })

        viewModel.deleteInternetError.observe(viewLifecycleOwner, Observer {
            showDeleteInternetErrorDialog()
        })

        viewModel.increaseCounterInternetError.observe(viewLifecycleOwner, Observer {
            showIncreaseCounterErrorDialog(it)
        })

        viewModel.decreaseCounterInternetError.observe(viewLifecycleOwner, Observer {
            showDecreaseCounterErrorDialog(it)
        })
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
                .setPositiveButton(getString(R.string.dismis)) { dialog, _ -> dialog.dismiss() }
                .setNegativeButton(getString(R.string.retry)) { dialog, _ ->
                    dialog.dismiss()
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
                .setPositiveButton(getString(R.string.dismis)) { dialog, _ -> dialog.dismiss() }
                .setNegativeButton(getString(R.string.retry)) { dialog, _ ->
                    dialog.dismiss()
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
                .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
            val dialog = builder.create()
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    private fun showDeleteCounterDialog() {
        activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            val last = viewModel.selectedCounters.value!!.last()
            builder.setMessage(getString(R.string.delete_counter_title, last.title ?: ""))
                .setPositiveButton(getString(R.string.delete)) { dialog, id ->
                    dialog.dismiss()
                    viewModel.deleteSelectedCounter(networkConnection.isConnected())
                }
                .setNegativeButton(getString(R.string.cancel)) { dialog, id -> dialog.dismiss() }
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
