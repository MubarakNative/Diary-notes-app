package com.mubarak.madexample.ui.deleted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.mubarak.madexample.R
import com.mubarak.madexample.databinding.FragmentDeletedNoteBinding
import com.mubarak.madexample.ui.SharedViewModel
import com.mubarak.madexample.ui.note.NoteItemAdapter
import com.mubarak.madexample.utils.openNavDrawer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeletedNoteFragment : Fragment() {


    private var _binding: FragmentDeletedNoteBinding? = null
    private val binding: FragmentDeletedNoteBinding get() = _binding!!
    private val deletedNoteViewModel: DeletedNoteViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var adapter: NoteItemAdapter? = null

    private var noteId: Long = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeletedNoteBinding.inflate(layoutInflater, container, false)
        adapter = NoteItemAdapter(noteItemClickListener = deletedNoteViewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.noteIdEvent.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { noteId ->
                this.noteId = noteId
            }
        }

        val emptyTrashMenu = binding.toolbarTrashNote.menu.findItem(R.id.action_delete_all_note)
        deletedNoteViewModel.isEmpty.observe(viewLifecycleOwner) {
            binding.deletedNotePlaceHolder.isVisible = it
            emptyTrashMenu.isVisible= !it
        }

        // called when user restore the note in trash fragment
        sharedViewModel.noteUnRestoreEvent.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { msg ->
                Snackbar.make(binding.deletedNoteCoordinator, msg, Snackbar.LENGTH_SHORT)
                    .setGestureInsetBottomIgnored(true).setAction(R.string.undo) {
                        deletedNoteViewModel.undoUnRestore(noteId)
                    }.show()
            }
        }

        deletedNoteViewModel.onNoteItemClick.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {note->
                val action =
                    DeletedNoteFragmentDirections.actionDeletedNoteFragmentToActionNoteFragment(note.id)
                findNavController().navigate(action)
            }
        }


        binding.toolbarTrashNote.setNavigationOnClickListener {
            requireView().openNavDrawer(requireActivity())
        }

        binding.toolbarTrashNote.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_delete_all_note -> {
                    confirmDeleteAllNote()
                }
            }
            true
        }
        deletedNoteViewModel.getNoteByStatus.observe(viewLifecycleOwner) {
            adapter?.submitList(it)
        }

        binding.deletedNoteList.adapter = adapter
        binding.deletedNoteList.layoutManager = LinearLayoutManager(requireContext())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }

    private fun confirmDeleteAllNote(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.empty_trash_title)
            .setMessage(R.string.empty_trash_msg)
            .setPositiveButton(R.string.delete){ dialog,_->
                deletedNoteViewModel.deleteAllNotes()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel){ dialog,_->
                dialog.dismiss()
            }
            .show()

    }
}

