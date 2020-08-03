package com.aliceresponde.countingapp.presentation.main

import android.opengl.Visibility
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.constraintlayout.solver.GoalRow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliceresponde.countingapp.domain.DecreaseCounterUseCase
import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.domain.model.ErrorState
import com.aliceresponde.countingapp.domain.model.SuccessState
import com.aliceresponde.countingapp.domain.usecase.delete.DeleteCounterUseCase
import com.aliceresponde.countingapp.domain.usecase.increase.IncreaseCounterUseCase
import com.aliceresponde.countingapp.domain.usecase.getcounters.GetCountersUseCase
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ActivityScoped
class MainViewModel(
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

    private val _swipeToRefreshVisibility = MutableLiveData<Int>(GONE)
    val swipeToRefreshVisibility: LiveData<Int> get() = _swipeToRefreshVisibility

    private val _noDataVisibility = MutableLiveData<Int>(GONE)
    val noDataVisibility: LiveData<Int> get() = _noDataVisibility

    private val _internetErrorVisibility = MutableLiveData<Int>(GONE)
    val internetErrorVisibility: LiveData<Int> get() = _internetErrorVisibility

    private val _addCounterVisibility = MutableLiveData<Int>(GONE)
    val addCounterVisibility: LiveData<Int> get() = _addCounterVisibility

    private val _counterListVisibility = MutableLiveData<Int>(GONE)
    val counterListVisibility: LiveData<Int> get() = _counterListVisibility

    private val _counters = MutableLiveData<List<Counter>>()
    val counters: LiveData<List<Counter>> get() = _counters

    fun getAllCounters() {
        viewModelScope.launch {
            withContext(IO) {
                showLoading()
                val result = getCountersUC()
                when (result) {
                    is SuccessState -> setupUiContent(result.data ?: listOf())
                    is ErrorState -> showInternetError()
                }
            }
        }
    }

    fun increaseCounter(counter: Counter) {
        viewModelScope.launch {
            withContext(IO) {
                showLoading()
                val result = increaseCounterUC(counter.id)
                when (result) {
                    is SuccessState -> setupUiContent(result.data ?: listOf())
                    is ErrorState -> showInternetError()
                }
            }
        }
    }

    fun decreaseCounter(counter: Counter, position: Int) {
        viewModelScope.launch {
            withContext(IO) {
                showLoading()
                val result = decreaseCounterUC(counter.id)
                when (result) {
                    is SuccessState -> setupUiContent(result.data ?: listOf())
                    is ErrorState -> showInternetError()
                }
            }
        }
    }

    fun delete(counter: Counter) {
        viewModelScope.launch {
            withContext(IO) {
                showLoading()
                val result = deleteCounterUC(counter.id)
                when (result) {
                    is SuccessState -> setupUiContent(result.data ?: listOf())
                    is ErrorState -> showInternetError()
                }
            }
        }
    }

    private fun setupUiContent(counters: List<Counter>) {
        if (counters.isEmpty()) showEmptyData()
        else showData(counters)
    }

    private fun hideAll() {
        _loadingVisibility.postValue(GONE)
        _swipeToRefreshVisibility.postValue(GONE)
        _noDataVisibility.postValue(GONE)
        _internetErrorVisibility.postValue(GONE)
        _addCounterVisibility.postValue(GONE)
        _counterListVisibility.postValue(GONE)
        _selectedItemBarVisibility.postValue(GONE)
        _searchBarVisibility.postValue(VISIBLE)
    }

    private fun showSwipeToDismiss() {
        _loadingVisibility.postValue(GONE)
        _noDataVisibility.postValue(GONE)
        _swipeToRefreshVisibility.postValue(VISIBLE)
        _internetErrorVisibility.postValue(GONE)
        _addCounterVisibility.postValue(VISIBLE)
        _counterListVisibility.postValue(VISIBLE)
        _selectedItemBarVisibility.postValue(GONE)
        _searchBarVisibility.postValue(VISIBLE)
    }

    private fun showInternetError() {
        _loadingVisibility.postValue(GONE)
        _swipeToRefreshVisibility.postValue(GONE)
        _noDataVisibility.postValue(GONE)
        _internetErrorVisibility.postValue(VISIBLE)
        _addCounterVisibility.postValue(VISIBLE)
        _counterListVisibility.postValue(GONE)
        _selectedItemBarVisibility.postValue(GONE)
        _searchBarVisibility.postValue(VISIBLE)
    }

    private fun showData(counters: List<Counter>) {
        _counters.postValue(counters)
        _addCounterVisibility.postValue(VISIBLE)
        _counterListVisibility.postValue(VISIBLE)
        _loadingVisibility.postValue(GONE)
        _swipeToRefreshVisibility.postValue(GONE)
        _noDataVisibility.postValue(GONE)
        _internetErrorVisibility.postValue(GONE)
        _selectedItemBarVisibility.postValue(GONE)
        _searchBarVisibility.postValue(VISIBLE)
    }

    private fun showEmptyData() {
        _noDataVisibility.postValue(VISIBLE)
        _addCounterVisibility.postValue(VISIBLE)
        _loadingVisibility.postValue(GONE)
        _swipeToRefreshVisibility.postValue(GONE)
        _internetErrorVisibility.postValue(GONE)
        _counterListVisibility.postValue(GONE)
        _selectedItemBarVisibility.postValue(GONE)
        _searchBarVisibility.postValue(VISIBLE)
    }

    private fun showLoading() {
        _loadingVisibility.postValue(VISIBLE)
        _swipeToRefreshVisibility.postValue(GONE)
        _noDataVisibility.postValue(GONE)
        _internetErrorVisibility.postValue(GONE)
        _addCounterVisibility.postValue(VISIBLE)
        _counterListVisibility.postValue(GONE)
        _selectedItemBarVisibility.postValue(GONE)
        _searchBarVisibility.postValue(VISIBLE)
    }
}