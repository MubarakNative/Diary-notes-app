package com.mubarak.madexample.data.repository

import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.data.sources.local.NoteDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** [NoteRepository] implementation*/

class NoteRepositoryImpl @Inject constructor(
    private val notesDatabase: NoteDatabase
): NoteRepository {
    override suspend fun insertNote(note: Note) {
        notesDatabase.getDao.insertNote(note)
    }

    override suspend fun upsertNote(note: Note) {
        notesDatabase.getDao.upsertNote(note)
    }

    override fun getAllNote(): Flow<List<Note>> {
        return notesDatabase.getDao.getAllNotes()
    }

    override suspend fun deleteNote(note: Note) {
        notesDatabase.getDao.deleteNote(note)
    }

    override suspend fun deleteNoteById(noteId: String) {
        notesDatabase.getDao.deleteNoteById(noteId)
    }

    override fun searchNote(searchQuery: String): Flow<List<Note>> {
        return notesDatabase.getDao.searchNote(searchQuery)
    }

    override fun getNoteById(noteId: String): Flow<Note> {
       return notesDatabase.getDao.getNoteById(noteId)
    }

    //This function need as proper name and proper asynchronous implementation
    override suspend fun getNoteByIdd(noteId: String): Note {
        return notesDatabase.getDao.getNoteByIdd(noteId)
    }


}