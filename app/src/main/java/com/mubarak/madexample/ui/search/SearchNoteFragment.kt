package com.mubarak.madexample.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mubarak.madexample.R
import com.mubarak.madexample.ui.note.HomeNoteItemAdapter
import com.mubarak.madexample.databinding.FragmentSearchNoteBinding
import com.mubarak.madexample.ui.note.HomeNoteViewModel
import com.mubarak.madexample.utils.onUpButtonClick
import com.mubarak.madexample.utils.showSoftKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchNoteFragment : Fragment() {

    private lateinit var binding: FragmentSearchNoteBinding
    private val searchNoteViewModel:SearchNoteViewModel by viewModels()
    private val homeNoteViewModel:HomeNoteViewModel by viewModels()

    /**todo NOTE: We can also improve the search functionality provided in this app
     * ex: we can use fts (Full text search in sqlite)
     * todo This is a very basic search functionality in future we wil Improve them!! */

    private val adapter by lazy {
        HomeNoteItemAdapter(homeNoteViewModel)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.fragment_search_note,
            container,
            false
        )
        binding = FragmentSearchNoteBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = searchNoteViewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarSearch.onUpButtonClick()
        setUpRecyclerView()


        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
               return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchNoteViewModel.searchNote(newText.toString()).observe(viewLifecycleOwner){
                    adapter.submitList(it)
                } // checking purpose don't write in this ;
                return true
            }

        })

        /** for showing soft-keyboard in search-view
         * see https://stackoverflow.com/questions/12022715/unable-to-show-keyboard-automatically-in-the-searchview
         * */
        binding.searchView.setOnQueryTextFocusChangeListener { edittext, hasFocus ->
            if (hasFocus){
                view.showSoftKeyboard(edittext.findFocus())
            }
        }
        binding.searchView.requestFocus()

    }

    private fun setUpRecyclerView(){
        binding.apply {
            binding.searchNoteList.layoutManager = LinearLayoutManager(requireContext())
            binding.searchNoteList.setHasFixedSize(true)
            binding.searchNoteList.adapter= adapter
        }
    }


}