package com.aliceresponde.countingapp.repository

import com.aliceresponde.countingapp.data.dataSource.LocalDataSource
import com.aliceresponde.countingapp.data.dataSource.RemoteDataSource
import com.aliceresponde.countingapp.data.local.CounterEntity
import com.aliceresponde.countingapp.data.remote.NoInternetException

class CounterRepositoryImp(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : CounterRepository {
    override suspend fun getAllCounters(): DataState<List<CounterEntity>> {
        try {
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
        } catch (e: NoInternetException) {
            return localDataSource.getAllCounters()
        }
    }

    override suspend fun createCounter(tile: String): DataState<List<CounterEntity>> {
        return SuccessState(listOf())
    }

    override suspend fun increaseCounter(id: String): DataState<List<CounterEntity>> {
        return when (val remote = remoteDataSource.increaseCounter(id)) {
            is SuccessState -> {
                val data = remote.data ?: listOf()
                localDataSource.saveAllCounters(data.map {
                    CounterEntity(it.id, it.title, it.count)
                })
                localDataSource.getAllCounters()
            }
            is ErrorState -> ErrorState(remote.message ?: "")
        }
    }

    override suspend fun decreaseCounter(id: String): DataState<List<CounterEntity>> {
        return SuccessState(listOf())
    }

    override suspend fun deleteCounters(ids: List<String>): DataState<List<CounterEntity>> {
        return when (val remote = ids.map { remoteDataSource.deleteCounter(it) }.last()) {
            is SuccessState -> {
                return localDataSource.deleteCounters(ids)
            }
            is ErrorState -> ErrorState(remote.message ?: "")
        }
    }

}