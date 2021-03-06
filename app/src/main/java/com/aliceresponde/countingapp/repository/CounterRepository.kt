package com.aliceresponde.countingapp.repository

import com.aliceresponde.countingapp.data.local.CounterEntity
import kotlinx.coroutines.flow.Flow

interface CounterRepository {
    suspend fun createCounter(tile: String): DataState<CounterEntity>
    suspend fun getAllCounters(): DataState<List<CounterEntity>>
    suspend fun increaseCounter(id: String): DataState<List<CounterEntity>>
    suspend fun decreaseCounter(id: String): DataState<List<CounterEntity>>
    suspend fun deleteCounters(id: List<String>): DataState<List<CounterEntity>>
    fun filterByName(query: String): DataState<Flow<List<CounterEntity>>>
}