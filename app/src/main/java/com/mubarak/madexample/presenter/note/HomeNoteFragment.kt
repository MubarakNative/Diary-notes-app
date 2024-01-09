package com.mubarak.madexample.presenter.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mubarak.madexample.R
import com.mubarak.madexample.adapter.HomeNoteItemAdapter
import com.mubarak.madexample.data.Note
import com.mubarak.madexample.databinding.FragmentHomeNoteBinding
import com.mubarak.madexample.utils.openNavDrawer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeNoteFragment : Fragment() {

    private lateinit var binding: FragmentHomeNoteBinding
    private val homeViewModel: HomeNoteViewModel by viewModels()
    lateinit var draggedNote: Note
    val homeAdapter by lazy { HomeNoteItemAdapter(homeViewModel) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val rootView = inflater.inflate(
            R.layout.fragment_home_note,
            container,
            false
        )
        binding = FragmentHomeNoteBinding.bind(
            rootView
        ).apply {
            viewModel = homeViewModel
            lifecycleOwner = viewLifecycleOwner

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCreateNote.setOnClickListener {
            navigateToAddEditFragment()

        }

        /**we get the note id from note_list_item layout this note_id will available for [HomeNoteViewModel]
         * we simply observe it*/
        homeViewModel.getNoteIdEvent.observe(viewLifecycleOwner){ noteId ->
            noteId.getContentIfNotHandled()?.let {
                navigateToEditNoteFragment(it)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.getAllNote.collect {
                    homeAdapter.submitList(it)
                    binding.homeNoteList.adapter = homeAdapter
                }
            }
        }



        binding.apply {
            toolBarHome.setNavigationOnClickListener {
                requireView().openNavDrawer(requireActivity())
            }
            toolBarHome.setOnMenuItemClickListener { menuItem ->
                return@setOnMenuItemClickListener when (menuItem.itemId) {
                    R.id.searchNote -> {
                        findNavController().navigate(R.id.action_homeNoteFragment_to_searchNoteFragment)
                        true
                    }

                    else -> false
                }
            }
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                draggedNote = homeAdapter.currentList[viewHolder.adapterPosition]
                homeViewModel.deleteNote(draggedNote)
            }

        }).attachToRecyclerView(binding.homeNoteList)

        homeViewModel.noteDeletedEvent.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).setAction(
                    "Undo"
                ) {
                    homeViewModel.undoDeletedNote(draggedNote) // undo deletion means insert the
                // same note which was deleted
                }.show()
            }
        }

    }

    /**note_id is only available if the note should exist and click on the note item
     * [TODO that means we need to edit the note to edit it we pass the note it to [ActionNoteFragment]]
     * */
    private fun navigateToEditNoteFragment(noteId:String) {
        Toast.makeText(requireContext(), noteId, Toast.LENGTH_SHORT).show()
        val action = HomeNoteFragmentDirections.actionHomeNoteFragmentToActionNoteFragment(
            noteId
        )
        findNavController().navigate(action)
    }


     private fun navigateToAddEditFragment() {
            val action = HomeNoteFragmentDirections.actionHomeNoteFragmentToActionNoteFragment(
                null
            )
            findNavController().navigate(action)

    }

}