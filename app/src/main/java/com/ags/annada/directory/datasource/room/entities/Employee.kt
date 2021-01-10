package com.ags.annada.directory.datasource.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employee_table")
data class Employee(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "avatar") val avatar: String,
    @ColumnInfo(name = "createdAt") val createdAt: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "favouriteColor") val favouriteColor: String,
    @ColumnInfo(name = "firstName") val firstName: String,
    @ColumnInfo(name = "lastName") val lastName: String,
    @ColumnInfo(name = "jobTitle") val jobTitle: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "phone") val phone: String
)