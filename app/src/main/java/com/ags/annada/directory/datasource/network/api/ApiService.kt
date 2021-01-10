package com.ags.annada.directory.datasource.network.api

import com.ags.annada.directory.datasource.room.entities.Employee
import com.ags.annada.directory.datasource.room.entities.OfficeRoom
import retrofit2.http.GET

interface ApiService {
    @GET("people")
    suspend fun getEmployees(): List<Employee>

    @GET("rooms")
    suspend fun getOfficeRooms(): List<OfficeRoom>
}