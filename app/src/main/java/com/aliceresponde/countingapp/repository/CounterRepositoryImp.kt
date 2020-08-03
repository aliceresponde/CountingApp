package com.aliceresponde.countingapp.repository

import com.aliceresponde.countingapp.data.dataSource.LocalDataSource
import com.aliceresponde.countingapp.data.dataSource.RemoteDataSource
import com.aliceresponde.countingapp.data.local.CounterEntity

class CounterRepositoryImp (
    private val  localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
                        ) : CounterRepository{
    override suspend fun createCounter(tile: String): DataState<List<CounterEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCounters(): DataState<List<CounterEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun increaseCounter(id: String): DataState<List<CounterEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun decreaseCounter(id: String): DataState<List<CounterEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCounter(id: String): DataState<List<CounterEntity>> {
        TODO("Not yet implemented")
    }

}