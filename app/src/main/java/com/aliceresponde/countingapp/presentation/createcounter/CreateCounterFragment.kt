package com.aliceresponde.countingapp.presentation.createcounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.aliceresponde.countingapp.R
import com.aliceresponde.countingapp.data.remote.NetworkConnection
import com.aliceresponde.countingapp.databinding.FragmentCreateCounterBinding
import com.aliceresponde.countingapp.databinding.FragmentWelcomeBinding
import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.presentation.common.BaseAppFragment
import com.aliceresponde.countingapp.presentation.common.EventObserver
import com.aliceresponde.countingapp.presentation.common.hideKeyboard
import com.aliceresponde.countingapp.presentation.common.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateCounterFragment : BaseAppFragment(R.layout.fragment_create_counter) {

    private val binding by viewBinding(FragmentCreateCounterBinding::bind)
    private val viewModel: CreateCounterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupObservers()
    }

    override fun setupUi() {
        super.setupUi()
        binding.apply {
            lifecycleOwner = this@CreateCounterFragment
            viewModel = this@CreateCounterFragment.viewModel
            seeSamples.setOnClickListener {
                val action =
                    CreateCounterFragmentDirections.actionCreateCounterFragmentToCounterSampleFragment()
                findNavController().navigate(action)
            }

            cruzBtn.setOnClickListener { viewModel?.onCruzClicked() }

            saveBtn.setOnClickListener {
                hideKeyboard()
                val title = counterTitleEdit.text.toString()
                this@CreateCounterFragment.viewModel.createCounter(
                    title,
                    networkConnection.isConnected()
                )
            }
        }
    }

    private fun clearTitle() {
        binding.counterTitleEdit.text?.clear()
        hideKeyboard()
    }

    override fun setupObservers() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("counter_title")
            ?.observe(viewLifecycleOwner, Observer {
                binding.counterTitleEdit.setText(it)
                binding.counterTitleEdit.selectionEnd
            })

        viewModel.newCounter.observe(viewLifecycleOwner) { navigateToMainScreen(it) }
        viewModel.showEmptyCounter.observe(viewLifecycleOwner, EventObserver {
            if (it)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.empty_counter),
                    Toast.LENGTH_LONG
                ).show()
        })
        viewModel.showInternetError.observe(viewLifecycleOwner) { showNoInternetDialog() }
        viewModel.clearTitle.observe(viewLifecycleOwner, EventObserver {
            if (it) clearTitle()
        })
        viewModel.showCreateCounterError.observe(viewLifecycleOwner) {
            if (!it.hasBeenHandled)
                Toast.makeText(requireContext(), it.peekContent(), Toast.LENGTH_LONG).show()
        }
    }

    private fun showNoInternetDialog() {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.dialog_create_counter_error_title))
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

    private fun navigateToMainScreen(newCounter: Counter) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set("key", newCounter)
        findNavController().navigateUp()
    }
}