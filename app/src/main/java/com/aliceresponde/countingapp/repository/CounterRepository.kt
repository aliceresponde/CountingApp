package com.aliceresponde.countingapp.repository

import com.aliceresponde.countingapp.data.local.CounterEntity

interface CounterRepository {
    suspend fun createCounter(tile: String): DataState<List<CounterEntity>>
    suspend fun getAllCounters(): DataState<List<CounterEntity>>
    suspend fun increaseCounter(id: String): DataState<List<CounterEntity>>
    suspend fun decreaseCounter(id: String): DataState<List<CounterEntity>>
    suspend fun deleteCounter(id: String): DataState<List<CounterEntity>>
}