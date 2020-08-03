package com.aliceresponde.countingapp.data.remote

import com.aliceresponde.countingapp.data.dataSource.RemoteDataSource
import com.aliceresponde.countingapp.repository.DataState
import com.aliceresponde.countingapp.repository.SuccessState
import javax.inject.Inject

class RetrofitDataSource @Inject constructor(private val service: CornerApiService) :
    RemoteDataSource {
    override suspend fun getAllCounters(): DataState<List<CounterResponse>> {
        val response = service.getCounters()
        return SuccessState(response)
    }

    override suspend fun increaseCounter(id: String): DataState<List<CounterResponse>> {
        val response = service.incrementCounter(ModifyCounterBody(id))
        return SuccessState(response)
    }

    override suspend fun decreaseCounter(id: String): DataState<List<CounterResponse>> {
        val response = service.decrementCounter(ModifyCounterBody(id))
        return SuccessState(response)
    }

    override suspend fun deleteCounter(id: String): DataState<List<CounterResponse>> {
        val response = service.deleteCounter(ModifyCounterBody(id))
        return SuccessState(response)
    }

    override suspend fun createCounter(title: String): DataState<List<CounterResponse>> {
        val response = service.createCounter(CreateCounterBody(title))
        return SuccessState(response)
    }
}