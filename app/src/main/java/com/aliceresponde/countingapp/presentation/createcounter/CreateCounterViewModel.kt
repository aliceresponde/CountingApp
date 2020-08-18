package com.aliceresponde.countingapp.presentation.createcounter

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.domain.usecase.create.CreateCounterUseCase
import com.aliceresponde.countingapp.presentation.common.Event
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateCounterViewModel @ViewModelInject constructor(private val createCounterUC: CreateCounterUseCase) :
    ViewModel() {

    private val _newCounter = MutableLiveData<Counter>()
    val newCounter: LiveData<Counter> get() = _newCounter

    private val _saveVisibility = MutableLiveData<Int>()
    val saveVisibility: LiveData<Int> get() = _saveVisibility

    private val _loadingVisibility = MutableLiveData<Int>(GONE)
    val loadingVisibility: LiveData<Int> get() = _loadingVisibility

    private val _showInternetError = MutableLiveData<Event<Boolean>>()
    val showInternetError: LiveData<Event<Boolean>> get() = _showInternetError


    fun createCounter(title: String, isInternetAccess: Boolean) {
        if (!isInternetAccess) _showInternetError.value = Event(true)
        else {
            try {
                viewModelScope.launch {
                    withContext(IO) {
                        showLoading()
                        val result = createCounterUC(title)
                        val data = result.data
                        data?.let {
                            _newCounter.postValue(data)
                        }
                        hideLoading()
                    }
                }
            } catch (e: Exception) {
                _saveVisibility.value = VISIBLE
                _loadingVisibility.value = GONE
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

}

