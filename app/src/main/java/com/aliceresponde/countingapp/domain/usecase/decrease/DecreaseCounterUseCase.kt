package com.aliceresponde.countingapp.domain.usecase.decrease

import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.domain.model.UiState

interface DecreaseCounterUseCase {
    suspend operator fun invoke( id: String) :UiState<List<Counter>>
}
