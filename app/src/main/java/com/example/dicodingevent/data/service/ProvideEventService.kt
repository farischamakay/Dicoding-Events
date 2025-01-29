package com.example.dicodingevent.data.service

import com.example.dicodingevent.data.response.DetailEventResponse
import com.example.dicodingevent.data.response.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProvideEventService {
    @GET("events")
    suspend fun getActiveEvents(
        @Query("active") active: Int = 1
    ): EventResponse

    @GET("events")
    suspend fun getFinishedEvents(
        @Query("active") active: Int = 0
    ): EventResponse

    @GET("events")
    suspend fun searchEvents(
        @Query("active") active: Int = -1,
        @Query("q") query: String
    ): EventResponse

    @GET("events/{id}")
    suspend fun getDetailEvents(
        @Path("id") id: Int
    ): DetailEventResponse

}