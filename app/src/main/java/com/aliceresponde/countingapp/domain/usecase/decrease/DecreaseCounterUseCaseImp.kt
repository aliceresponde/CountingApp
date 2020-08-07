package com.aliceresponde.countingapp.domain.usecase.decrease

import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.domain.model.ErrorViewState
import com.aliceresponde.countingapp.domain.model.SuccessViewState
import com.aliceresponde.countingapp.domain.model.UiState
import com.aliceresponde.countingapp.repository.CounterRepository
import com.aliceresponde.countingapp.repository.ErrorState
import com.aliceresponde.countingapp.repository.SuccessState

class DecreaseCounterUseCaseImp(private val repository: CounterRepository) :
    DecreaseCounterUseCase {
    override suspend fun invoke(id: String): UiState<List<Counter>> {
        return when (val result = repository.decreaseCounter(id)) {
            is SuccessState -> {
                val data = result.data ?: listOf()
                SuccessViewState(data.map { Counter(it.id, it.title, it.count) })
            }
            is ErrorState -> {
                ErrorViewState(result.message ?: "")
            }
        }
    }
}