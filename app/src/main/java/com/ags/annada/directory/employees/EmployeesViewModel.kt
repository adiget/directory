package com.ags.annada.directory.employees

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.ags.annada.directory.R
import com.ags.annada.directory.datasource.EmployeesRepository
import com.ags.annada.directory.datasource.room.entities.Employee
import com.ags.annada.directory.utils.Event
import com.ags.annada.directory.utils.Result
import kotlinx.coroutines.launch
import java.util.*

class EmployeesViewModel @ViewModelInject constructor(
    private val repository: EmployeesRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectItemEvent = MutableLiveData<Event<String>>()
    val selectItemEvent: LiveData<Event<String>> = _selectItemEvent

    private val _searchString: MutableLiveData<String> = MutableLiveData("")

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _items: LiveData<List<Employee>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate) {
            _dataLoading.value = true
            viewModelScope.launch {
                repository.refreshEmployees()
                _dataLoading.value = false
            }
        }

        repository.item.distinctUntilChanged().switchMap { filterEmployees(it) }
    }

    val items: LiveData<List<Employee>> = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _currentFilteringLabel = MutableLiveData<String>()
    val currentFilteringLabel: LiveData<String> = _currentFilteringLabel

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    val isDataLoadingError = MutableLiveData<Boolean>()

    init {
        loadEmployees(true)
    }

    fun onClickItem(item: Employee) {
        _selectItemEvent.value = Event(item.id)
    }

    fun loadEmployees(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    private fun filterEmployees(employeesResult: Result<List<Employee>>): LiveData<List<Employee>> {
        val result = MutableLiveData<List<Employee>>()

        if (employeesResult is Result.Success) {
            isDataLoadingError.value = false
            viewModelScope.launch {
                result.value = filterItems(employeesResult.data)
            }
        } else {
            result.value = emptyList()
            showSnackbarMessage(R.string.loading_employees_error)
            isDataLoadingError.value = true
        }

        return result
    }

    private fun filterItems(employees: List<Employee>): List<Employee> {
        val filteredItems: List<Employee>

        val search = _searchString.value.toString()

        filteredItems = employees.filter { employee ->
            search.isEmpty() || employee.firstName.toLowerCase(Locale.ROOT)
                .startsWith(search.toLowerCase(Locale.ROOT))
        }

        return filteredItems
    }

    fun setSearchString(search: String) {
        _searchString.value = search

        loadEmployees(false)
    }
}