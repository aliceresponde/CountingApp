package com.aliceresponde.countingapp.repository

import com.aliceresponde.countingapp.data.dataSource.LocalDataSource
import com.aliceresponde.countingapp.data.dataSource.RemoteDataSource
import com.aliceresponde.countingapp.data.local.CounterEntity
import com.aliceresponde.countingapp.data.remote.NoInternetException
import kotlinx.coroutines.flow.Flow

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
        } catch (e: Throwable) {
            return localDataSource.getAllCounters()
        }
    }

    override suspend fun createCounter(tile: String): DataState<CounterEntity> {
        try {
            return when (val remote = remoteDataSource.createCounter(tile)) {
                is SuccessState -> {
                    val data = remote.data ?: listOf()
                    val newRemoteCounter = data.last()
                    val createdCounter =
                        localDataSource.createCounter(newRemoteCounter.id, newRemoteCounter.title)
                    SuccessState(createdCounter)
                }
                is ErrorState -> ErrorState(remote.message ?: "")
            }
        } catch (e: NoInternetException) {
            return ErrorState("Internet error, we did't create the counter")
        } catch (e: Throwable) {
            return ErrorState("Internet error, we did't create the counter")
        }
    }

    override suspend fun increaseCounter(id: String): DataState<List<CounterEntity>> {
        try {
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
        }  catch (e: NoInternetException) {
            return ErrorState("Internet error, we did't increase the counter")
        } catch (e: Throwable) {
            return ErrorState("Internet error, we did't increase the counter")
        }
    }

    override suspend fun decreaseCounter(id: String): DataState<List<CounterEntity>> {
        try {


            return when (val remote = remoteDataSource.decreaseCounter(id)) {
                is SuccessState -> {
                    val data = remote.data ?: listOf()
                    localDataSource.saveAllCounters(data.map {
                        CounterEntity(it.id, it.title, it.count)
                    })
                }
                is ErrorState -> ErrorState(remote.message ?: "")
            }
        } catch (e: NoInternetException) {
            return ErrorState("Internet error, we did't decrease the counter")
        } catch (e: Throwable) {
            return ErrorState("Internet error, we did't decrease the counter")
        }
    }

    override suspend fun deleteCounters(ids: List<String>): DataState<List<CounterEntity>> {
        try {
            return when (val remote = ids.map { remoteDataSource.deleteCounter(it) }.last()) {
                is SuccessState -> {
                    return localDataSource.deleteCounters(ids)
                }
                is ErrorState -> ErrorState(remote.message ?: "")
            }
        } catch (e: NoInternetException) {
            return ErrorState("Internet error, we did't delete the counter(s)")
        } catch (e: Throwable) {
            return ErrorState("Internet error, we did't delete the counter(s)")
        }
    }

    override fun filterByName(query: String): DataState<Flow<List<CounterEntity>>> {
        return localDataSource.filterByName(query)
    }

}