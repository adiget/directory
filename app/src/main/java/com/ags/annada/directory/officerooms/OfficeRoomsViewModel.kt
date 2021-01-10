package com.ags.annada.directory.officerooms

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.ags.annada.directory.R
import com.ags.annada.directory.datasource.OfficeRoomsRepository
import com.ags.annada.directory.datasource.room.entities.Employee
import com.ags.annada.directory.datasource.room.entities.OfficeRoom
import com.ags.annada.directory.utils.Event
import com.ags.annada.directory.utils.Result
import kotlinx.coroutines.launch
import java.util.*

class OfficeRoomsViewModel @ViewModelInject constructor(
    private val repository: OfficeRoomsRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectItemEvent = MutableLiveData<Event<String>>()
    val selectItemEvent: LiveData<Event<String>> = _selectItemEvent

    private val _searchString: MutableLiveData<String> = MutableLiveData("")

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _items: LiveData<List<OfficeRoom>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate) {
            _dataLoading.value = true
            viewModelScope.launch {
                repository.refreshOfficeRooms()
                _dataLoading.value = false
            }
        }

        repository.item.distinctUntilChanged().switchMap { filterOfficeRooms(it) }
    }

    val items: LiveData<List<OfficeRoom>> = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _currentFilteringLabel = MutableLiveData<String>()
    val currentFilteringLabel: LiveData<String> = _currentFilteringLabel

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    val isDataLoadingError = MutableLiveData<Boolean>()

    init {
        loadOfficeRooms(true)
    }

    fun onClickItem(item: OfficeRoom) {
        _selectItemEvent.value = Event(item.id)
    }

    fun loadOfficeRooms(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    private fun filterOfficeRooms(officeRoomsResult: Result<List<OfficeRoom>>): LiveData<List<OfficeRoom>> {
        val result = MutableLiveData<List<OfficeRoom>>()

        if (officeRoomsResult is Result.Success) {
            isDataLoadingError.value = false
            viewModelScope.launch {
                result.value = filterItems(officeRoomsResult.data)
            }
        } else {
            result.value = emptyList()
            showSnackbarMessage(R.string.loading_office_rooms_error)
            isDataLoadingError.value = true
        }

        return result
    }

    private fun filterItems(officeRooms: List<OfficeRoom>): List<OfficeRoom> {
        val filteredItems: List<OfficeRoom>

        val search = _searchString.value.toString()

        filteredItems = officeRooms.filter { officeRoom ->
            search.isEmpty() || officeRoom.name.toLowerCase(Locale.ROOT)
                .startsWith(search.toLowerCase(Locale.ROOT))
        }

        return filteredItems
    }

    fun setSearchString(search: String) {
        _searchString.value = search

        loadOfficeRooms(false)
    }
}