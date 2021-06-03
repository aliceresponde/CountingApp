package com.aliceresponde.countingapp.presentation.main

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.domain.model.ErrorViewState
import com.aliceresponde.countingapp.domain.model.SuccessViewState
import com.aliceresponde.countingapp.domain.usecase.decrease.DecreaseCounterUseCase
import com.aliceresponde.countingapp.domain.usecase.delete.DeleteCounterUseCase
import com.aliceresponde.countingapp.domain.usecase.getcounters.GetCountersUseCase
import com.aliceresponde.countingapp.domain.usecase.increase.IncreaseCounterUseCase
import com.aliceresponde.countingapp.presentation.common.Event
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest : MockkableTestBase() {

    @RelaxedMockK
    lateinit var loadingVisibilityOb: Observer<Int>

    @RelaxedMockK
    lateinit var searchBarVisibilityObserver: Observer<Int>

    @RelaxedMockK
    lateinit var selectedItemBarVisibilityObserver: Observer<Int>

    @RelaxedMockK
    lateinit var noDataVisibilityObserver: Observer<Int>

    @RelaxedMockK
    lateinit var internetErrorVisibilityObserver: Observer<Int>

    @RelaxedMockK
    lateinit var addCounterVisibilityObserver: Observer<Int>

    @RelaxedMockK
    lateinit var counterListVisibilityObserver: Observer<Int>

    @RelaxedMockK
    lateinit var isFilterResultEmptyVisibilityObserver: Observer<Int>

    @RelaxedMockK
    lateinit var countersObserver: Observer<MutableList<Counter>>

    @RelaxedMockK
    lateinit var selectedCountersObserver: Observer<MutableList<Counter>>

    @RelaxedMockK
    lateinit var deleteInternetErrorObserver: Observer<Event<Boolean>>

    @RelaxedMockK
    lateinit var increaseCounterInternetErrorObserver: Observer<Event<Counter>>

    @RelaxedMockK
    lateinit var decreaseCounterInternetErrorObserver: Observer<Event<Counter>>

    @RelaxedMockK
    lateinit var getCounterUC: GetCountersUseCase

    @RelaxedMockK
    lateinit var increaseCounterUC: IncreaseCounterUseCase

    @RelaxedMockK
    lateinit var decreaseCounterUseCase: DecreaseCounterUseCase

    @RelaxedMockK
    lateinit var deleteCounterUC: DeleteCounterUseCase

    lateinit var sut: MainViewModel

    init {
        MockKAnnotations.init(this)
    }

    @Before
    fun setUp() {
        sut = MainViewModel(
            getCounterUC,
            increaseCounterUC,
            decreaseCounterUseCase,
            deleteCounterUC,
            testDispatcher
        ).apply {
            loadingVisibility.observeForever(loadingVisibilityOb)
            searchBarVisibility.observeForever(searchBarVisibilityObserver)
            selectedItemBarVisibility.observeForever(selectedItemBarVisibilityObserver)
            noDataVisibility.observeForever(noDataVisibilityObserver)
            internetErrorVisibility.observeForever(internetErrorVisibilityObserver)
            addCounterVisibility.observeForever(addCounterVisibilityObserver)
            counterListVisibility.observeForever(counterListVisibilityObserver)
            isFilteredResultEmptyVisibility.observeForever(isFilterResultEmptyVisibilityObserver)
            counters.observeForever(countersObserver)
            selectedCounters.observeForever(selectedCountersObserver)
            deleteInternetError.observeForever(deleteInternetErrorObserver)
            increaseCounterInternetError.observeForever(increaseCounterInternetErrorObserver)
            decreaseCounterInternetError.observeForever(decreaseCounterInternetErrorObserver)
        }
    }

    @Test
    fun `syncData() returns empty list then show empty data`() {
        coEvery { getCounterUC.invoke() } answers { SuccessViewState(data = listOf()) }
        sut.synData()
        coVerify {
            initialValues()
            showLoading()
            showEmptyData()
        }
    }

    @Test
    fun `syncData() returns list of counters then show data`() {
        val data = listOf(counterWithZero)
        coEvery { getCounterUC.invoke() } answers { SuccessViewState(data = data) }
        sut.synData()
        coVerify {
            initialValues()
            showLoading()
            showData(data)
        }
    }

    @Test
    fun `syncData() returns errorViewState`() {
        val element = Counter("1", "nap", 0)
        val data = listOf(element)
        coEvery { getCounterUC.invoke() } answers { ErrorViewState(data = data, message = "") }
        sut.synData()
        coVerify {
            initialValues()
            showLoading()
            showInternetError()
        }
    }

    @Test
    fun `increaseCounter without internet access show internet error`() {
        sut.increaseCounter(counterWithZero, false)
        verify {
            initialValues()
            increaseCounterInternetErrorObserver.onChanged(any())
        }
    }

    @Test
    fun `increaseCounter with internet that is access show data`() {
        val result = listOf(counterWithZero.copy(count = counterWithZero.count + 1))
        coEvery { increaseCounterUC(id = counterWithZero.id) } answers { SuccessViewState(result) }
        sut.increaseCounter(counterWithZero, true)
        coVerify {
            initialValues()
            showLoading()
            showData(result)
        }
    }

    @Test
    fun `increaseCounter with internet that has error show internet error`() {
        val result = listOf(counterWithZero.copy(count = counterWithZero.count + 1))
        coEvery { increaseCounterUC(id = counterWithZero.id) } answers { ErrorViewState("") }
        sut.increaseCounter(counterWithZero, true)
        coVerify {
            initialValues()
            showLoading()
            showInternetError()
        }
    }

    @Test
    fun `decreaseCounter with internet failure emit decrease internet error event`() {
        sut.decreaseCounter(counterWithZero, false)
        verify {
            initialValues()
            decreaseCounterInternetErrorObserver.onChanged(any())
        }
    }

    @Test
    fun `decreaseCounter with zero count just returns`() {
        sut.decreaseCounter(counterWithZero, false)
        verify {
            initialValues()
        }
    }

    @Test
    fun `decreaseCounter with positive count returns success data`() {
        val result = listOf(counterWithZero)
        coEvery { decreaseCounterUseCase.invoke("1") } answers {
            SuccessViewState(listOf(counterWithZero))
        }
        sut.decreaseCounter(counterWithZero.copy(count = 2), true)
        coVerify {
            initialValues()
            showLoading()
            showData(result)
        }
    }

    @Test
    fun `decreaseCounter with positive count returns ErrorViewState showInternetError`() {
        coEvery { decreaseCounterUseCase.invoke("1") } answers { ErrorViewState("") }
        sut.decreaseCounter(counterWithZero.copy(count = 2), true)
        coVerify {
            initialValues()
            showLoading()
            showInternetError()
        }
    }

    @Test
    fun `onFilterResult with empty data show Empty fileterredResult`() {
        sut.onFilterResult(emptyList())
        verify {
            initialValues()
            showEmptyFilteredResult()
        }
    }

    @Test
    fun `onFilterResult with data then showData`() {
        val list = listOf(counterWithZero)
        sut.onFilterResult(list)
        verify {
            initialValues()
            showData(list)
        }
    }

    private fun initialValues() {
        loadingVisibilityOb.onChanged(GONE)
        searchBarVisibilityObserver.onChanged(VISIBLE)
        selectedItemBarVisibilityObserver.onChanged(GONE)
        noDataVisibilityObserver.onChanged(GONE)
        internetErrorVisibilityObserver.onChanged(GONE)
        addCounterVisibilityObserver.onChanged(GONE)
        counterListVisibilityObserver.onChanged(GONE)
    }

    private fun showLoading() { //8
        isFilterResultEmptyVisibilityObserver.onChanged(GONE)
        loadingVisibilityOb.onChanged(VISIBLE)
        noDataVisibilityObserver.onChanged(GONE)
        internetErrorVisibilityObserver.onChanged(GONE)
        addCounterVisibilityObserver.onChanged(VISIBLE)
        counterListVisibilityObserver.onChanged(GONE)
        selectedItemBarVisibilityObserver.onChanged(GONE)
        searchBarVisibilityObserver.onChanged(VISIBLE)
    }

    private fun showEmptyData() {
        loadingVisibilityOb.onChanged(VISIBLE)
        loadingVisibilityOb.onChanged(GONE)
        isFilterResultEmptyVisibilityObserver.onChanged(GONE)
        noDataVisibilityObserver.onChanged(VISIBLE)
        addCounterVisibilityObserver.onChanged(VISIBLE)
        loadingVisibilityOb.onChanged(GONE)
        internetErrorVisibilityObserver.onChanged(GONE)
        counterListVisibilityObserver.onChanged(GONE)
        selectedItemBarVisibilityObserver.onChanged(GONE)
        searchBarVisibilityObserver.onChanged(VISIBLE)
    }

    private fun showSelectedItemToolBoar() {
        isFilterResultEmptyVisibilityObserver.onChanged(GONE)
        loadingVisibilityOb.onChanged(GONE)
        noDataVisibilityObserver.onChanged(GONE)
        internetErrorVisibilityObserver.onChanged(GONE)
        addCounterVisibilityObserver.onChanged(GONE)
        searchBarVisibilityObserver.onChanged(GONE)

        counterListVisibilityObserver.onChanged(VISIBLE)
        selectedItemBarVisibilityObserver.onChanged(VISIBLE)
    }

    fun clearCurrentSelection() {
        isFilterResultEmptyVisibilityObserver.onChanged(GONE)
        counterListVisibilityObserver.onChanged(VISIBLE)
        addCounterVisibilityObserver.onChanged(VISIBLE)
        searchBarVisibilityObserver.onChanged(VISIBLE)
        selectedItemBarVisibilityObserver.onChanged(GONE)
        selectedCountersObserver.onChanged(mutableListOf())
    }

    private fun showEmptyFilteredResult() {
        isFilterResultEmptyVisibilityObserver.onChanged(VISIBLE)
        searchBarVisibilityObserver.onChanged(VISIBLE)
        addCounterVisibilityObserver.onChanged(VISIBLE)
        loadingVisibilityOb.onChanged(GONE)
        noDataVisibilityObserver.onChanged(GONE)
        internetErrorVisibilityObserver.onChanged(GONE)
        counterListVisibilityObserver.onChanged(GONE)
        selectedItemBarVisibilityObserver.onChanged(GONE)
    }

    private fun showInternetError() {
        isFilterResultEmptyVisibilityObserver.onChanged(GONE)
        loadingVisibilityOb.onChanged(GONE)
        noDataVisibilityObserver.onChanged(GONE)
        internetErrorVisibilityObserver.onChanged(VISIBLE)
        addCounterVisibilityObserver.onChanged(VISIBLE)
        counterListVisibilityObserver.onChanged(GONE)
        selectedItemBarVisibilityObserver.onChanged(GONE)
        searchBarVisibilityObserver.onChanged(VISIBLE)
    }

    private fun showData(counters: List<Counter>) {
        countersObserver.onChanged(counters.toMutableList())
        isFilterResultEmptyVisibilityObserver.onChanged(GONE)
        addCounterVisibilityObserver.onChanged(VISIBLE)
        counterListVisibilityObserver.onChanged(VISIBLE)
        loadingVisibilityOb.onChanged(GONE)
        noDataVisibilityObserver.onChanged(GONE)
        internetErrorVisibilityObserver.onChanged(GONE)
        selectedItemBarVisibilityObserver.onChanged(GONE)
        searchBarVisibilityObserver.onChanged(VISIBLE)
    }

    companion object {
        val counterWithZero = Counter("1", "nap", 0)
    }
}