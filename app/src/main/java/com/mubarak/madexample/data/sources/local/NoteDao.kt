package com.mubarak.madexample.data.sources.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.mubarak.madexample.data.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note_table ORDER BY note_id DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("DELETE FROM note_table WHERE note_id = :noteId")
   suspend fun deleteNoteById(noteId:Int)

   @Query(" SELECT * FROM note_table WHERE Title LIKE '%' || :searchQuery ||'%' or Description LIKE '%' || :searchQuery ||'%' ")
   fun searchNote(searchQuery:String):Flow<List<Note>>

   @Query("SELECT * FROM note_table WHERE note_id =:noteId ")
   fun getNoteById(noteId:String): Flow<Note>
}