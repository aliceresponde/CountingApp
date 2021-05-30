package com.aliceresponde.countingapp.domain.usecase.filter

import com.aliceresponde.countingapp.domain.model.Counter
import kotlinx.coroutines.flow.Flow

interface FilterDataUseCase {
     operator fun invoke(query: String): Flow<List<Counter>>
}