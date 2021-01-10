package com.ags.annada.directory.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ags.annada.directory.datasource.network.api.ApiService
import com.ags.annada.directory.datasource.room.DirectoryDatabase
import com.ags.annada.directory.datasource.room.entities.OfficeRoom
import com.ags.annada.directory.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfficeRoomsRepository @Inject constructor(
    private val database: DirectoryDatabase,
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher
) {
    private val _items = database.officeRoomDao().observeOfficeRooms()
    val item: LiveData<Result<List<OfficeRoom>>> = Transformations.map(_items) {
        Result.Success(it)
    }

    fun getOfficeRoomWithId(roomId: String): LiveData<OfficeRoom> {
        return database.officeRoomDao().getOfficeRoomWithId(roomId)
    }

    suspend fun refreshOfficeRooms() {
        updateOfficeRoomsFromRemoteDataSource()
    }

    private suspend fun updateOfficeRoomsFromRemoteDataSource() {
        withContext(ioDispatcher) {
            val officeRooms = apiService.getOfficeRooms()
            database.officeRoomDao().insertAll(officeRoom = officeRooms.toTypedArray())
        }
    }
}