package com.ags.annada.directory.datasource

import androidx.lifecycle.LiveData
import com.ags.annada.directory.datasource.network.api.ApiService
import com.ags.annada.directory.datasource.room.DirectoryDatabase
import com.ags.annada.directory.datasource.room.daos.EmployeeDao
import com.ags.annada.directory.datasource.room.entities.Employee
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class EmployeesRepositoryTest {
    lateinit var sut: EmployeesRepository

    @MockK
    lateinit var mockDatabase: DirectoryDatabase

    @MockK
    lateinit var mockApiService: ApiService

    @RelaxedMockK
    lateinit var mockDao: EmployeeDao

    @MockK
    lateinit var mockLiveSingleItem: LiveData<Employee>

    lateinit var mainDispatcher: CoroutineDispatcher

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mainDispatcher = Dispatchers.Main
        every { mockDatabase.employeeDao() } returns mockDao
        sut = EmployeesRepository(mockDatabase, mockApiService, mainDispatcher)
    }

    @Test
    fun `given single item returns, when getEmployeeWithId called, then dao getEmployeeWithId called`() {
        //given
        every { mockDao.getEmployeeWithId("1") } returns mockLiveSingleItem

        //when
        sut.getEmployeeWithId("1")

        //then
        verify { mockDao.getEmployeeWithId("1") }
    }
}