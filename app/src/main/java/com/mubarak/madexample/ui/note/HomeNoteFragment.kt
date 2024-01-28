package com.mubarak.madexample.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mubarak.madexample.R
import com.mubarak.madexample.data.sources.datastore.TodoPreferenceDataStore
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.databinding.FragmentHomeNoteBinding
import com.mubarak.madexample.ui.sortdialog.SortDialogFragment
import com.mubarak.madexample.utils.openNavDrawer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeNoteFragment : Fragment() {

    @Inject
    lateinit var toDoPreferenceDataStore: TodoPreferenceDataStore

    private lateinit var binding: FragmentHomeNoteBinding
    private val homeViewModel: HomeNoteViewModel by viewModels()

    lateinit var draggedNote: Note
    val homeAdapter by lazy { HomeNoteItemAdapter(homeViewModel) }

    @Inject
   lateinit var  todoPreferenceDataStore :TodoPreferenceDataStore

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

        noteItemLayout()

        binding.fabCreateNote.setOnClickListener {
            navigateToAddEditFragment()

        }

        binding.apply {
            toolBarHome.setNavigationOnClickListener {
                requireView().openNavDrawer(requireActivity())
            }


            /**we get the note id from note_list_item layout this note_id will available for [HomeNoteViewModel]
             * we simply observe it*/
            homeViewModel.getNoteIdEvent.observe(viewLifecycleOwner) { noteId ->
                noteId.getContentIfNotHandled()?.let {
                    navigateToEditNoteFragment(it)
                }
            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    homeViewModel.getAllNote.collect { note ->
                        homeAdapter.submitList(note)
                        binding.homeNoteList.adapter = homeAdapter
                    }
                }
            }

           toolBarHome.setOnMenuItemClickListener { menuItem ->
                return@setOnMenuItemClickListener when (menuItem.itemId) {
                    R.id.action_searchNote -> {
                        findNavController().navigate(R.id.action_homeNoteFragment_to_searchNoteFragment)
                        true
                    }

                    R.id.action_sort_note_order -> {
                        /**TODO Need to implement the sort order now just show the sort_order dialog*/
                        SortDialogFragment().show(
                            childFragmentManager, ""
                        )
                        true
                    }

                    R.id.action_note_view_type -> {
                        /**TODO proper architecture implementation is needed*/
                        viewLifecycleOwner.lifecycleScope.launch{
                            var isGrid = todoPreferenceDataStore.getNoteItemLayout.first()
                            isGrid = !isGrid

                            todoPreferenceDataStore.setNoteItemLayout(isGrid) // true means grid

                        }
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
    private fun navigateToEditNoteFragment(noteId: String) { // when click on the note item
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


    /** Pending noteItemLayout impl we need to impl the grid or list item of note*/
    private fun noteItemLayout(){

        viewLifecycleOwner.lifecycleScope.launch {
            todoPreferenceDataStore.getNoteItemLayout.collect{
                val noteLayout = binding.toolBarHome.menu.findItem(R.id.action_note_view_type)
                if (it){ // Grid
                    binding.homeNoteList.layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        noteLayout.setIcon(R.drawable.list_view_icon24px).setTitle("View as List")
                }else{ // Linear (Default)
                   binding.homeNoteList.layoutManager = LinearLayoutManager(requireContext())
                   binding.homeNoteList.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

                    noteLayout.setIcon(R.drawable.grid_view_icon24px).setTitle("View as Grid")
                }
            }
        }
    }

}