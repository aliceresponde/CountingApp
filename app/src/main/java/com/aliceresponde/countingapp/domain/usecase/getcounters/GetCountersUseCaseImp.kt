package com.aliceresponde.countingapp.domain.usecase.getcounters

import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.domain.model.UiState
import com.aliceresponde.countingapp.repository.CounterRepository

class GetCountersUseCaseImp(private val repository: CounterRepository) : GetCountersUseCase{
    override suspend fun invoke(): UiState<List<Counter>> {
        TODO("Not yet implemented")
    }
}