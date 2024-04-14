package com.mubarak.madexample.ui.note

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.mubarak.madexample.MainCoroutineRule
import com.mubarak.madexample.data.repository.FakeNoteRepository
import com.mubarak.madexample.data.sources.datastore.FakeUserPreference
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.getOrAwaitValue
import com.mubarak.madexample.utils.NoteLayout
import com.mubarak.madexample.utils.NoteStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class HomeNoteViewModelTest {

    private lateinit var homeNoteViewModel: HomeNoteViewModel
    private lateinit var fakeNoteRepository: FakeNoteRepository
    private lateinit var userPreference: FakeUserPreference

    @ExperimentalCoroutinesApi
     @get:Rule
      val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        fakeNoteRepository = FakeNoteRepository()
        userPreference = FakeUserPreference()
        homeNoteViewModel = HomeNoteViewModel(fakeNoteRepository, userPreference)

    }

    @Test
    fun listLayoutMode_ShouldReturn_GRID() = runTest {
        val actual = homeNoteViewModel.noteItemLayout.getOrAwaitValue()
        assertThat(actual).isNotEqualTo(NoteLayout.GRID.name)
    }

    @Test
    fun archiveNoteShouldChangeInto_Active() = runTest {
        val note = Note(
            1,
            "Title",
            "Description",
            NoteStatus.ARCHIVE
        )
        fakeNoteRepository.insertNote(note)

        homeNoteViewModel.redoNoteToActive(noteId = note.id) // it redo note to Active
        val actual = fakeNoteRepository.getNoteById(note.id) // now it ACTIVE
        val expectNote = Note(
            1,
            "Title",
            "Description",
            NoteStatus.ACTIVE
        )
        assertThat(expectNote).isEqualTo(actual)
    }

    @Test
    fun trashNoteShouldChangeInto_Active() = runTest {
        val note = Note(
            1,
            "Title",
            "Description",
            NoteStatus.TRASH
        )
        fakeNoteRepository.insertNote(note)

        homeNoteViewModel.redoNoteToActive(noteId = note.id) // it redo note to Active
        val actual = fakeNoteRepository.getNoteById(note.id) // now it ACTIVE
        val expectNote = Note(
            1,
            "Title",
            "Description",
            NoteStatus.ACTIVE
        )
        assertThat(expectNote).isEqualTo(actual)
    }

    @Test
    fun activeNoteShouldChangeIntoArchive() = runTest{
        val note = Note(
            1,
            "Title",
            "Description",
            NoteStatus.ACTIVE
        )
        fakeNoteRepository.insertNote(note)

        homeNoteViewModel.updateNoteStatus(noteId = note.id) // it redo note to ARCHIVE
        val actual = fakeNoteRepository.getNoteById(note.id) // now it ARCHIVE
        val expectNote = Note(
            1,
            "Title",
            "Description",
            NoteStatus.ARCHIVE
        )
        assertThat(expectNote).isEqualTo(actual)
    }

}