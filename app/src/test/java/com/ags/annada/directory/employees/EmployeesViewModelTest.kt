package com.ags.annada.directory.employees

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.ags.annada.directory.datasource.EmployeesRepository
import com.ags.annada.directory.datasource.room.entities.Employee
import com.ags.annada.directory.utils.MainCoroutineRule
import com.google.common.truth.Truth
import com.ags.annada.directory.utils.Result
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class EmployeesViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var sut: EmployeesViewModel

    @RelaxedMockK
    lateinit var mockRepository: EmployeesRepository

    @MockK
    lateinit var mockObserver: Observer<List<Employee>>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = EmployeesViewModel(mockRepository, SavedStateHandle())
    }

    @Test
    fun `given observer set for live data items, when item changes, then observer notified`() {
        //Given
        val sampleLiveData: MutableLiveData<List<Employee>> = MutableLiveData()
        sampleLiveData.observeForever(mockObserver)
        every { mockObserver.onChanged(any()) } returns Unit

        //When
        sampleLiveData.value = emptyList()

        //Then
        verify { mockObserver.onChanged(any()) }
    }

    @Test
    fun `given observer set for item, when loadEmployees called, then dataLoading toggles`() {
        //Given
        val dataLoadingObserver = mockk<Observer<Boolean>>()
        sut.dataLoading.observeForever(dataLoadingObserver)
        every { dataLoadingObserver.onChanged(any()) } returns Unit

        sut.items.observeForever(mockObserver)
        every { mockObserver.onChanged(any()) } returns Unit

        // When
        mainCoroutineRule.pauseDispatcher()
        sut.loadEmployees(true)

        //Then
        val slot = slot<Boolean>()
        verify { dataLoadingObserver.onChanged(capture(slot)) }

        Truth.assertThat(slot.captured).isTrue()

        mainCoroutineRule.resumeDispatcher()

        verify { dataLoadingObserver.onChanged(capture(slot)) }
        Truth.assertThat(slot.captured).isFalse()
    }

    @Test
    fun `given error state, when loadEmployees called, then update live data for error status`() {
        // Given
        val isDataLoadingErrorObserver = mockk<Observer<Boolean>>()
        sut.isDataLoadingError.observeForever(isDataLoadingErrorObserver)
        every { isDataLoadingErrorObserver.onChanged(any()) } returns Unit

        sut.items.observeForever(mockObserver)
        every { mockObserver.onChanged(any()) } returns Unit

        val message = "This is an error!"
        val resource = Result.Error(Exception(message))
        every { mockRepository.item } returns MutableLiveData(resource)

        // When
        sut.loadEmployees(true)

        // Then
        val slot = slot<Boolean>()
        verify { isDataLoadingErrorObserver.onChanged(capture(slot)) }

        Truth.assertThat(slot.captured).isTrue()
    }
}