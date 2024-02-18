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
import java.util.UUID
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

    private val _noteUpdateEvent = MutableLiveData<Event<Unit>>()
    val noteUpdateEvent: LiveData<Event<Unit>> = _noteUpdateEvent

    private val _snackBarEvent = MutableLiveData<Event<Int>>()
    val snackBarEvent: LiveData<Event<Int>> = _snackBarEvent

    private var isNewNote: Boolean = false

    private var noteId: String? = null

    fun checkIsNewNoteOrExistingNote(noteId: String?) {

        this.noteId = noteId

        if (noteId == null) {
            isNewNote = true
            return
        }

        isNewNote = false

        viewModelScope.launch {

            val note = noteRepository.getNoteById(noteId).stateIn(viewModelScope)
            title.value = note.value.title
            description.value = note.value.description

        }

    }


    fun saveNote() {
        val currentTitle = title.value  // from ui
        val currentDescription = description.value

        if (isNewNote) {

            viewModelScope.launch {
                _noteUpdateEvent.value =
                    Event(Unit)

                if (currentTitle.isBlank() && currentDescription.isBlank()) {
                    _snackBarEvent.value = Event(R.string.empty_note_message)
                } else {
                    val note = Note(title = title.value, description = description.value)
                    createNote(note)
                }

            }
        } else {
            viewModelScope.launch {

                _noteUpdateEvent.value =
                    Event(Unit)
                if (currentTitle.isBlank() && currentDescription.isBlank()) {
                    _snackBarEvent.value =
                        Event(R.string.empty_note_message)
                } else {
                    updateNote()
                }
            }

        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            noteId?.let {
                noteRepository.deleteNoteById(it)
                _noteDeletedEvent.value = Event(R.string.note_deleted)
            }
        }
    }

    fun createCopyNote(noteId: String?) {
        viewModelScope.launch {

            viewModelScope.launch {
                if (noteId != null) {
                    val note = noteRepository.getNoteByIdd(noteId)
                    val n = Note(id = UUID.randomUUID().toString(), note.title, note.description)
                    noteRepository.insertNote(note = n)
                }
            }

        }

    }

    private suspend fun createNote(note: Note) {
        noteRepository.insertNote(note)
    }

    private suspend fun updateNote() {
        noteRepository.upsertNote(
            Note(noteId!!, title.value, description.value)
        )
    }


}