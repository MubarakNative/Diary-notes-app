package com.mubarak.madexample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mubarak.madexample.R
import com.mubarak.madexample.utils.Event
import com.mubarak.madexample.utils.NoteStatus
import javax.inject.Inject

/** SharedViewModel is used to Share's the events between fragments*/
class SharedViewModel @Inject constructor(
) :ViewModel(){

    private val _snackBarEvent = MutableLiveData<Event<Int>>()
    val snackBarEvent: LiveData<Event<Int>> = _snackBarEvent

    private val _noteArchivedEvent = MutableLiveData<Event<Int>>()
    val noteArchivedEvent: LiveData<Event<Int>> = _noteArchivedEvent

    private val _noteUnArchivedEvent = MutableLiveData<Event<Int>>()
    val noteUnArchivedEvent: LiveData<Event<Int>> = _noteUnArchivedEvent

    private val _noteMovedToTrashEvent = MutableLiveData<Event<Int>>()
    val noteMovedToTrashEvent: LiveData<Event<Int>> = _noteMovedToTrashEvent

    private val _noteUnRestoreEvent = MutableLiveData<Event<Int>>()
    val noteUnRestoreEvent: LiveData<Event<Int>> = _noteUnRestoreEvent

    private val _noteIdEvent = MutableLiveData<Event<Long>>()
    val noteIdEvent: LiveData<Event<Long>> = _noteIdEvent

    private val _noteDeletedEvent = MutableLiveData<Event<Int>>()
    val noteDeletedEvent: LiveData<Event<Int>> = _noteDeletedEvent

    private var noteStatusEvent = Event(NoteStatus.ACTIVE)

    fun onBlankNote(){
        _snackBarEvent.value = Event(R.string.empty_note_message)
    }

    fun onNoteStatusChanges(noteStatus: NoteStatus){
        noteStatusEvent = Event(noteStatus)
    }

    fun onNoteDeleted(){
        _noteDeletedEvent.value = Event(R.string.note_moved_trash)
    }

    fun onNoteArchived() {
        _noteArchivedEvent.value = Event(R.string.note_archived)

    }

    fun getNoteId(it: Long) {
        _noteIdEvent.value = Event(it)
    }

    fun onNoteUnArchived() {
        _noteUnArchivedEvent.value = Event(R.string.note_un_archived)
    }

    fun onNoteMovedToTrash(){
        _noteMovedToTrashEvent.value = Event(R.string.note_moved_trash)
    }

    fun onNoteUnRestore(){
        _noteUnRestoreEvent.value = Event(R.string.restore_note)
    }

}