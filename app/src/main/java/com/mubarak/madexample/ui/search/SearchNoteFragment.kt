package com.mubarak.madexample.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mubarak.madexample.R
import com.mubarak.madexample.utils.NoteLayout
import com.mubarak.madexample.databinding.FragmentSearchNoteBinding
import com.mubarak.madexample.ui.note.HomeNoteItemAdapter
import com.mubarak.madexample.ui.note.HomeNoteViewModel
import com.mubarak.madexample.utils.onUpButtonClick
import com.mubarak.madexample.utils.showSoftKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchNoteFragment : Fragment() {

    private lateinit var binding: FragmentSearchNoteBinding
    private val searchNoteViewModel: SearchNoteViewModel by viewModels()
    private val homeNoteViewModel: HomeNoteViewModel by viewModels()


    private val adapter by lazy {
        HomeNoteItemAdapter(homeNoteViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchNoteBinding.inflate(layoutInflater,
            container,false).apply {
            viewmodel = searchNoteViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarSearch.onUpButtonClick()
        setUpRecyclerView()

        onNoteItemClick()

        /**for handling overlapping in landscape mode*/
        ViewCompat.setOnApplyWindowInsetsListener(binding.searchCoordinator){ v, insets ->
            val windowInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            v.updatePadding(
                right =  windowInsets.right,
                top = windowInsets.top,
                bottom = windowInsets.bottom,
                left = windowInsets.left
            )
            WindowInsetsCompat.CONSUMED
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(searchQuery: String?): Boolean {

                searchQuery?.let { search ->
                    val filteredQuery = SearchQueryFilter.filterQuery(search)
                    searchNoteViewModel.searchNote(filteredQuery)
                }
                return true
            }

        })

        searchNoteViewModel.searchResults.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        // to focus the SearchView automatically when we enter searchNoteFragment
        binding.searchView.setOnQueryTextFocusChangeListener { edittext, hasFocus ->
            if (hasFocus) {
                view.showSoftKeyboard(edittext.findFocus())
            }
        }
        binding.searchView.requestFocus()

    }

    // to show the search note-item layout according to the value set in HomeFragment
    private fun setUpRecyclerView() {
        homeNoteViewModel.noteItemLayout.observe(viewLifecycleOwner) {
            when (it) {
                NoteLayout.LIST.ordinal -> {
                    binding.searchNoteList.layoutManager = LinearLayoutManager(requireContext())
                }

                NoteLayout.GRID.ordinal -> {
                    binding.searchNoteList.layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                }
            }

        }
        binding.apply {
            searchNoteList.setHasFixedSize(true)
            searchNoteList.adapter = adapter
        }
    }

    private fun onNoteItemClick() { // to open note-detail screen in search

        homeNoteViewModel.getNoteIdEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { noteId ->
                val action = SearchNoteFragmentDirections.actionSearchNoteFragmentToActionNoteFragment(noteId)
                findNavController().navigate(action)
            }
        }
    }

}