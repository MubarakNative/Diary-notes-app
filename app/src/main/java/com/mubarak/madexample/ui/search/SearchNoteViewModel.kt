package com.mubarak.madexample.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mubarak.madexample.data.sources.NoteRepository
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.ui.note.NoteItemAdapter
import com.mubarak.madexample.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel(), NoteItemAdapter.NoteItemClickListener {

    private val _onNoteItemClick = MutableLiveData<Event<Note>>()
    val onNoteItemClick: LiveData<Event<Note>> = _onNoteItemClick

    private var searchJob: Job? = null

    private val _searchResults: MutableLiveData<List<Note>> = MutableLiveData(emptyList())
    val searchResults: LiveData<List<Note>> = _searchResults

    fun searchNote(searchQuery: String) {
        searchJob = viewModelScope.launch {
            delay(DEBOUNCE_DELAY)
            noteRepository.searchNote(searchQuery).catch {
                emit(emptyList())
            }.collect {
                _searchResults.value = it
            }
        }

    }

    companion object {
        private const val DEBOUNCE_DELAY = 120L
    }

    override fun onNoteItemClick(note: Note) {
        _onNoteItemClick.value = Event(note)
    }

}