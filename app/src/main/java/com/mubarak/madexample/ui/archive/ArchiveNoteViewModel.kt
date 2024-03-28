package com.mubarak.madexample.ui.archive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.mubarak.madexample.data.repository.NoteRepository
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.ui.note.NoteItemAdapter
import com.mubarak.madexample.utils.Event
import com.mubarak.madexample.utils.NoteStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository

):ViewModel(), NoteItemAdapter.NoteItemClickListener {

    private val _onNoteItemClick = MutableLiveData<Event<Note>>()
    val onNoteItemClick: LiveData<Event<Note>> = _onNoteItemClick
    fun undoUnArchive(noteId: Long) {
        viewModelScope.launch{
            val note = noteRepository.getNoteById(noteId)
            val updateNote = Note(note.id,note.title,note.description, NoteStatus.ARCHIVE)

            noteRepository.upsertNote(updateNote)
        }
    }

    val getNoteByStatus = noteRepository.getNoteByStatus(NoteStatus.ARCHIVE).asLiveData()

    val isEmpty: LiveData<Boolean> = getNoteByStatus.map{ it.isEmpty() }
    override fun onNoteItemClick(note: Note) {
        _onNoteItemClick.value = Event(note)

    }
}