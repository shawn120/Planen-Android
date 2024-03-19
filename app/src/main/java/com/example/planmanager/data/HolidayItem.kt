package com.example.planmanager.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import java.util.UUID

@JsonClass(generateAdapter = true)
data class HolidayItem(
    val date: String,
    val name: String,
)