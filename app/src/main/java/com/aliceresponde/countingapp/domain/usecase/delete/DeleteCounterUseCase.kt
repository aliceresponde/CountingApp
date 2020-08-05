package com.aliceresponde.countingapp.domain.usecase.delete

import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.domain.model.UiState

interface DeleteCounterUseCase {
    suspend operator fun invoke(id:List<String>): UiState<List<Counter>>
}
