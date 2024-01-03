package com.mubarak.madexample.data.repository

import com.mubarak.madexample.data.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun insertNote(note:Note)
    suspend fun upsertNote(note:Note)
     fun getAllNote(): Flow<List<Note>>
    suspend fun deleteNote(note:Note)
    suspend fun deleteNoteById(noteId:Int)
}