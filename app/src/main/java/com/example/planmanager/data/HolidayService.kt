package com.example.planmanager.data

import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HolidayService {
    @GET("v3/publicholidays/{year}/{countryCode}")
    suspend fun getHolidays(
        @Path("year") year: String,
        @Path("countryCode") countryCode: String
    ): Response<List<HolidayItem>>

    companion object {
        private const val BASE_URL = "https://date.nager.at/api/"
        fun create() : HolidayService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(HolidayService::class.java)
        }
    }
}