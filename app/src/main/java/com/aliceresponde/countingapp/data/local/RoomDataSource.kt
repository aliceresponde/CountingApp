package com.aliceresponde.countingapp.data.local

import com.aliceresponde.countingapp.data.dataSource.LocalDataSource
import com.aliceresponde.countingapp.repository.DataState
import com.aliceresponde.countingapp.repository.SuccessState

class RoomDataSource(private val db: AppDatabase) : LocalDataSource {
    private val dao: CounterDao = db.counterDao()
    override suspend fun createCounter(
        id: String,
        title: String,
        count: Int
    ): CounterEntity {
        dao.insert(CounterEntity(id, title, count))
        return dao.getCounterBy(id)
    }


    override suspend fun getAllCounters(): DataState<List<CounterEntity>> {
        val data = dao.getAllCounters()
        return SuccessState(data)
    }

    override suspend fun saveAllCounters(counters: List<CounterEntity>): DataState<List<CounterEntity>> {
        dao.deleteAll()
        dao.insertAll(counters)
        val data = dao.getAllCounters()
        return SuccessState(data)
    }

    override suspend fun increaseCounter(id: String): DataState<List<CounterEntity>> {
        val counter = dao.getCounterBy(id)
        dao.updateCounter(counter.copy(count = counter.count + 1))
        val data = dao.getAllCounters()
        return SuccessState(data)
    }

    override suspend fun decreaseCounter(id: String): DataState<List<CounterEntity>> {
        val counter = dao.getCounterBy(id)
        dao.updateCounter(counter.copy(count = counter.count - 1))
        val data = dao.getAllCounters()
        return SuccessState(data)
    }

    override suspend fun deleteCounters(counterIdLis: List<String>): DataState<List<CounterEntity>> {
        counterIdLis.forEach { id -> deleteCounter(id) }
        return SuccessState(dao.getAllCounters())
    }

    override suspend fun deleteAllCounters(): DataState<List<CounterEntity>> {
        dao.deleteAll()
        val data = dao.getAllCounters()
        return SuccessState(data)
    }

    private suspend fun deleteCounter(id: String): DataState<List<CounterEntity>> {
        val counter = dao.getCounterBy(id)
        dao.delete(counter)
        val data = dao.getAllCounters()
        return SuccessState(data)
    }

}
