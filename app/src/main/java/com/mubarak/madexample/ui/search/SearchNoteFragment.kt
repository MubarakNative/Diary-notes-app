package com.mubarak.madexample.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mubarak.madexample.utils.NoteLayout
import com.mubarak.madexample.databinding.FragmentSearchNoteBinding
import com.mubarak.madexample.ui.note.NoteItemAdapter
import com.mubarak.madexample.ui.note.HomeNoteViewModel
import com.mubarak.madexample.utils.onUpButtonClick
import com.mubarak.madexample.utils.showSoftKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchNoteFragment : Fragment() {

    private var _binding: FragmentSearchNoteBinding? = null
    private val binding: FragmentSearchNoteBinding get() = _binding!!
    private val searchNoteViewModel: SearchNoteViewModel by viewModels()
    private val homeNoteViewModel: HomeNoteViewModel by viewModels()


    private val adapter by lazy {
        NoteItemAdapter(noteItemClickListener =searchNoteViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchNoteBinding.inflate(layoutInflater,
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

        searchNoteViewModel.onNoteItemClick.observe(viewLifecycleOwner) { note ->
            note.getContentIfNotHandled()?.let {
                val directions =
                    SearchNoteFragmentDirections.actionSearchNoteFragmentToActionNoteFragment(it.id)
                findNavController().navigate(directions)
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?)= false

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
    private fun setUpRecyclerView() {
        homeNoteViewModel.noteItemLayout.observe(viewLifecycleOwner) {
            when (it) {
                NoteLayout.LIST.name -> {
                    binding.searchNoteList.layoutManager = LinearLayoutManager(requireContext())
                }

                NoteLayout.GRID.name -> {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}