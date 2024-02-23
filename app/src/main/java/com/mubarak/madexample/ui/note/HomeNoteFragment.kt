package com.mubarak.madexample.ui.note

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
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
import com.mubarak.madexample.utils.openNavDrawer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeNoteFragment : Fragment() {
    @Inject
    lateinit var todoPreferenceDataStore: TodoPreferenceDataStore

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

        noteItemLayout()

        binding.fabCreateNote.setOnClickListener {
            navigateToAddEditFragment()

        }

        binding.apply {
            toolBarHome.setNavigationOnClickListener {
                requireView().openNavDrawer(requireActivity())
            }


            homeViewModel.getNoteIdEvent.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { content ->
                    navigateToEditNoteFragment(content)
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                homeViewModel.getAllNote.flowWithLifecycle(
                    lifecycle,
                    Lifecycle.State.STARTED,
                ).collect { note ->
                    homeAdapter.submitList(note)
                    binding.homeNoteList.adapter = homeAdapter

                }
            }

            toolBarHome.setOnMenuItemClickListener { menuItem ->
                return@setOnMenuItemClickListener when (menuItem.itemId) {
                    R.id.action_searchNote -> {
                        findNavController().navigate(R.id.action_homeNoteFragment_to_searchNoteFragment)
                        true
                    }

                    R.id.action_note_view_type -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            var isGrid =
                                this@HomeNoteFragment.todoPreferenceDataStore.getNoteItemLayout.first()
                            isGrid = !isGrid

                            this@HomeNoteFragment.todoPreferenceDataStore.setNoteItemLayout(isGrid)

                        }
                        true
                    }

                    else -> false
                }
            }
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or
                ItemTouchHelper.RIGHT) {
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
                    homeViewModel.undoDeletedNote(draggedNote)
                }.show()
            }
        }

    }



    private fun navigateToEditNoteFragment(noteId: String) {
        val action = HomeNoteFragmentDirections.actionHomeNoteFragmentToActionNoteFragment(noteId)
        findNavController().navigate(action)
    }


    private fun navigateToAddEditFragment() {
        val action = HomeNoteFragmentDirections.actionHomeNoteFragmentToActionNoteFragment(
            null
        )
        findNavController().navigate(action)
    }

    private fun noteItemLayout() {
      binding.homeNoteList.setHasFixedSize(true)

        viewLifecycleOwner.lifecycleScope.launch {
            this@HomeNoteFragment.todoPreferenceDataStore.getNoteItemLayout.collect {
                val noteLayout = binding.toolBarHome.menu.findItem(R.id.action_note_view_type)
                if (it) {
                    val staggeredGridLayoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    binding.homeNoteList.layoutManager = staggeredGridLayoutManager

                    noteLayout.setIcon(R.drawable.list_view_icon24px)
                        .setTitle(R.string.note_layout_list)
                } else {
                    binding.homeNoteList.layoutManager = LinearLayoutManager(requireContext())
                    noteLayout.setIcon(R.drawable.grid_view_icon24px)
                        .setTitle(R.string.note_layout_grid)
                }
            }
        }
    }

}