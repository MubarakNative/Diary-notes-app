package com.mubarak.madexample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mubarak.madexample.R
import com.mubarak.madexample.utils.Event
import javax.inject.Inject

/** SharedViewModel is used to Share's the events between fragments*/
class SharedViewModel @Inject constructor(
) :ViewModel(){

    private val _snackBarEvent = MutableLiveData<Event<Int>>()
    val snackBarEvent: LiveData<Event<Int>> = _snackBarEvent

    private val _noteDeletedEvent = MutableLiveData<Event<Int>>()
    val noteDeletedEvent: LiveData<Event<Int>> = _noteDeletedEvent


    fun onBlankNote(){
        _snackBarEvent.value = Event(R.string.empty_note_message)
    }

    fun onNoteDeleted(){
        _noteDeletedEvent.value = Event(R.string.note_deleted)
    }

}