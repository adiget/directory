package com.ags.annada.directory.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ags.annada.directory.datasource.network.api.ApiService
import com.ags.annada.directory.datasource.room.DirectoryDatabase
import com.ags.annada.directory.datasource.room.entities.Employee
import com.ags.annada.directory.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmployeesRepository @Inject constructor(
    private val database: DirectoryDatabase,
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher
) {
    private val _items = database.employeeDao().observeEmployees()
    val item: LiveData<Result<List<Employee>>> = Transformations.map(_items) {
        Result.Success(it)
    }

    fun getEmployeeWithId(empId: String): LiveData<Employee> {
        return database.employeeDao().getEmployeeWithId(empId)
    }

    suspend fun refreshEmployees() {
        updateEmployeesFromRemoteDataSource()
    }

    private suspend fun updateEmployeesFromRemoteDataSource() {
        withContext(ioDispatcher) {
            val employees = apiService.getEmployees()
            database.employeeDao().insertAll(employee = employees.toTypedArray())
        }
    }
}