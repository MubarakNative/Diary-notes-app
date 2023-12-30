package com.mubarak.madexample.data.sources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mubarak.madexample.data.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDatabase:RoomDatabase() {
    abstract val getDao:NoteDao
}