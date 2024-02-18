package com.mubarak.madexample.ui.addoreditnote

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.mubarak.madexample.R
import com.mubarak.madexample.data.sources.datastore.TodoPreferenceDataStore
import com.mubarak.madexample.databinding.FragmentActionNoteBinding
import com.mubarak.madexample.utils.onUpButtonClick
import com.mubarak.madexample.utils.showSoftKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ActionNoteFragment : Fragment() {

    private lateinit var binding: FragmentActionNoteBinding
    private val actionNoteViewModel: ActionNoteViewModel by viewModels()

    @Inject
    lateinit var todoPreferenceDataStore: TodoPreferenceDataStore

    private val args: ActionNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, viewGroup: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(
            R.layout.fragment_action_note,
            viewGroup,
            false
        )

        binding = FragmentActionNoteBinding.bind(root).apply {
            actionViewModel = actionNoteViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarCreateNote.onUpButtonClick()

        val toolBarMenu = binding.toolbarCreateNote.menu
        val sendNoteMenuItem = toolBarMenu.findItem(R.id.action_send_note)
        val deleteNoteMenuItem = toolBarMenu.findItem(R.id.action_delete_note)
        val makeCopyMenuItem = toolBarMenu.findItem(R.id.action_copy_note)

        if (args.noteId == null) {
            sendNoteMenuItem.isVisible = false
            deleteNoteMenuItem.isVisible = false
            makeCopyMenuItem.isVisible = false
        } else {
            sendNoteMenuItem.isVisible = true
            deleteNoteMenuItem.isVisible = true
            makeCopyMenuItem.isVisible = true

        }

        setUpNavigation()

        actionNoteViewModel.noteDeletedEvent.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled().let {
                Snackbar.make(binding.root, "Note deleted", Snackbar.LENGTH_SHORT).show()
                findNavController().popBackStack()

            }
        }

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

                else -> false
            }
        }

        // pass this args from HomeNoteFragment if null means create once else update
        actionNoteViewModel.checkIsNewNoteOrExistingNote(args.noteId)

        binding.etAddTitle.showSoftKeyboard(binding.etAddTitle)


        actionNoteViewModel.snackBarEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { content ->
                Snackbar.make(binding.root, content, Snackbar.LENGTH_SHORT).show()
            }
        }


    }

    private fun setUpNavigation() {
        actionNoteViewModel.noteUpdateEvent.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
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


}
