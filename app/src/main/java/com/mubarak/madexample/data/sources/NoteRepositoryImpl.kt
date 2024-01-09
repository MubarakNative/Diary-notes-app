package com.mubarak.madexample.data.sources

import com.mubarak.madexample.data.Note
import com.mubarak.madexample.data.repository.NoteRepository
import com.mubarak.madexample.data.sources.local.NoteDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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

    override suspend fun deleteNoteById(noteId: Int) {
        notesDatabase.getDao.deleteNoteById(noteId)
    }

    override fun searchNote(searchQuery: String): Flow<List<Note>> {
        return notesDatabase.getDao.searchNote(searchQuery)
    }

    override fun getNoteById(noteId: String): Flow<Note> {
       return notesDatabase.getDao.getNoteById(noteId)
    }
}