package com.mubarak.madexample.ui.archive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mubarak.madexample.R
import com.mubarak.madexample.databinding.FragmentArchiveNoteBinding
import com.mubarak.madexample.ui.SharedViewModel
import com.mubarak.madexample.ui.note.HomeNoteViewModel
import com.mubarak.madexample.ui.note.NoteItemAdapter
import com.mubarak.madexample.utils.openNavDrawer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArchiveNoteFragment : Fragment() {


    private var _binding: FragmentArchiveNoteBinding? = null
    private val binding: FragmentArchiveNoteBinding get() = _binding!!

    private val archiveNoteViewModel: ArchiveNoteViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val homeNoteViewModel: HomeNoteViewModel by viewModels()
    private lateinit var adapter: NoteItemAdapter

    private var noteId: Long = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchiveNoteBinding.inflate(layoutInflater, container, false)

        adapter = NoteItemAdapter(noteItemClickListener = archiveNoteViewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sharedViewModel.noteIdEvent.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { noteId ->
                this.noteId = noteId
            }
        }

        archiveNoteViewModel.isEmpty.observe(viewLifecycleOwner) {
            binding.archivePlaceHolder.isVisible = it
        }

        archiveNoteViewModel.onNoteItemClick.observe(viewLifecycleOwner) { note ->

            note.getContentIfNotHandled()?.let {
                val directions =
                    ArchiveNoteFragmentDirections.actionArchiveNoteFragmentToActionNoteFragment(it.id)
                findNavController().navigate(directions)
            }

        }

        archiveNoteViewModel.getNoteByStatus.observe(
            viewLifecycleOwner
        ) {
            adapter.submitList(it)
        }

        sharedViewModel.noteUnArchivedEvent.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let {
                Snackbar.make(binding.archiveCoordinator, it, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.undo) {
                        archiveNoteViewModel.undoUnArchive(noteId)
                    }.show()
            }
        }

        sharedViewModel.noteDeletedEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { content ->
                Snackbar.make(requireView(), content, Snackbar.LENGTH_SHORT).setAction(
                    R.string.undo
                ) {
                    homeNoteViewModel.updateNoteStatus(noteId)
                }.show()
            }
        }

        binding.toolbarArchiveNote.setNavigationOnClickListener {
            requireView().openNavDrawer(requireActivity())
        }

        sharedViewModel.noteArchivedEvent.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let {
                Snackbar.make(binding.archiveCoordinator, it, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.undo) {
                        archiveNoteViewModel.undoUnArchive(noteId)
                    }.show()
            }
        }

        sharedViewModel.noteMovedToTrashEvent.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let {
                Snackbar.make(binding.archiveCoordinator, it, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.undo) {
                        archiveNoteViewModel.undoUnArchive(noteId)
                    }.show()
            }
        }

        binding.archiveNoteList.setHasFixedSize(true)
        binding.archiveNoteList.adapter = adapter
        binding.archiveNoteList.layoutManager = LinearLayoutManager(requireContext())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}