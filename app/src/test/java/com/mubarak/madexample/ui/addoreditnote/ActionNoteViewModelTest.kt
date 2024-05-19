package com.mubarak.madexample.ui.addoreditnote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.mubarak.madexample.MainCoroutineRule
import com.mubarak.madexample.data.repository.FakeNoteRepository
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.data.sources.local.model.Reminder
import com.mubarak.madexample.utils.NoteStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ActionNoteViewModelTest {

    private lateinit var actionNoteViewModel: ActionNoteViewModel
    private lateinit var fakeNoteRepository: FakeNoteRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    private val note = Note(1, "New note title", "New note description", null,NoteStatus.ACTIVE)

    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepository()
        actionNoteViewModel = ActionNoteViewModel(fakeNoteRepository)
    }

    @Test
    fun insertNote_ActiveNote_ShouldReturnSameNote() = runTest {
        fakeNoteRepository.insertNote(note)
        val noteId = fakeNoteRepository.getNoteById(note.id)
        assertThat(note.title).isEqualTo(noteId.title)
        assertThat(note.description).isEqualTo(noteId.description)
    }


}