package com.aliceresponde.countingapp.data.remote

import com.google.gson.annotations.SerializedName

data class CreateCounterBody(@SerializedName("title") val title: String)
