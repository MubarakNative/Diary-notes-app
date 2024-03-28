package com.mubarak.madexample.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mubarak.madexample.R
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.data.repository.NoteRepository
import com.mubarak.madexample.data.sources.datastore.TodoPreferenceDataStore
import com.mubarak.madexample.utils.NoteLayout
import com.mubarak.madexample.utils.Event
import com.mubarak.madexample.utils.NoteStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val todoPreferenceDataStore: TodoPreferenceDataStore
) : ViewModel(),NoteItemAdapter.NoteAdapterListener  {

    private val _onNoteSwipe = MutableLiveData<Event<Note>>()
    val onNoteSwipe: LiveData<Event<Note>> = _onNoteSwipe

    private val _onNoteItemClick = MutableLiveData<Event<Note>>()
    val onNoteItemClick: LiveData<Event<Note>> = _onNoteItemClick

    val getAllNote = noteRepository.getNoteByStatus(NoteStatus.ACTIVE)

    private val _noteItemLayout: MutableLiveData<String> = MutableLiveData()
    val  noteItemLayout: LiveData<String> = _noteItemLayout

    // This is for displaying placeholder in home fragment
    val isEmpty: StateFlow<Boolean> = getAllNote.map { it.isEmpty() }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        true
    )

    private val _noteStatusChangeEvent = MutableLiveData<Event<Int>>()
    val noteStatusChangeEvent: LiveData<Event<Int>> = _noteStatusChangeEvent


    init {
        viewModelScope.launch{
            _noteItemLayout.value =todoPreferenceDataStore.getNoteLayout.first()}
    }

    // toggle between list to grid and vice versa in Home Fragment
    fun toggleNoteLayout(){

        viewModelScope.launch {
            val layout = when(_noteItemLayout.value){
                NoteLayout.LIST.name -> NoteLayout.GRID.name
                NoteLayout.GRID.name -> NoteLayout.LIST.name
                else -> {return@launch}
            }

            _noteItemLayout.value = layout // update the changed value
            todoPreferenceDataStore.setNoteLayout(layout) // store the updated value in datastore
        }
    }

    fun redoNoteToActive(noteId: Long){
        viewModelScope.launch{
            val note = noteRepository.getNoteById(noteId)
            val updateNote = Note(note.id,note.title,note.description,NoteStatus.ACTIVE)
            noteRepository.upsertNote(updateNote)
        }
    }
    fun updateNoteStatus(noteId: Long) {
        viewModelScope.launch{
            val note = noteRepository.getNoteById(noteId)
            val updateNote = Note(note.id,note.title,note.description,NoteStatus.ARCHIVE)
            noteRepository.upsertNote(updateNote)
            _noteStatusChangeEvent.value = Event(R.string.note_archived)
        }
    }
    override fun onNoteItemClicked(note: Note) {
        _onNoteItemClick.value = Event(note)
    }

    override fun onNoteSwipe(note: Note) {
        _onNoteSwipe.value = Event(note)
    }
}