package com.aliceresponde.countingapp.domain.usecase.increase

import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.domain.model.UiState
import com.aliceresponde.countingapp.repository.CounterRepository

class IncreaseCounterUseCaseImp(private val repository: CounterRepository) :
    IncreaseCounterUseCase {
    override suspend fun invoke(id: String): UiState<List<Counter>> {
        TODO("Not yet implemented")
    }
}