package com.aliceresponde.countingapp.domain.usecase.getcounters

import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.domain.model.UiState

interface GetCountersUseCase {
    suspend operator fun invoke() : UiState<List<Counter>>
}
