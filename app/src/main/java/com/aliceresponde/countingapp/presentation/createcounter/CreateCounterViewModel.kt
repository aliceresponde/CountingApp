package com.aliceresponde.countingapp.presentation.createcounter

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.domain.model.ErrorViewState
import com.aliceresponde.countingapp.domain.model.SuccessViewState
import com.aliceresponde.countingapp.domain.usecase.create.CreateCounterUseCase
import com.aliceresponde.countingapp.presentation.common.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CreateCounterViewModel @ViewModelInject constructor(
    private val createCounterUC: CreateCounterUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = IO,
) :
    ViewModel() {

    private val _newCounter = MutableLiveData<Counter>()
    val newCounter: LiveData<Counter> get() = _newCounter

    private val _saveVisibility = MutableLiveData<Int>()
    val saveVisibility: LiveData<Int> get() = _saveVisibility

    private val _loadingVisibility = MutableLiveData<Int>(GONE)
    val loadingVisibility: LiveData<Int> get() = _loadingVisibility

    private val _showInternetError = MutableLiveData<Event<Boolean>>()
    val showInternetError: LiveData<Event<Boolean>> get() = _showInternetError

    private val _clearTitle = MutableLiveData<Event<Boolean>>()
    val clearTitle: LiveData<Event<Boolean>> get() = _clearTitle

    private val _back = MutableLiveData<Event<Boolean>>()
    val back: LiveData<Event<Boolean>> get() = _back

    private val _showEmptyCounter = MutableLiveData<Event<Boolean>>()
    val showEmptyCounter: LiveData<Event<Boolean>> get() = _showEmptyCounter

    private val _showCreateCounterError = MutableLiveData<Event<String>>()
    val showCreateCounterError: LiveData<Event<String>> get() = _showCreateCounterError


    fun createCounter(title: String, isInternetAccess: Boolean) {
        if (!isInternetAccess) _showInternetError.value = Event(true)
        else if (title.isEmpty()) _showEmptyCounter.value = Event(true)
        else {
            viewModelScope.launch {
                withContext(coroutineDispatcher) {
                    showLoading()
                    when (val result = createCounterUC.invoke(title)) {
                        is ErrorViewState -> {
                            _showCreateCounterError.postValue(Event(result.message ?: ""))
                        }
                        is SuccessViewState -> {
                            result.data?.let { _newCounter.postValue(it) }
                        }
                    }
                    hideLoading()
                }
            }
        }
    }


    private fun showLoading() {
        _saveVisibility.postValue(GONE)
        _loadingVisibility.postValue(VISIBLE)
    }

    private fun hideLoading() {
        _saveVisibility.postValue(VISIBLE)
        _loadingVisibility.postValue(GONE)
    }

    fun onCruzClicked() {
        _clearTitle.value = Event(true)
    }
}

