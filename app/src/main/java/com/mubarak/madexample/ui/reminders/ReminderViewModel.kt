package com.mubarak.madexample.ui.reminders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mubarak.madexample.data.sources.NoteRepository
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _note = MutableLiveData<Note>()
    val note: LiveData<Note> = _note


    fun getNoteById(notedId: Long)= viewModelScope.launch {
        val note = noteRepository.getNoteById(notedId)
        _note.value = note
    }
}