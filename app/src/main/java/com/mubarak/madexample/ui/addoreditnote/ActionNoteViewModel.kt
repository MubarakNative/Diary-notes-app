package com.mubarak.madexample.ui.addoreditnote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mubarak.madexample.R
import com.mubarak.madexample.data.repository.NoteRepository
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActionNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    // Two-way data-binding
    val title: MutableStateFlow<String> = MutableStateFlow("")
    val description: MutableStateFlow<String> = MutableStateFlow("")

    private val _noteDeletedEvent = MutableLiveData<Event<Int>>()
    val noteDeletedEvent: LiveData<Event<Int>> = _noteDeletedEvent

    private val _snackBarEvent = MutableLiveData<Event<Int>>()
    val snackBarEvent: LiveData<Event<Int>> = _snackBarEvent

    private val _backPressEvent = MutableLiveData<Event<Unit>>()
    val backPressEvent: LiveData<Event<Unit>> = _backPressEvent

    private var isNewNote: Boolean = false

    private var noteId: Long = -1L

    fun checkIsNewNoteOrExistingNote(noteId: Long) {

        this.noteId = noteId

        if (noteId == -1L) { // -1 means this is new note
            isNewNote = true
            return
        }

        isNewNote = false // else update the existing note

        viewModelScope.launch {

            val note = noteRepository.getNoteStreamById(noteId).stateIn(viewModelScope)
            // Filled the both Title & Description edittext with that value does it previously have
            title.value = note.value.title
            description.value = note.value.description

        }

    }


    fun saveAndExit() {

        val currentTitle = title.value
        val currentDescription = description.value

        if (isNewNote) {

            viewModelScope.launch {
                if (currentTitle.isBlank() && currentDescription.isBlank()) {
                    _snackBarEvent.value = Event(R.string.empty_note_message)
                } else {
                    val note = Note(id =0L, title = title.value, description = description.value)
                    createNote(note)
                }

            }
        } else {
            viewModelScope.launch {
                if (currentTitle.isBlank() && currentDescription.isBlank()) {
                    _snackBarEvent.value =
                        Event(R.string.empty_note_message)
                } else {
                    updateNote()
                }
            }

        }
        _backPressEvent.value = Event(Unit)

    }

    fun deleteNote() {
        viewModelScope.launch {
            noteId.let {
                noteRepository.deleteNoteById(it)
                _noteDeletedEvent.value = Event(R.string.note_deleted)
                _backPressEvent.value = Event(Unit)
            }
        }
    }

    fun createCopyNote(noteId: Long) {
        viewModelScope.launch {

            viewModelScope.launch {
                if (noteId != -1L) {
                    val note = noteRepository.getNoteById(noteId)
                    val n = Note(id = 0, note.title, note.description)
                    noteRepository.insertNote(note = n)
                }
            }

        }

    }

    private suspend fun createNote(note: Note) {
        noteRepository.insertNote(note)
    }

    private suspend fun updateNote() { // update the existing note
        noteRepository.upsertNote(
            Note(noteId, title.value, description.value)
        )
    }


}