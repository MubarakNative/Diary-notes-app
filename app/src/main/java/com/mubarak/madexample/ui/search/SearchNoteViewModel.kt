package com.mubarak.madexample.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mubarak.madexample.data.repository.NoteRepository
import com.mubarak.madexample.data.sources.local.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private var searchJob: Job? = null

    private val _searchResults: MutableLiveData<List<Note>> = MutableLiveData(emptyList())
    val searchResults: LiveData<List<Note>> = _searchResults

    fun searchNote(searchQuery: String) {
        searchJob = viewModelScope.launch {
            delay(DEBOUNCE_DELAY)
            noteRepository.searchNote(searchQuery).
            catch {
                emit(emptyList())
            }.collect{
                _searchResults.value = it
            }
        }

    }
    companion object {
        private const val DEBOUNCE_DELAY = 120L
    }

}