package com.mubarak.madexample.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mubarak.madexample.R
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.data.repository.NoteRepository
import com.mubarak.madexample.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {


    private val _getNoteIdEvent: MutableLiveData<Event<String>> = MutableLiveData()
    val getNoteIdEvent: LiveData<Event<String>> = _getNoteIdEvent

    val getAllNote = noteRepository.getAllNote()

    val isEmpty: StateFlow<Boolean> = getAllNote.map { it.isEmpty() }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        true
    )

    private val _noteDeletedEvent = MutableLiveData<Event<Int>>()
    val noteDeletedEvent: LiveData<Event<Int>> = _noteDeletedEvent


    fun getNoteId(noteId: String) {
        _getNoteIdEvent.value = Event(noteId)
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteRepository.deleteNote(note)
            _noteDeletedEvent.value = Event(R.string.note_deleted)
        }
    }

    fun undoDeletedNote(note: Note) {

        viewModelScope.launch {
            noteRepository.insertNote(note)
        }
    }
}