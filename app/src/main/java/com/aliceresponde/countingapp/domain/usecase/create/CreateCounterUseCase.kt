package com.aliceresponde.countingapp.domain.usecase.create

import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.domain.model.UiState

interface CreateCounterUseCase {
    suspend operator fun invoke(counter: Counter): UiState<Any>
}
