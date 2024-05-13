package com.mubarak.madexample.ui.addoreditnote

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mubarak.madexample.R
import com.mubarak.madexample.data.sources.datastore.TodoPreferenceDataStore
import com.mubarak.madexample.databinding.FragmentActionNoteBinding
import com.mubarak.madexample.ui.SharedViewModel
import com.mubarak.madexample.ui.reminders.RemindersFragment
import com.mubarak.madexample.utils.NoteStatus
import com.mubarak.madexample.utils.hideSoftKeyboard
import com.mubarak.madexample.utils.showSoftKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ActionNoteFragment : Fragment() {

    private var _binding: FragmentActionNoteBinding? = null
    private val binding: FragmentActionNoteBinding get() = _binding!!
    private val actionNoteViewModel: ActionNoteViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    @Inject
    lateinit var todoPreferenceDataStore: TodoPreferenceDataStore

    private val args: ActionNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, viewGroup: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActionNoteBinding.inflate(
            layoutInflater,
            viewGroup, false
        )
        binding.actionViewModel = actionNoteViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionNoteViewModel.noteStatus.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { noteStatus ->

                if (noteStatus == NoteStatus.TRASH) {
                    binding.etAddTitle.isEnabled = false
                    binding.etAddDesc.isEnabled = false
                } else {
                    binding.etAddTitle.isEnabled = true
                    binding.etAddDesc.isEnabled = true
                }
                sharedViewModel.onNoteStatusChanges(noteStatus)
                changeMenuItemForNoteStatus(noteStatus)
            }
        }

        actionNoteViewModel.checkIsNewNoteOrExistingNote(args.noteId)


        binding.toolbarCreateNote.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_send_note -> {
                    getNoteTitle()
                    true
                }

                R.id.action_delete_note -> {
                    actionNoteViewModel.deleteNote()
                    true
                }

                R.id.action_copy_note -> {
                    actionNoteViewModel.createCopyNote(args.noteId)
                    true
                }

                R.id.action_note_status -> {
                    actionNoteViewModel.onNoteStatusChange()
                    true
                }

                else -> false
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            actionNoteViewModel.saveAndExit()
        }

        binding.toolbarCreateNote.setNavigationOnClickListener {
            view.hideSoftKeyboard()
            actionNoteViewModel.saveAndExit()
        }

        actionNoteViewModel.backPressEvent.observe(viewLifecycleOwner) {
            view.hideSoftKeyboard()
            findNavController().popBackStack()
        }

        actionNoteViewModel.noteIdEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { noteId ->
                sharedViewModel.getNoteId(noteId)
            }
        }

        actionNoteViewModel.noteArchivedEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                sharedViewModel.onNoteArchived()
            }
        }

        actionNoteViewModel.noteMovedToTrashEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                sharedViewModel.onNoteMovedToTrash()
            }
        }

        actionNoteViewModel.noteUnArchiveEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                sharedViewModel.onNoteUnArchived()
            }
        }

        actionNoteViewModel.noteUnRestoreEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                sharedViewModel.onNoteUnRestore()
            }
        }

        actionNoteViewModel.noteDeletedEvent.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled().let {
                view.hideSoftKeyboard()
                sharedViewModel.onNoteDeleted()
            }
        }

        actionNoteViewModel.noteDeleteConfirmationEvent.observe(viewLifecycleOwner) {
            confirmNoteDelete()
        }

        binding.etAddTitle.showSoftKeyboard(binding.etAddTitle)


        actionNoteViewModel.snackBarEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                sharedViewModel.onBlankNote()
            }
        }

    }


    private fun sendNote(noteTitle: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, noteTitle)
        }
        startActivity(Intent.createChooser(intent, "Sent Note to:"))

    }

    private fun getNoteTitle() {
        viewLifecycleOwner.lifecycleScope.launch {
            sendNote(
                actionNoteViewModel.title.first()
            )
        }
    }

    private fun changeMenuItemForNoteStatus(status: NoteStatus) {
        val menu = binding.toolbarCreateNote.menu

        val menuItem = menu.findItem(R.id.action_note_status)
        when (status) {
            NoteStatus.ACTIVE -> {
                menuItem.setIcon(R.drawable.archive_icon24px)
                menuItem.setTitle(R.string.archive)
            }

            NoteStatus.ARCHIVE -> {
                menuItem.setIcon(R.drawable.unarchive_icon24px)
                menuItem.setTitle(R.string.unarchive)
            }

            NoteStatus.TRASH -> {
                menuItem.setIcon(R.drawable.restore_from_trash_icon24px)
                menuItem.setTitle(R.string.restore)
            }
        }

        val isTrash = status == NoteStatus.TRASH
        menu.findItem(R.id.action_send_note).isVisible = !isTrash
        menu.findItem(R.id.action_copy_note).isVisible = !isTrash
        menu.findItem(R.id.action_delete_note).setTitle(
            if (isTrash) {
                R.string.delete_forever
            } else {
                R.string.delete
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun confirmNoteDelete() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.confirm_note_delete_msg)
            .setPositiveButton(
                R.string.delete
            ) { dialog, _ ->
                actionNoteViewModel.deleteNotePermanently()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}
