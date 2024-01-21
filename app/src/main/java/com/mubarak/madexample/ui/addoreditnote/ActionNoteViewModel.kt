package com.mubarak.madexample.ui.addoreditnote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mubarak.madexample.R
import com.mubarak.madexample.data.Note
import com.mubarak.madexample.data.sources.NoteRepository
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

    private val _noteUpdateEvent = MutableLiveData<Event<Unit>>()
    val noteUpdateEvent: LiveData<Event<Unit>> = _noteUpdateEvent

    private val _snackBarEvent = MutableLiveData<Event<Int>>()
    val snackBarEvent: LiveData<Event<Int>> = _snackBarEvent

    private var isNewNote: Boolean = false

    private var noteId: String? = null


    fun checkIsNewNoteOrExistingNote(noteId: String?) {

        this.noteId = noteId // for global reference

        if (noteId==null) { // that means create a new Note
            isNewNote = true
            return
        }

        isNewNote = false // this means update a existing note (UPDATE)

        /**Update the note here first you insert the text which is already present in the item*/
        viewModelScope.launch {
            val note = noteRepository.getNoteById(noteId).stateIn(viewModelScope)
            title.value = note.value.title
            description.value = note.value.description
        }

    }

    /**this function only call when click on fab in ui*/
    fun saveNote() {
        val currentTitle = title.value  // from ui
        val currentDescription = description.value

        if (isNewNote) { // that means this is for creating note (INSERT)

            Log.d("note", "Is newNote:${isNewNote.toString()}")
            viewModelScope.launch {
                _noteUpdateEvent.value = Event(Unit) // this is like a flag for navigation we observe it when click the fab

                if (currentTitle.isEmpty() && currentDescription.isEmpty()) {
                    _snackBarEvent.value = Event(R.string.empty_note_message)
                    // need to notify field are empty note can't be created
                } else {
                    val note = Note(title = title.value, description = description.value)
                    createNote(note)  // TODO not work need to check and impl sql
                }

            }
        } else { // update them. (UPDATE)
            viewModelScope.launch {

                _noteUpdateEvent.value = Event(Unit)  // listen for updated note (because clear the backstack move to home)
                if (currentTitle.isEmpty() && currentDescription.isEmpty()) {
                    _snackBarEvent.value =
                        Event(R.string.empty_note_message) // need to notify field are empty note can't be created
                } else {
                    updateNote()
                }
            }

        }
    }

    private suspend fun createNote(note: Note) { // create a new note
        noteRepository.insertNote(note)
    }

    private suspend fun updateNote() { // update a existing note
        noteRepository.upsertNote(
            Note(noteId!!,title.value,description.value)
        )
    }

}