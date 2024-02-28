package com.mubarak.madexample.data.sources.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.mubarak.madexample.utils.Constants.NOTE_ITEM_LAYOUT_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoPreferenceDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    val getNoteLayout: Flow<Int> =
        dataStore.data.map {
            it[NOTE_LAYOUT_KEY] ?: 0 // 0 means list
        }
    suspend fun setNoteLayout(noteLayout: Int) {
        dataStore.edit {
            it[NOTE_LAYOUT_KEY] = noteLayout
        }
    }

    companion object {
        val NOTE_LAYOUT_KEY = intPreferencesKey(NOTE_ITEM_LAYOUT_KEY)
    }

}
