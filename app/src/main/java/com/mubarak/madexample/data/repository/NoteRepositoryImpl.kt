package com.mubarak.madexample.data.repository

import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.data.sources.local.NoteDatabase
import com.mubarak.madexample.utils.NoteStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/** [NoteRepository] implementation*/

class NoteRepositoryImpl @Inject constructor(
    private val notesDatabase: NoteDatabase
): NoteRepository {
    override suspend fun insertNote(note: Note) = withContext(Dispatchers.IO){
        notesDatabase.getDao.insertNote(note)
    }

    override suspend fun upsertNote(note: Note) = withContext(Dispatchers.IO){
        notesDatabase.getDao.upsertNote(note)
    }

    override fun getAllNote(): Flow<List<Note>> {
        return notesDatabase.getDao.getAllNotes()
    }

    override suspend fun deleteNote(note: Note) = withContext(Dispatchers.IO){
        notesDatabase.getDao.deleteNote(note)
    }

    override suspend fun deleteNoteById(noteId: Long)  = withContext(Dispatchers.IO) {
        notesDatabase.getDao.deleteNoteById(noteId)
    }

    override suspend fun deleteAllNotes() {
        notesDatabase.getDao.deleteAllNotes()
    }

    override suspend fun deleteAllNotesInTrash() {
        notesDatabase.getDao.deleteAllNotesInTrash()
    }

    override fun searchNote(searchQuery: String): Flow<List<Note>> {
        return notesDatabase.getDao.getSearchedNote(searchQuery)
    }

    override fun getNoteStreamById(noteId: Long): Flow<Note> {
        return notesDatabase.getDao.getNoteStream(noteId)
    }

    override fun getNoteByStatus(noteStatus: NoteStatus): Flow<List<Note>> {
        return notesDatabase.getDao.getNoteByStatus(noteStatus)
    }

    override suspend fun getNoteById(noteId: Long):Note{
        return withContext(Dispatchers.IO){
            notesDatabase.getDao.getNoteById(noteId)
        }

    }

}