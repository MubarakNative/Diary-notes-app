package com.mubarak.madexample.presenter.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mubarak.madexample.R
import com.mubarak.madexample.data.Note
import com.mubarak.madexample.data.repository.NoteRepository
import com.mubarak.madexample.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {



    val getAllNote = noteRepository.getAllNote().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )



    private val _noteDeletedEvent = MutableLiveData<Event<Int>>()
    val noteDeletedEvent :LiveData<Event<Int>> = _noteDeletedEvent






    fun deleteNote(note:Note){
        viewModelScope.launch {
            noteRepository.deleteNote(note)
            _noteDeletedEvent.value = Event(R.string.note_deleted)
        }
    }

    fun undoDeletedNote(note:Note){ // insert note for undo deletion
        viewModelScope.launch {
            noteRepository.insertNote(note)
        }
    }
}