package com.aliceresponde.countingapp.domain.usecase.filter

import com.aliceresponde.countingapp.domain.mapper.DataToDomainMapper
import com.aliceresponde.countingapp.domain.model.Counter
import com.aliceresponde.countingapp.repository.CounterRepository
import com.aliceresponde.countingapp.repository.ErrorState
import com.aliceresponde.countingapp.repository.SuccessState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class FilterDataUseCaseImp(
    private val mapper: DataToDomainMapper,
    private val repository: CounterRepository,
) : FilterDataUseCase {
    override  fun invoke(query: String): Flow<List<Counter>> {
        return when (val result = repository.filterByName(query)) {
            is SuccessState -> result.data?.let {
                it.map {
                    it.map(
                        mapper::dataToDomain
                    )
                }
            } ?: emptyFlow()

            is ErrorState -> emptyFlow()
        }
    }
}