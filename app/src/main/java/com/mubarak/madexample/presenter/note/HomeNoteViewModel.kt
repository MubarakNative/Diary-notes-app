package com.mubarak.madexample.presenter.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mubarak.madexample.data.Note
import com.mubarak.madexample.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
):ViewModel() {

    private val _getAllNotes: MutableStateFlow<List<Note>> = MutableStateFlow<List<Note>>(emptyList())
    val getAllNotes:StateFlow<List<Note>> = _getAllNotes.asStateFlow()

  private fun getAllNotes(){
        viewModelScope.launch {
            noteRepository.getAllNote().catch {

            }.collect{
                _getAllNotes.value = it
            }

        }
    }

}