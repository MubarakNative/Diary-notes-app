package com.mubarak.madexample.data.repository

import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.utils.NoteStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/** FakeNoteRepository for testing*/
class FakeNoteRepository : NoteRepository {

    private val noteList = mutableListOf<Note>()
    override suspend fun insertNote(note: Note) {
        noteList.add(note)
    }

    override suspend fun upsertNote(note: Note) {
        val index = noteList.indexOfFirst { it.id == note.id }
        if (index != -1) {
            noteList[index] = note
        } else {
            noteList.add(note)
        }
    }

    override fun getAllNote(): Flow<List<Note>> {
        return flow { emit(noteList) }
    }

    override suspend fun deleteNote(note: Note) {
        noteList.remove(note)
    }

    override suspend fun deleteNoteById(noteId: Long) {
        noteList.removeAll { it.id == noteId }
    }

    override suspend fun deleteAllNotes() {
        noteList.clear()
    }

    override suspend fun deleteAllNotesInTrash() {
        noteList.clear()
    }

    override fun searchNote(searchQuery: String): Flow<List<Note>> {
        return flow {
            val filter = noteList.filter { it.title == searchQuery || it.description == searchQuery }
            emit(filter)
        }
    }

    override fun getNoteStreamById(noteId: Long): Flow<Note> {
        return flow {noteList.find { it.id == noteId }!! }
    }

    override fun getNoteByStatus(noteStatus: NoteStatus): Flow<List<Note>> {
        return  flow {
            val filter = noteList.filter { it.noteStatus == noteStatus}
            emit(emptyList())
        }
    }

    override suspend fun getNoteById(noteId: Long): Note {
        return noteList.find { it.id == noteId }!!
    }
}