package com.ags.annada.directory.datasource.room.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ags.annada.directory.datasource.room.entities.OfficeRoom

@Dao
interface OfficeRoomDao {

    @get:Query("SELECT * from office_rooms_table")
    val all: LiveData<List<OfficeRoom>>

    @Query("SELECT * FROM office_rooms_table")
    fun observeOfficeRooms(): LiveData<List<OfficeRoom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(officeRoom: OfficeRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg officeRoom: OfficeRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOfficeRooms(vararg officeRoom: OfficeRoom)

    @Query("SELECT * from office_rooms_table WHERE id = :key")
    fun getOfficeRoomWithId(key: String): LiveData<OfficeRoom>

    @Query("DELETE from office_rooms_table")
    suspend fun deleteAll()
}