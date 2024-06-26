package com.mubarak.madexample.data.sources.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mubarak.madexample.utils.Constants.NOTE_ITEM_LAYOUT_KEY
import com.mubarak.madexample.utils.NoteLayout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoPreferenceDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreference {
    override suspend fun setNoteLayout(noteLayout: String) {
        dataStore.edit {
            it[NOTE_LAYOUT_KEY] = noteLayout
        }
    }

    override val getNoteLayout: Flow<String>
        get() = dataStore.data.map {
            it[NOTE_LAYOUT_KEY] ?: NoteLayout.LIST.name // default is LIST
        }

    companion object {
        val NOTE_LAYOUT_KEY = stringPreferencesKey(NOTE_ITEM_LAYOUT_KEY)
    }
}

interface UserPreference {
    suspend fun setNoteLayout(noteLayout: String)
    val getNoteLayout: Flow<String>
}
