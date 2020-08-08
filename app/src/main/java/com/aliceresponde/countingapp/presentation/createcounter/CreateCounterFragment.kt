package com.aliceresponde.countingapp.presentation.createcounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.aliceresponde.countingapp.R
import com.aliceresponde.countingapp.data.remote.NetworkConnection
import com.aliceresponde.countingapp.databinding.FragmentCreateCounterBinding
import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.presentation.common.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateCounterFragment : Fragment() {

    lateinit var binding: FragmentCreateCounterBinding
    private val viewModel: CreateCounterViewModel by viewModels()

    @Inject
    lateinit var networkConnection: NetworkConnection


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_counter, container, false)
        binding.apply {
            lifecycleOwner = this@CreateCounterFragment
            viewModel = this@CreateCounterFragment.viewModel
            seeSamples.setOnClickListener {
                val action =
                    CreateCounterFragmentDirections.actionCreateCounterFragmentToCounterSampleFragment()
                findNavController().navigate(action)
            }

            cruzBtn.setOnClickListener {
                counterTitleEdit.text?.clear()
                hideKeyboard()
            }

            saveBtn.setOnClickListener {
                hideKeyboard()
                val title = counterTitleEdit.text.toString()
                this@CreateCounterFragment.viewModel.createCounter(
                    title,
                    networkConnection.isConnected()
                )
            }
        }

        setupObservers()

        return binding.root
    }

    private fun setupObservers() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("counter_title")
            ?.observe(viewLifecycleOwner, Observer {
                binding.counterTitleEdit.setText(it)
                binding.counterTitleEdit.selectionEnd
            })

        viewModel.newCounter.observe(viewLifecycleOwner, Observer {
            navigateToMainScreen(it)
        })

        viewModel.showInternetError.observe(viewLifecycleOwner, Observer {
            showNoInternetDialog()
        })
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