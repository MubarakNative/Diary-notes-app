package com.mubarak.madexample.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.mubarak.madexample.MainCoroutineRule
import com.mubarak.madexample.data.repository.FakeNoteRepository
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.getOrAwaitValue
import com.mubarak.madexample.ui.deleted.DeletedNoteViewModel
import com.mubarak.madexample.utils.NoteStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchNoteViewModelTest {

    private lateinit var searchNoteViewModel: SearchNoteViewModel
    private lateinit var fakeNoteRepository: FakeNoteRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepository()
        searchNoteViewModel = SearchNoteViewModel(fakeNoteRepository)
    }
    @Test
    fun searchNote()= runTest{

    }
}