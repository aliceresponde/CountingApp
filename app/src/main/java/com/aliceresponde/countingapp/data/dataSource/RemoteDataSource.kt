package com.aliceresponde.countingapp.data.dataSource

import com.aliceresponde.countingapp.data.remote.CounterResponse
import com.aliceresponde.countingapp.repository.DataState

interface RemoteDataSource {
    suspend fun getAllCounters(): DataState<List<CounterResponse>>
    suspend fun increaseCounter(id: String): DataState<List<CounterResponse>>
    suspend fun decreaseCounter(id: String): DataState<List<CounterResponse>>
    suspend fun deleteCounter(id: String): DataState<List<CounterResponse>>
    suspend fun createCounter(title: String): DataState<List<CounterResponse>>
}
