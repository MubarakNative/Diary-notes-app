package com.mubarak.madexample.data.sources.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.mubarak.madexample.data.sources.local.NoteDatabase
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.utils.NoteStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
@ExperimentalCoroutinesApi
class NoteDaoTest {

    private lateinit var noteDatabase: NoteDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        noteDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NoteDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Test
    fun insertNote_getNoteById_ExpectingSameNote() = runTest {
        val note = Note(1, "Dao Test", "Testing Dao", null, NoteStatus.ACTIVE)
        noteDatabase.getDao.insertNote(note)

        val getNote = noteDatabase.getDao.getNoteById(note.id)
        assertThat(getNote).isEqualTo(note)
    }

    @Test
    fun updateNote_getNoteById_ExpectingDiffNoteAttribute() = runTest {
        val note = Note(1, "Original Note Title", "Original Note Description",null, NoteStatus.ACTIVE)
        noteDatabase.getDao.insertNote(note)

        val updatedNote = Note(note.id, "Updated Title", "Updated Description",null,NoteStatus.ARCHIVE)
        noteDatabase.getDao.upsertNote(updatedNote)
        val getNote = noteDatabase.getDao.getNoteById(note.id)
        assertThat(getNote).isEqualTo(updatedNote)

    }

    @Test
    fun insertAllNotes_getAllNotes_ExpectingSameNote() = runTest {
        val noteList = listOf(
            Note(1, "1st Note Title", "1st Note Description", null,NoteStatus.ACTIVE),
            Note(2, "2st Note Title", "2st Note Description", null,NoteStatus.ARCHIVE),
            Note(3, "3st Note Title", "3st Note Description", null,NoteStatus.TRASH),
        )
        noteDatabase.getDao.insertAllNote(noteList)
        val getAllNotes = noteDatabase.getDao.getAllNotes().first()
        val titleDescending = noteList.sortedByDescending { it.title }
        assertThat(getAllNotes).isEqualTo(titleDescending)
    }

    @Test
    fun deleteNote_getAllNotes_EmptyListIsEmpty() = runTest {
        val note = Note(1, "Title", "Description",null,NoteStatus.TRASH)
        noteDatabase.getDao.insertNote(note)
        noteDatabase.getDao.deleteNote(note)

        val getNote = noteDatabase.getDao.getAllNotes().first()
        assertThat(getNote.isEmpty()).isEqualTo(true)
    }

    @Test
    fun deleteNoteById_getAllNotes_EmptyListIsEmpty() = runTest {
        val note = Note(1, "Title", "Description",null,NoteStatus.ACTIVE)
        noteDatabase.getDao.insertNote(note)
        noteDatabase.getDao.deleteNoteById(note.id)

        val getNote = noteDatabase.getDao.getAllNotes().first()
        assertThat(getNote.isEmpty()).isEqualTo(true)
    }

    @Test
    fun deleteAllNote_getAllNotes_EmptyListIsEmpty() = runTest {
        noteDatabase.getDao.insertAllNote(
            listOf(
                Note(1, "Title 1", "1 Description",null,NoteStatus.ACTIVE),
                Note(2, "Title 2", "2 Description", null,NoteStatus.ARCHIVE),
                Note(3, "Title 3", "3 Description",null,NoteStatus.TRASH),
            )
        )
        noteDatabase.getDao.deleteAllNotes()
        val getNote = noteDatabase.getDao.getAllNotes().first()
        assertThat(getNote.isEmpty()).isEqualTo(true)

    }

    @Test
    fun deleteAllNotesInTrash_getNoteByStatusTrash_EmptyListIsEmpty() = runTest {

        noteDatabase.getDao.insertNote(Note(1, "Title", "Description",null, NoteStatus.TRASH))
        noteDatabase.getDao.deleteAllNotesInTrash()
        val getNote = noteDatabase.getDao.getNoteByStatus(NoteStatus.TRASH).first()
        assertThat(getNote.isEmpty()).isEqualTo(true)
    }

    @Test
    fun getNoteStream_getNoteById_ExpectedNoteWithSameAttrsThatInserted() = runTest {
        val note = Note(1, "Title", "Description", null,NoteStatus.TRASH)
        noteDatabase.getDao.insertNote(note)
        val getNote = noteDatabase.getDao.getNoteStream(note.id).first()
        assertThat(getNote).isEqualTo(note)
    }

    @Test
    fun getSearchedNote_getAllNote_ExpectedAppropriateSearchedNote() = runTest {
        val noteList = listOf(
            Note(1, "Test", "NoteDao Test", null,NoteStatus.ACTIVE),
            Note(2, "Development", "This Project is under heavy development", null,NoteStatus.ARCHIVE),
            Note(3, "Architecture", "Try new make a scalable architecture", null,NoteStatus.TRASH),
        )
        noteDatabase.getDao.insertAllNote(
            noteList
        )

        val searchedNote = noteDatabase.getDao.getSearchedNote("Architecture").first()
        assertThat(searchedNote.first()).isEqualTo(noteList[2])

    }

    @After
    fun tearDown() {
        noteDatabase.close()
    }
}