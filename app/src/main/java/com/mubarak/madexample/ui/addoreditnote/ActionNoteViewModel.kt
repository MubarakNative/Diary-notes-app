package com.mubarak.madexample.ui.addoreditnote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mubarak.madexample.R
import com.mubarak.madexample.data.repository.NoteRepository
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.utils.Event
import com.mubarak.madexample.utils.NoteStatus
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

    private val _noteIdEvent = MutableLiveData<Event<Long>>()
    val noteIdEvent: LiveData<Event<Long>> = _noteIdEvent

    private val _noteStatus = MutableLiveData<Event<NoteStatus>>()
    val noteStatus: LiveData<Event<NoteStatus>> = _noteStatus

    private val _noteArchivedEvent = MutableLiveData<Event<Int>>()
    val noteArchivedEvent: LiveData<Event<Int>> = _noteArchivedEvent

    private val _noteMovedToTrashEvent = MutableLiveData<Event<Int>>()
    val noteMovedToTrashEvent: LiveData<Event<Int>> = _noteMovedToTrashEvent

    private val _noteUnArchiveEvent = MutableLiveData<Event<Int>>()
    val noteUnArchiveEvent: LiveData<Event<Int>> = _noteUnArchiveEvent

    private val _noteDeleteConfirmationEvent = MutableLiveData<Event<Unit>>()
    val noteDeleteConfirmationEvent: LiveData<Event<Unit>> = _noteDeleteConfirmationEvent

    private val _noteUnRestoreEvent = MutableLiveData<Event<Int>>()
    val noteUnRestoreEvent: LiveData<Event<Int>> = _noteUnRestoreEvent

    private val _backPressEvent = MutableLiveData<Event<Unit>>()
    val backPressEvent: LiveData<Event<Unit>> = _backPressEvent

    private var isNewNote: Boolean = false

    private var status: NoteStatus = NoteStatus.ACTIVE

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

            val noted = noteRepository.getNoteById(noteId)
            val noteStatus = noted.noteStatus
            _noteStatus.value = Event(noteStatus)
            status = noteStatus

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
                    val note = Note(id =0L, title = title.value, description = description.value,
                        noteStatus = NoteStatus.ACTIVE)
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

        when (status) {
            NoteStatus.ACTIVE -> {
                if (isNewNote) {
                    _snackBarEvent.value = Event(R.string.empty_note_message)
                    _backPressEvent.value = Event(Unit)
                } else {
                    viewModelScope.launch {
                        noteRepository.upsertNote(
                            Note(
                                noteId, title.value, description.value, NoteStatus.TRASH
                            )
                        )
                    }
                    _noteDeletedEvent.value = Event(R.string.note_moved_trash)
                    _noteIdEvent.value = Event(noteId)
                    _backPressEvent.value = Event(Unit)

                }
            }

            NoteStatus.ARCHIVE -> {
                viewModelScope.launch {
                    val noteStatus = noteRepository.getNoteById(noteId)
                    noteRepository.upsertNote(
                        Note(
                            noteStatus.id,
                            noteStatus.title,
                            noteStatus.description,
                            NoteStatus.TRASH
                        )
                    )
                }
                _noteMovedToTrashEvent.value = Event(R.string.note_moved_trash)
                _noteIdEvent.value = Event(noteId)
                _backPressEvent.value = Event(Unit)
            }

            NoteStatus.TRASH -> {
                viewModelScope.launch {
                    _noteDeleteConfirmationEvent.value = Event(Unit)
                }
            }
        }
    }
    fun deleteNotePermanently() {
        viewModelScope.launch {
            val note = noteRepository.getNoteById(noteId)
            noteRepository.deleteNote(note)
            _backPressEvent.value = Event(Unit)
        }
    }

    fun createCopyNote(noteId: Long) {

        viewModelScope.launch {

            viewModelScope.launch {
                if (!isNewNote) {
                    val getNoteById = noteRepository.getNoteById(noteId)
                    val note = Note(id = 0, getNoteById.title, getNoteById.description, getNoteById.noteStatus)
                    noteRepository.insertNote(note)
                }
            }
        }

    }

    private suspend fun createNote(note: Note) {
        noteRepository.insertNote(note)
    }

    private suspend fun updateNote() { // update the existing note

        val note = noteRepository.getNoteById(noteId)
        val noteStatus = note.noteStatus
        status = noteStatus
        _noteStatus.value = Event(noteStatus)
        noteRepository.upsertNote(
            Note(noteId, title.value, description.value, noteStatus)
        )
    }

    fun onNoteStatusChange() { // called when user click on `archive` menu in Edit Fragment

        when (status) {
            NoteStatus.ACTIVE -> {
                if (isNewNote) {
                    // new note simply pop the stack
                    _snackBarEvent.value = Event(R.string.empty_note_message)
                    _backPressEvent.value = Event(Unit)
                } else {
                    viewModelScope.launch {
                        noteRepository.upsertNote(
                            Note(
                                noteId, title.value, description.value, NoteStatus.ARCHIVE
                            )
                        )
                    }
                    _noteArchivedEvent.value = Event(R.string.note_archived)
                    _noteIdEvent.value = Event(noteId)
                    _backPressEvent.value = Event(Unit)

                }
            }

            NoteStatus.ARCHIVE -> {
                viewModelScope.launch {
                    val noteStatus = noteRepository.getNoteById(noteId)
                    noteRepository.upsertNote(
                        Note(
                            noteStatus.id,
                            noteStatus.title,
                            noteStatus.description,
                            NoteStatus.ACTIVE
                        )
                    )
                }

                _noteUnArchiveEvent.value = Event(R.string.note_un_archived)

                _noteIdEvent.value = Event(noteId)
                _backPressEvent.value = Event(Unit)
            }

            NoteStatus.TRASH -> {
                viewModelScope.launch {
                    val noteStatus = noteRepository.getNoteById(noteId)
                    noteRepository.upsertNote(
                        Note(
                            noteStatus.id,
                            noteStatus.title,
                            noteStatus.description,
                            NoteStatus.ACTIVE
                        )
                    )
                }
                _noteUnRestoreEvent.value = Event(R.string.restore_note)
                _noteIdEvent.value = Event(noteId)
                _backPressEvent.value = Event(Unit)
            }
        }

    }

}