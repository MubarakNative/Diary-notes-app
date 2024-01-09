package com.mubarak.madexample.presenter.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.mubarak.madexample.R
import com.mubarak.madexample.data.Note
import com.mubarak.madexample.data.repository.NoteRepository
import com.mubarak.madexample.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _getNoteIdEvent: MutableLiveData<Event<String>> = MutableLiveData()
    val getNoteIdEvent: LiveData<Event<String>> = _getNoteIdEvent

    /**[getAllNote] returns all the note if it present*/
    val getAllNote = noteRepository.getAllNote()


    /** [isEmpty] is boolean tells whether our note is empty if it empty we show empty note image else show the note*/
    val isEmpty: StateFlow<Boolean> = getAllNote.map { it.isEmpty() }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        true
    )


    /**[Event] is a wrapper class it trigger when any event occur like deletion or insertion according to this event
     * we trigger some code in ui such as navigation or show a snack-bar*/
    private val _noteDeletedEvent = MutableLiveData<Event<Int>>()
    val noteDeletedEvent :LiveData<Event<Int>> = _noteDeletedEvent

    /**We get note_id from [note_list_item] layout when click on the item we wrap the id into Event wrapper class later we observe the event and get id
     * from Ui*/
    fun getNoteId(noteId:String){
        _getNoteIdEvent.value = Event(noteId)
    }

    /**delete a specific note*/
    fun deleteNote(note:Note){
        viewModelScope.launch {
            noteRepository.deleteNote(note)
            _noteDeletedEvent.value = Event(R.string.note_deleted)
        }
    }

    /**insert a note*/
    fun undoDeletedNote(note:Note){ // insert note for undo deletion
        viewModelScope.launch {
            noteRepository.insertNote(note)
        }
    }
}