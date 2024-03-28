package com.mubarak.madexample.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mubarak.madexample.R
import com.mubarak.madexample.data.sources.datastore.TodoPreferenceDataStore
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.utils.NoteLayout
import com.mubarak.madexample.databinding.FragmentHomeNoteBinding
import com.mubarak.madexample.ui.SharedViewModel
import com.mubarak.madexample.utils.openNavDrawer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeNoteFragment : Fragment() {

    private var _binding: FragmentHomeNoteBinding? = null
    private val binding: FragmentHomeNoteBinding get() = _binding!!
    @Inject
    lateinit var todoPreferenceDataStore: TodoPreferenceDataStore
    private val homeViewModel: HomeNoteViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var noteId: Long = -1
    private lateinit var draggedNote: Note
    private val homeAdapter by lazy { NoteItemAdapter(homeViewModel) }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeNoteBinding.inflate(
            layoutInflater,
            container, false
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

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.getAllNote.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED,
            ).collect { note ->
                homeAdapter.submitList(note)

            }
        }

        binding.homeNoteList.adapter = homeAdapter
        binding.homeNoteList.setHasFixedSize(true)

        homeAdapter.touchHelper.attachToRecyclerView(binding.homeNoteList)

        homeViewModel.onNoteItemClick.observe(viewLifecycleOwner) { note ->

            note.getContentIfNotHandled()?.let {
                val directions =
                    HomeNoteFragmentDirections.actionHomeNoteFragmentToActionNoteFragment(it.id)
                findNavController().navigate(directions)
            }
        }

        homeViewModel.onNoteSwipe.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { note ->
                draggedNote = note
                homeViewModel.updateNoteStatus(note.id)
            }
        }

        binding.toolBarHome.setNavigationOnClickListener {
            requireView().openNavDrawer(requireActivity())
        }

        sharedViewModel.snackBarEvent.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let {
                Snackbar.make(binding.homeCoordinator, it, Snackbar.LENGTH_SHORT)
                    .setAnchorView(binding.fabCreateNote)
                    .setGestureInsetBottomIgnored(true).show()
            }
        }

        sharedViewModel.noteIdEvent.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { noteId ->
                this.noteId = noteId
            }
        }

        sharedViewModel.noteArchivedEvent.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let {
                Snackbar.make(binding.homeCoordinator, it, Snackbar.LENGTH_SHORT)
                    .setAnchorView(binding.fabCreateNote).setAction(R.string.undo) {
                        homeViewModel.redoNoteToActive(noteId)
                    }.show()
            }
        }

        sharedViewModel.noteDeletedEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { content ->
                Snackbar.make(requireView(), content, Snackbar.LENGTH_SHORT).setAction(
                    R.string.undo
                ) {
                    homeViewModel.redoNoteToActive(noteId)
                }.setGestureInsetBottomIgnored(true).setAnchorView(binding.fabCreateNote).show()
            }
        }

        binding.toolBarHome.setOnMenuItemClickListener { menuItem ->
            return@setOnMenuItemClickListener when (menuItem.itemId) {
                R.id.action_searchNote -> {
                    findNavController().navigate(R.id.action_homeNoteFragment_to_searchNoteFragment)
                    true
                }

                R.id.action_note_view_type -> {
                    homeViewModel.toggleNoteLayout()
                    true
                }

                else -> false
            }
        }


        homeViewModel.noteStatusChangeEvent.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).setAction(
                    R.string.undo
                ) {
                    homeViewModel.redoNoteToActive(draggedNote.id)
                }.setAnchorView(binding.fabCreateNote).show()
            }
        }


        homeViewModel.noteItemLayout.observe(viewLifecycleOwner) {
            val noteItemMenu = binding.toolBarHome.menu.findItem(R.id.action_note_view_type)

            when (it) {
                NoteLayout.LIST.name -> { // default is List
                    binding.homeNoteList.layoutManager = LinearLayoutManager(requireContext())
                    noteItemMenu.setIcon(R.drawable.grid_view_icon24px)
                        .setTitle(R.string.note_layout_grid)
                }

                NoteLayout.GRID.name -> {
                    binding.homeNoteList.layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

                    noteItemMenu.setIcon(R.drawable.list_view_icon24px)
                        .setTitle(R.string.note_layout_list)
                }
            }
        }
    }

    private fun navigateToAddEditFragment() {
        val action = HomeNoteFragmentDirections.actionHomeNoteFragmentToActionNoteFragment(
            -1
        )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


