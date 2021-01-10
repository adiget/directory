package com.ags.annada.directory.datasource

import androidx.lifecycle.LiveData
import com.ags.annada.directory.datasource.network.api.ApiService
import com.ags.annada.directory.datasource.room.DirectoryDatabase
import com.ags.annada.directory.datasource.room.daos.OfficeRoomDao
import com.ags.annada.directory.datasource.room.entities.OfficeRoom
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class OfficeRoomsRepositoryTest {
    lateinit var sut: OfficeRoomsRepository

    @MockK
    lateinit var mockDatabase: DirectoryDatabase

    @MockK
    lateinit var mockApiService: ApiService

    @RelaxedMockK
    lateinit var mockDao: OfficeRoomDao

    @MockK
    lateinit var mockLiveSingleItem: LiveData<OfficeRoom>

    lateinit var mainDispatcher: CoroutineDispatcher

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mainDispatcher = Dispatchers.Main
        every { mockDatabase.officeRoomDao() } returns mockDao
        sut = OfficeRoomsRepository(mockDatabase, mockApiService, mainDispatcher)
    }

    @Test
    fun `given single item returns, when getOfficeRoomWithId called, then dao getOfficeRoomWithId called`() {
        //given
        every { mockDao.getOfficeRoomWithId("1") } returns mockLiveSingleItem

        //when
        sut.getOfficeRoomWithId("1")

        //then
        verify { mockDao.getOfficeRoomWithId("1") }
    }
}