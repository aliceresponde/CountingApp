package com.aliceresponde.countingapp.data.remote

import com.google.gson.annotations.SerializedName

data class ModifyCounterBody(@SerializedName("id") val id: String)
