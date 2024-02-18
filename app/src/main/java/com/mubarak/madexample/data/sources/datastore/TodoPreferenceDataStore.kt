package com.mubarak.madexample.data.sources.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.mubarak.madexample.utils.Constants.NOTE_ITEM_LAYOUT_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoPreferenceDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    val getNoteItemLayout: Flow<Boolean> =
        dataStore.data.map {
            it[NOTE_ORDER_KEY] ?: false // default note layout is linear
        }

    suspend fun setNoteItemLayout(noteLayout: Boolean) {
        dataStore.edit {
            it[NOTE_ORDER_KEY] = noteLayout
        }
    }

    companion object {
        val NOTE_ORDER_KEY = booleanPreferencesKey(NOTE_ITEM_LAYOUT_KEY)
    }
}
