package com.mubarak.madexample.presenter.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.mubarak.madexample.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {


    fun searchNote(searchQuery: String) =
        noteRepository.searchNote(searchQuery).asLiveData()


}