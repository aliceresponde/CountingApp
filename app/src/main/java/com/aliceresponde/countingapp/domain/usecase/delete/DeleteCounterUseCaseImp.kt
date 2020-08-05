package com.aliceresponde.countingapp.domain.usecase.delete

import com.aliceresponde.countingapp.data.remote.NoInternetException
import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.domain.model.ErrorViewState
import com.aliceresponde.countingapp.domain.model.SuccessViewState
import com.aliceresponde.countingapp.domain.model.UiState
import com.aliceresponde.countingapp.repository.CounterRepository
import com.aliceresponde.countingapp.repository.ErrorState
import com.aliceresponde.countingapp.repository.SuccessState

class DeleteCounterUseCaseImp(private val repository: CounterRepository) : DeleteCounterUseCase {
    override suspend fun invoke(ids: List<String>): UiState<List<Counter>> {
        return try {
                when (val result = repository.deleteCounters(ids)) {
                    is SuccessState -> {
                        val data = result.data ?: listOf()
                        SuccessViewState(data.map { Counter(it.id, it.title, it.count) })
                    }
                    is ErrorState -> {
                        ErrorViewState(result.message ?: "")
                    }
                }
        }catch (e: NoInternetException){
            ErrorViewState(e.message ?: "")
        }
    }
}
