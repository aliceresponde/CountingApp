package com.aliceresponde.countingapp.domain.usecase.increase

import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.domain.model.UiState

interface IncreaseCounterUseCase {
    suspend operator fun invoke( id: String) :UiState<List<Counter>>
}
