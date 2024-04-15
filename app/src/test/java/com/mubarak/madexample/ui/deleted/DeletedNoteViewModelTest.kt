package com.mubarak.madexample.ui.deleted

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.mubarak.madexample.MainCoroutineRule
import com.mubarak.madexample.data.repository.FakeNoteRepository
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.getOrAwaitValue
import com.mubarak.madexample.utils.NoteStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DeletedNoteViewModelTest {

    private lateinit var deletedNoteViewModel: DeletedNoteViewModel
    private lateinit var fakeNoteRepository: FakeNoteRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    private val activeNote = Note(1, "Title", "Description", NoteStatus.ACTIVE)
    private val archivedNote = Note(1, "Title", "Description", NoteStatus.ARCHIVE)

    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepository()
        deletedNoteViewModel = DeletedNoteViewModel(fakeNoteRepository)

    }

    @Test
    fun noteStatusShouldChangeIntoTrash() = runTest {
        fakeNoteRepository.insertNote(activeNote)

        deletedNoteViewModel.undoUnRestore(noteId = activeNote.id)
        val expected = fakeNoteRepository.getNoteById(activeNote.id)
        val archiveExpected = fakeNoteRepository.getNoteById(archivedNote.id)
        val actual = Note(
            1,
            "Title",
            "Description",
            NoteStatus.TRASH
        )
        assertThat(actual).isEqualTo(expected)
        assertThat(actual).isEqualTo(archiveExpected)
    }

    @Test
    fun getAllNotesByDeleted() = runTest {
        fakeNoteRepository.noteList.add(
            Note(
                1,
                "Deleted Title",
                "Deleted Note Description",
                NoteStatus.TRASH
            )
        )
        val deletedNoteList = deletedNoteViewModel.getNoteByStatus.getOrAwaitValue()
        assertThat("Deleted Title").contains(deletedNoteList[0].title)
        assertThat(deletedNoteList).hasSize(1)
    }
}