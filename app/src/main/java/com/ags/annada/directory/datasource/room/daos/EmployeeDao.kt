package com.ags.annada.directory.datasource.room.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ags.annada.directory.datasource.room.entities.Employee

@Dao
interface EmployeeDao {

    @get:Query("SELECT * from employee_table")
    val all: LiveData<List<Employee>>

    @Query("SELECT * FROM employee_table")
    fun observeEmployees(): LiveData<List<Employee>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employee: Employee)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg employee: Employee)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmployees(vararg employee: Employee)

    @Query("SELECT * from employee_table WHERE id = :key")
    fun getEmployeeWithId(key: String): LiveData<Employee>

    @Query("DELETE from employee_table")
    suspend fun deleteAll()
}