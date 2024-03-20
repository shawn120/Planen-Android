package com.example.planmanager.data

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HolidayRepository (
    private val service: HolidayService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loadHoliday(
        year:String,
        countryCode: String
    ) : Result<List<HolidayItem>?>{
        return withContext(ioDispatcher) {
            try {
                val response = service.getHolidays(year, countryCode)
                if (response.isSuccessful) {
                    Log.d("APIAPIAPI", "${response.body()}")
                    Result.success(response.body())
                } else {
                    Log.d("APIAPIAPI", "false")
                    Result.failure(Exception(response.errorBody()?.string()))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}