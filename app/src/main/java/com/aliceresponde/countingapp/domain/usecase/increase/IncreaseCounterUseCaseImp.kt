package com.aliceresponde.countingapp.domain.usecase.increase

import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.domain.model.ErrorViewState
import com.aliceresponde.countingapp.domain.model.SuccessViewState
import com.aliceresponde.countingapp.domain.model.UiState
import com.aliceresponde.countingapp.repository.CounterRepository
import com.aliceresponde.countingapp.repository.ErrorState
import com.aliceresponde.countingapp.repository.SuccessState

class IncreaseCounterUseCaseImp(private val repository: CounterRepository) :
    IncreaseCounterUseCase {
    override suspend fun invoke(id: String): UiState<List<Counter>> {
        val result = repository.increaseCounter(id)
        return when (result) {
            is SuccessState -> {
                val data = result.data ?: listOf()
                SuccessViewState(data.map { Counter(it.id, it.title, it.count) })
            }
            is ErrorState -> ErrorViewState(result.message ?: "")
        }
    }
}