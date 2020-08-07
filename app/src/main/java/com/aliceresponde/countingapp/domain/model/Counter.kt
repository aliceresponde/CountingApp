package com.aliceresponde.countingapp.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Counter(val id: String, val title: String, val count: Int): Parcelable
