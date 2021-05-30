package com.aliceresponde.countingapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {
    @Query("SELECT * from counter_table")
    fun getAllCounters(): List<CounterEntity>

    @Query("SELECT * from counter_table WHERE id =:id")
    fun getCounterBy(id: String): CounterEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(counters: List<CounterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(counter: CounterEntity)

    @Query("DELETE FROM counter_table")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(counter: CounterEntity)

    @Update
    suspend fun updateCounter(counter: CounterEntity)

    @Transaction
    suspend fun restoreData(counters: List<CounterEntity>): List<CounterEntity> {
        deleteAll()
        insertAll(counters)
        return getAllCounters()
    }

    @Query("SELECT * from counter_table WHERE title =:query")
    fun filterByName(query: String): Flow<List<CounterEntity>>
}