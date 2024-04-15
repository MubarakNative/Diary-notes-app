package com.mubarak.madexample.ui.archive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.mubarak.madexample.MainCoroutineRule
import com.mubarak.madexample.data.repository.FakeNoteRepository
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.utils.NoteStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ArchiveNoteViewModelTest {

    private lateinit var archiveNoteViewModel: ArchiveNoteViewModel
    private lateinit var fakeNoteRepository: FakeNoteRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    private val activeNote = Note(1, "Title", "Description", NoteStatus.ACTIVE)
    private val deletedNote = Note(1, "Title", "Description", NoteStatus.TRASH)

    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepository()
        archiveNoteViewModel = ArchiveNoteViewModel(fakeNoteRepository)

    }

    @Test
    fun noteStatusShouldChangeIntoArchive() = runTest{

        fakeNoteRepository.insertNote(activeNote)

        archiveNoteViewModel.undoUnArchive(noteId = activeNote.id)
        val expected = fakeNoteRepository.getNoteById(activeNote.id)
        val archiveActual = fakeNoteRepository.getNoteById(deletedNote.id)
        val actual = Note(
            1,
            "Title",
            "Description",
            NoteStatus.ARCHIVE
        )
        Truth.assertThat(actual).isEqualTo(expected)
        Truth.assertThat(actual).isEqualTo(archiveActual)
    }
}