package com.aliceresponde.countingapp.presentation.main

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
import com.aliceresponde.countingapp.domain.usecase.decrease.DecreaseCounterUseCase
import com.aliceresponde.countingapp.domain.usecase.delete.DeleteCounterUseCase
import com.aliceresponde.countingapp.domain.usecase.getcounters.GetCountersUseCase
import com.aliceresponde.countingapp.domain.usecase.increase.IncreaseCounterUseCase
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ActivityScoped
class MainViewModel @ViewModelInject constructor(
    private val getCountersUC: GetCountersUseCase,
    private val increaseCounterUC: IncreaseCounterUseCase,
    private val decreaseCounterUC: DecreaseCounterUseCase,
    private val deleteCounterUC: DeleteCounterUseCase
) : ViewModel() {

    private val _loadingVisibility = MutableLiveData<Int>(GONE)
    val loadingVisibility: LiveData<Int> get() = _loadingVisibility

    private val _searchBarVisibility = MutableLiveData<Int>(VISIBLE)
    val searchBarVisibility: LiveData<Int> get() = _searchBarVisibility

    private val _selectedItemBarVisibility = MutableLiveData<Int>(GONE)
    val selectedItemBarVisibility: LiveData<Int> get() = _selectedItemBarVisibility

    private val _noDataVisibility = MutableLiveData<Int>(GONE)
    val noDataVisibility: LiveData<Int> get() = _noDataVisibility

    private val _internetErrorVisibility = MutableLiveData<Int>(GONE)
    val internetErrorVisibility: LiveData<Int> get() = _internetErrorVisibility

    private val _addCounterVisibility = MutableLiveData<Int>(GONE)
    val addCounterVisibility: LiveData<Int> get() = _addCounterVisibility

    private val _counterListVisibility = MutableLiveData<Int>(GONE)
    val counterListVisibility: LiveData<Int> get() = _counterListVisibility

    private val _isFilteredResultEmptyVisibility = MutableLiveData<Int>()
    val isFilteredResultEmptyVisibility: LiveData<Int> get() = _isFilteredResultEmptyVisibility

    private val _counters = MutableLiveData<MutableList<Counter>>()
    val counters: LiveData<MutableList<Counter>> get() = _counters

    private val _selectedCounters = MutableLiveData<MutableList<Counter>>()
    val selectedCounters: LiveData<MutableList<Counter>> get() = _selectedCounters

    private val _deleteInternetError = MutableLiveData<Boolean>()
    val deleteInternetError: LiveData<Boolean> get() = _deleteInternetError

    private val _increaseCounterInternetError = MutableLiveData<Counter>()
    val increaseCounterInternetError: LiveData<Counter> get() = _increaseCounterInternetError

    private val _decreaseCounterInternetError = MutableLiveData<Counter>()
    val decreaseCounterInternetError: LiveData<Counter> get() = _decreaseCounterInternetError

    fun getAllCounters(isInternetAccess: Boolean) {
        if (!isInternetAccess) showInternetError()
        else
            viewModelScope.launch {
                withContext(IO) {
                    showLoading()
                    val result = getCountersUC()
                    when (result) {
                        is SuccessViewState -> setupUiContent(result.data ?: listOf())
                        is ErrorViewState -> showInternetError()
                    }
                }
            }
    }

    //

    fun increaseCounter(counter: Counter, isInternetAccess: Boolean) {
        if (!isInternetAccess) _increaseCounterInternetError.postValue(counter)
        else {
            viewModelScope.launch {
                withContext(IO) {
                    showLoading()
                    when (val result = increaseCounterUC(counter.id)) {
                        is SuccessViewState -> setupUiContent(result.data ?: listOf())
                        is ErrorViewState -> showInternetError()
                    }
                }
            }
        }
    }

    fun decreaseCounter(counter: Counter, isInternetAccess: Boolean) {
        if (!isInternetAccess) _decreaseCounterInternetError.postValue(counter)
        else if (counter.count == 0) return
        else {
            viewModelScope.launch {
                withContext(IO) {
                    showLoading()
                    when (val result = decreaseCounterUC(counter.id)) {
                        is SuccessViewState -> setupUiContent(result.data ?: listOf())
                        is ErrorViewState -> showInternetError()
                    }
                }
            }
        }
    }

    fun deleteSelectedCounter(isInternetAccess: Boolean) {
        if (!isInternetAccess) _deleteInternetError.postValue(true)
        else {
            val counters = selectedCounters.value!!
            val counterIss = counters.map { it.id }
            viewModelScope.launch {
                withContext(IO) {
                    showLoading()
                    when (val result = deleteCounterUC(counterIss)) {
                        is SuccessViewState -> {
                            val data: List<Counter> = result.data ?: listOf()
                            setupUiContent(data)
                        }
                        is ErrorViewState -> showInternetError()
                    }
                }
            }
            _selectedCounters.value?.clear()
        }
    }

    private fun setupUiContent(counters: List<Counter>) {
        if (counters.isEmpty()) showEmptyData()
        else showData(counters)
    }

    fun addSelectedCounter(counter: Counter) {
        val current = (_selectedCounters.value ?: mutableListOf()).also { it.add(counter) }
        showSelectedItemToolBoar()
        _selectedCounters.postValue(current)
    }

    fun addNewCounter(counter: Counter) {
        val current = (_counters.value ?: mutableListOf()).also { it.add(counter) }
        _counters.postValue(current)
    }

    fun countCounters(): Int = counters.value?.size ?: 0

    fun getCountersTimes(): Int {
        var times = 0
        counters.value?.forEach { times += it.count }
        return times
    }

    fun onFilterResult(list: List<Counter>) {
        if (list.isEmpty()) showEmptyFilteredResult()
    }

    private fun showEmptyFilteredResult() {
        _isFilteredResultEmptyVisibility.postValue(VISIBLE)
        _searchBarVisibility.postValue(VISIBLE)
        _addCounterVisibility.postValue(VISIBLE)
        _loadingVisibility.postValue(GONE)
        _noDataVisibility.postValue(GONE)
        _internetErrorVisibility.postValue(GONE)
        _counterListVisibility.postValue(GONE)
        _selectedItemBarVisibility.postValue(GONE)
    }

    private fun showInternetError() {
        _isFilteredResultEmptyVisibility.postValue(GONE)
        _loadingVisibility.postValue(GONE)
        _noDataVisibility.postValue(GONE)
        _internetErrorVisibility.postValue(VISIBLE)
        _addCounterVisibility.postValue(VISIBLE)
        _counterListVisibility.postValue(GONE)
        _selectedItemBarVisibility.postValue(GONE)
        _searchBarVisibility.postValue(VISIBLE)
    }

    private fun showData(counters: List<Counter>) {
        _counters.postValue(counters.toMutableList())
        _isFilteredResultEmptyVisibility.postValue(GONE)
        _addCounterVisibility.postValue(VISIBLE)
        _counterListVisibility.postValue(VISIBLE)
        _loadingVisibility.postValue(GONE)
        _noDataVisibility.postValue(GONE)
        _internetErrorVisibility.postValue(GONE)
        _selectedItemBarVisibility.postValue(GONE)
        _searchBarVisibility.postValue(VISIBLE)
    }

    private fun showEmptyData() {
        _isFilteredResultEmptyVisibility.postValue(GONE)
        _noDataVisibility.postValue(VISIBLE)
        _addCounterVisibility.postValue(VISIBLE)
        _loadingVisibility.postValue(GONE)
        _internetErrorVisibility.postValue(GONE)
        _counterListVisibility.postValue(GONE)
        _selectedItemBarVisibility.postValue(GONE)
        _searchBarVisibility.postValue(VISIBLE)
    }

    private fun showLoading() {
        _isFilteredResultEmptyVisibility.postValue(GONE)
        _loadingVisibility.postValue(VISIBLE)
        _noDataVisibility.postValue(GONE)
        _internetErrorVisibility.postValue(GONE)
        _addCounterVisibility.postValue(VISIBLE)
        _counterListVisibility.postValue(GONE)
        _selectedItemBarVisibility.postValue(GONE)
        _searchBarVisibility.postValue(VISIBLE)
    }

    private fun showSelectedItemToolBoar() {
        _isFilteredResultEmptyVisibility.postValue(GONE)
        _loadingVisibility.postValue(GONE)
        _noDataVisibility.postValue(GONE)
        _internetErrorVisibility.postValue(GONE)
        _addCounterVisibility.postValue(GONE)
        _searchBarVisibility.postValue(GONE)

        _counterListVisibility.postValue(VISIBLE)
        _selectedItemBarVisibility.postValue(VISIBLE)
    }

    fun clearCurrentSelection() {
        _isFilteredResultEmptyVisibility.postValue(GONE)
        _counterListVisibility.postValue(VISIBLE)
        _addCounterVisibility.postValue(VISIBLE)
        _searchBarVisibility.postValue(VISIBLE)
        _selectedItemBarVisibility.postValue(GONE)
        _selectedCounters.postValue(mutableListOf())
    }
}