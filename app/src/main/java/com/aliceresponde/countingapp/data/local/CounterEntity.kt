package com.aliceresponde.countingapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "counter_table")
data class CounterEntity(@PrimaryKey val id: String, val title: String, val count: Int)
