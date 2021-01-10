package com.ags.annada.directory.datasource.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "office_rooms_table")
data class OfficeRoom(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "created_at") val created_at: String,
    @ColumnInfo(name = "is_occupied") val is_occupied: Boolean,
    @ColumnInfo(name = "max_occupancy") val max_occupancy: Int,
    @ColumnInfo(name = "name") val name: String
)