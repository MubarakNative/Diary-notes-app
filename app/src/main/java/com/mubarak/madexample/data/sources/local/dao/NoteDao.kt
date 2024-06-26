package com.mubarak.madexample.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.utils.NoteStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNote(note: List<Note>)

    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note_table ORDER BY Title DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("DELETE FROM note_table WHERE note_id = :noteId")
    suspend fun deleteNoteById(noteId: Long)

    @Query("DELETE FROM note_table")
    suspend fun deleteAllNotes()
    @Query("DELETE FROM note_table WHERE note_status = :noteStatus")
    suspend fun deleteAllNotesInTrash(noteStatus: NoteStatus = NoteStatus.TRASH)

    @Query(
        """
          SELECT * FROM note_table
    JOIN note_fts on note_fts.rowid = note_id
    WHERE note_fts match :searchQuery
    """
    )
    fun getSearchedNote(searchQuery: String): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE note_id =:noteId ")
    fun getNoteStream(noteId: Long): Flow<Note>

    @Query("SELECT * FROM note_table WHERE note_status =:noteStatus")
    fun getNoteByStatus(noteStatus: NoteStatus): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE note_id =:noteId ")
    suspend fun getNoteById(noteId: Long): Note


}