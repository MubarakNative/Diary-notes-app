package com.mubarak.madexample.data.sources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mubarak.madexample.data.sources.local.dao.NoteDao
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.data.sources.local.model.NoteFts

@Database(
    entities = [Note::class,NoteFts::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase:RoomDatabase() {
    abstract val getDao: NoteDao

}