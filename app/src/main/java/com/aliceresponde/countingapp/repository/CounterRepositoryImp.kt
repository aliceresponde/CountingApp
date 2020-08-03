package com.aliceresponde.countingapp.repository

import com.aliceresponde.countingapp.data.dataSource.LocalDataSource
import com.aliceresponde.countingapp.data.dataSource.RemoteDataSource
import com.aliceresponde.countingapp.data.local.CounterEntity

class CounterRepositoryImp(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : CounterRepository {
    override suspend fun getAllCounters(): DataState<List<CounterEntity>> {
        return when (val remote = remoteDataSource.getAllCounters()) {
            is SuccessState -> {
                val remoteData = remote.data ?: listOf()
                val result = localDataSource.saveAllCounters(remoteData.map {
                    CounterEntity(it.id, it.title, it.count)
                })
                result
            }
            is ErrorState -> {
                ErrorState(remote.message ?: "")
            }
        }
    }

    override suspend fun createCounter(tile: String): DataState<List<CounterEntity>> {
        return SuccessState(listOf())
    }

    override suspend fun increaseCounter(id: String): DataState<List<CounterEntity>> {
        return SuccessState(listOf())
    }

    override suspend fun decreaseCounter(id: String): DataState<List<CounterEntity>> {
        return SuccessState(listOf())
    }

    override suspend fun deleteCounter(id: String): DataState<List<CounterEntity>> {
        return SuccessState(listOf())
    }

}