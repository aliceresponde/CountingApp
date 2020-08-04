package com.aliceresponde.countingapp.domain.model

data class Counter(val id: String, val title: String, val count: Int, val isSelected: Boolean = false)
