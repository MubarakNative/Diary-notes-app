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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
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
    fun setUp() {
        fakeNoteRepository = FakeNoteRepository()
        userPreference = FakeUserPreference()
        homeNoteViewModel = HomeNoteViewModel(fakeNoteRepository, userPreference)

    }

    @Test
    fun noteItemLayout_ListLayout_ShouldReturnGridLayout() {
        val actual = homeNoteViewModel.noteItemLayout.getOrAwaitValue()
        assertThat(actual).isNotEqualTo(NoteLayout.GRID.name)
    }

    @Test
    fun redoNoteToActive_NoteStatus_Archive_ShouldConvertArchiveToActive() = runTest {
        val note = Note(
            1,
            "Title",
            "Description",
            NoteStatus.ARCHIVE
        )
        fakeNoteRepository.insertNote(note)

        homeNoteViewModel.redoNoteToActive(noteId = note.id)
        val expected = fakeNoteRepository.getNoteById(note.id)
        val actual = Note(
            1,
            "Title",
            "Description",
            NoteStatus.ACTIVE
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun redoNoteToActive_NoteStatus_Trash_ShouldConvertTrashToActive() = runTest {
        val note = Note(
            1,
            "Title",
            "Description",
            NoteStatus.TRASH
        )
        fakeNoteRepository.insertNote(note)

        homeNoteViewModel.redoNoteToActive(noteId = note.id) // it redo note to Active
        val expected = fakeNoteRepository.getNoteById(note.id) // now it ACTIVE
        val actual = Note(
            1,
            "Title",
            "Description",
            NoteStatus.ACTIVE
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun updateNoteStatus_NoteStatus_Active_ShouldConvertActiveToArchive() = runTest{
        val note = Note(
            1,
            "Title",
            "Description",
            NoteStatus.ACTIVE
        )
        fakeNoteRepository.insertNote(note)

        homeNoteViewModel.updateNoteStatus(noteId = note.id)
        val expected = fakeNoteRepository.getNoteById(note.id)
        val actual = Note(
            1,
            "Title",
            "Description",
            NoteStatus.ARCHIVE
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun getAllNotes_AddSingleNote_ShouldReturnSameNote() = runTest {

        fakeNoteRepository.noteList.add(
            Note(1,"Note Title","Note Description",NoteStatus.ACTIVE)
        )
        val noteList = homeNoteViewModel.getAllNote.getOrAwaitValue()
        assertThat(noteList).hasSize(1)
        assertThat(noteList[0].title).contains("Note Title")
    }

}