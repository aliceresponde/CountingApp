package com.aliceresponde.countingapp.presentation.main

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aliceresponde.countingapp.domain.Counter

class MainViewModel : ViewModel() {

    private val _loadingVisibility = MutableLiveData<Int>(GONE)
    val loadingVisibility: LiveData<Int> get() = _loadingVisibility

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

    fun setupContent(counters: List<Counter>) {
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
    }

    private fun showSwipeToDismiss() {
        _loadingVisibility.postValue(GONE)
        _noDataVisibility.postValue(GONE)
        _swipeToRefreshVisibility.postValue(VISIBLE)
        _internetErrorVisibility.postValue(GONE)
        _addCounterVisibility.postValue(VISIBLE)
        _counterListVisibility.postValue(VISIBLE)
    }

    private fun showInternetError() {
        _loadingVisibility.postValue(GONE)
        _swipeToRefreshVisibility.postValue(GONE)
        _noDataVisibility.postValue(GONE)
        _internetErrorVisibility.postValue(VISIBLE)
        _addCounterVisibility.postValue(VISIBLE)
        _counterListVisibility.postValue(GONE)
    }

    private fun showData(counters: List<Counter>) {
        _counters.postValue(counters)
        _addCounterVisibility.postValue(VISIBLE)
        _counterListVisibility.postValue(VISIBLE)
        _loadingVisibility.postValue(GONE)
        _swipeToRefreshVisibility.postValue(GONE)
        _noDataVisibility.postValue(GONE)
        _internetErrorVisibility.postValue(GONE)
    }

    private fun showEmptyData() {
        _loadingVisibility.postValue(GONE)
        _swipeToRefreshVisibility.postValue(GONE)
        _noDataVisibility.postValue(VISIBLE)
        _internetErrorVisibility.postValue(GONE)
        _addCounterVisibility.postValue(VISIBLE)
        _counterListVisibility.postValue(GONE)
    }

    private fun showLoading() {
        _loadingVisibility.postValue(VISIBLE)
        _swipeToRefreshVisibility.postValue(GONE)
        _noDataVisibility.postValue(GONE)
        _internetErrorVisibility.postValue(GONE)
        _addCounterVisibility.postValue(VISIBLE)
        _counterListVisibility.postValue(GONE)
    }
}