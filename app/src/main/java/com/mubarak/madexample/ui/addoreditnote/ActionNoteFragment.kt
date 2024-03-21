package com.mubarak.madexample.ui.addoreditnote

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mubarak.madexample.R
import com.mubarak.madexample.data.sources.datastore.TodoPreferenceDataStore
import com.mubarak.madexample.databinding.FragmentActionNoteBinding
import com.mubarak.madexample.ui.SharedViewModel
import com.mubarak.madexample.utils.hideSoftKeyboard
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
    private val sharedViewModel: SharedViewModel by activityViewModels()

    @Inject
    lateinit var todoPreferenceDataStore: TodoPreferenceDataStore

    private val args: ActionNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, viewGroup: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActionNoteBinding.inflate(
            layoutInflater,
            viewGroup, false
        )
        binding.actionViewModel = actionNoteViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         requireActivity().onBackPressedDispatcher.addCallback(this) {
            actionNoteViewModel.saveAndExit()
        }

        binding.toolbarCreateNote.setNavigationOnClickListener {
            view.hideSoftKeyboard()
            actionNoteViewModel.saveAndExit()
        }

        actionNoteViewModel.noteDeletedEvent.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled().let {
                findNavController().popBackStack()
                sharedViewModel.onNoteDeleted()
            }
        }

        /**for handling overlapping in landscape mode*/
        ViewCompat.setOnApplyWindowInsetsListener(binding.actionCoordinator) { v, insets ->
            val windowInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            v.updatePadding(
                right = windowInsets.right,
                top = windowInsets.top,
                bottom = windowInsets.bottom,
                left = windowInsets.left
            )
            WindowInsetsCompat.CONSUMED
        }

        actionNoteViewModel.backPressEvent.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
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

        actionNoteViewModel.checkIsNewNoteOrExistingNote(args.noteId)

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

}
