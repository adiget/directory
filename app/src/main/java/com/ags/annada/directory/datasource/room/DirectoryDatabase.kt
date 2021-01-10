package com.ags.annada.directory.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ags.annada.directory.datasource.room.daos.EmployeeDao
import com.ags.annada.directory.datasource.room.daos.OfficeRoomDao
import com.ags.annada.directory.datasource.room.entities.Employee
import com.ags.annada.directory.datasource.room.entities.OfficeRoom

@Database(entities = [Employee::class, OfficeRoom::class], version = 1)
@TypeConverters(RoomTypeConverters::class)
abstract class DirectoryDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao

    abstract fun officeRoomDao(): OfficeRoomDao
}