package com.mubarak.madexample.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mubarak.madexample.R
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.data.repository.NoteRepository
import com.mubarak.madexample.data.sources.datastore.TodoPreferenceDataStore
import com.mubarak.madexample.data.sources.local.model.NoteLayout
import com.mubarak.madexample.data.sources.local.model.TodoTheme
import com.mubarak.madexample.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val todoPreferenceDataStore: TodoPreferenceDataStore
) : ViewModel() {


    private val _getNoteIdEvent: MutableLiveData<Event<Long>> = MutableLiveData()
    val getNoteIdEvent: LiveData<Event<Long>> = _getNoteIdEvent

    val getAllNote = noteRepository.getAllNote()

    private val _noteItemLayout: MutableLiveData<Int> = MutableLiveData()
    val  noteItemLayout: LiveData<Int> = _noteItemLayout

    val isEmpty: StateFlow<Boolean> = getAllNote.map { it.isEmpty() }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        true
    )

    private val _noteDeletedEvent = MutableLiveData<Event<Int>>()
    val noteDeletedEvent: LiveData<Event<Int>> = _noteDeletedEvent

    init {
        viewModelScope.launch{
            _noteItemLayout.value =todoPreferenceDataStore.getNoteLayout.first()}
    }

    fun toggleNoteLayout(){

        viewModelScope.launch {
            val layout = when(_noteItemLayout.value){
                NoteLayout.LIST.ordinal -> NoteLayout.GRID.ordinal
                NoteLayout.GRID.ordinal -> NoteLayout.LIST.ordinal
                else -> {return@launch}
            }

            _noteItemLayout.value = layout
            todoPreferenceDataStore.setNoteLayout(layout)
        }


    }

    fun getNoteId(noteId: Long) {
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