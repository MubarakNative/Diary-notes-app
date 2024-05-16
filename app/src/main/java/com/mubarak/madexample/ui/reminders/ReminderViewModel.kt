package com.mubarak.madexample.ui.reminders

import androidx.lifecycle.ViewModel
import com.mubarak.madexample.data.sources.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(private val noteRepository: NoteRepository):ViewModel() {

}