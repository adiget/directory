package com.ags.annada.directory.employees.details

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ags.annada.directory.datasource.EmployeesRepository
import com.ags.annada.directory.datasource.room.entities.Employee
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EmployeeDetailsViewModel @ViewModelInject constructor(
    private val repository: EmployeesRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val item: LiveData<Employee> =
        repository.getEmployeeWithId(savedStateHandle.get<String>("empId") ?: "")

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        loadEmployeeDetails()
    }

    private fun loadEmployeeDetails() {
        uiScope.launch {
            repository.getEmployeeWithId(savedStateHandle.get<String>("empId") ?: "")
        }
    }

    override fun onCleared() {
        super.onCleared()

        viewModelJob.cancel()
    }
}