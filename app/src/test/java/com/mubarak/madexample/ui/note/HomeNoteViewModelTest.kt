package com.mubarak.madexample.ui.note

import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mubarak.madexample.data.repository.FakeNoteRepository
import com.mubarak.madexample.data.sources.datastore.FakeUserPreference
import com.mubarak.madexample.getOrAwaitValue
import com.mubarak.madexample.utils.NoteLayout
import com.mubarak.madexample.utils.NoteStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.function.Predicate.not

class HomeNoteViewModelTest {

    private lateinit var homeNoteViewModel: HomeNoteViewModel
    private lateinit var fakeNoteRepository: FakeNoteRepository
    private lateinit var userPreference: FakeUserPreference

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // todo: write a use-case that verifies toggle note layout works correctly
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun init() {
        fakeNoteRepository = FakeNoteRepository()
        userPreference = FakeUserPreference()
        Dispatchers.setMain(StandardTestDispatcher())
        homeNoteViewModel = HomeNoteViewModel(fakeNoteRepository, userPreference)
    }

    @Test
    fun toggleNoteLayout() {

    }
}