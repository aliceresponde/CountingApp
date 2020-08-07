package com.aliceresponde.countingapp.domain.usecase.create

import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.domain.model.ErrorViewState
import com.aliceresponde.countingapp.domain.model.SuccessViewState
import com.aliceresponde.countingapp.domain.model.UiState
import com.aliceresponde.countingapp.repository.CounterRepository
import com.aliceresponde.countingapp.repository.ErrorState
import com.aliceresponde.countingapp.repository.SuccessState

class CreateCounterUseCaseImp(private val repository: CounterRepository) : CreateCounterUseCase {
    override suspend fun invoke(tile: String): UiState<Counter> {
        return when (val result = repository.createCounter(tile)) {
            is SuccessState -> {
                var data = result.data!!
                SuccessViewState(Counter(data.id, data.title, data.count))
            }
            is ErrorState -> ErrorViewState(result.message ?: "")
        }
    }
}