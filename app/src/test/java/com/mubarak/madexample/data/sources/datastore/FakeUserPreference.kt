package com.mubarak.madexample.data.sources.datastore

import com.mubarak.madexample.utils.NoteLayout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/** FakeUserPreference for testing*/
class FakeUserPreference : UserPreference {

    private var noteLayout: String = NoteLayout.LIST.name
    override suspend fun setNoteLayout(noteLayout: String) {
        this.noteLayout = noteLayout
    }

    override val getNoteLayout: Flow<String>
        get() = flow { emit(noteLayout) }
}