package com.aliceresponde.countingapp.data.local

import androidx.room.*

@Dao
interface CounterDao {
    @Query("SELECT * from counter_table")
    fun getAllTransactions(): List<CounterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(counters: List<CounterEntity>)

    @Query("DELETE FROM counter_table")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(counter: CounterEntity)

    @Update
    suspend fun updateTransaction(counter: CounterEntity)

    @Transaction
    suspend fun restoreData(counters: List<CounterEntity>): List<CounterEntity> {
        deleteAll()
        insertAll(counters)
        return getAllTransactions()
    }
}