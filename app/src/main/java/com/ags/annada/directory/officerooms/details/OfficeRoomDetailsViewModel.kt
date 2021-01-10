package com.ags.annada.directory.officerooms.details

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ags.annada.directory.datasource.OfficeRoomsRepository
import com.ags.annada.directory.datasource.room.entities.OfficeRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OfficeRoomDetailsViewModel @ViewModelInject constructor(
    private val repository: OfficeRoomsRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val item: LiveData<OfficeRoom> =
        repository.getOfficeRoomWithId(savedStateHandle.get<String>("roomId") ?: "1")

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        loadOfficeRoomDetails()
    }

    private fun loadOfficeRoomDetails() {
        uiScope.launch {
            repository.getOfficeRoomWithId(savedStateHandle.get<String>("roomId") ?: "1")
        }
    }

    override fun onCleared() {
        super.onCleared()

        viewModelJob.cancel()
    }
}