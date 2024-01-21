package com.mubarak.madexample.ui.sortdialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mubarak.madexample.databinding.SortDialogFragmentBinding

class SortDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?):Dialog {

        val binding = SortDialogFragmentBinding.inflate(layoutInflater,null,false)


       val dialog = MaterialAlertDialogBuilder(requireContext()).apply {
           setView(binding.root)
            setTitle("Sort options")
           setPositiveButton("OK") { _, _ ->

               }
           setNegativeButton("Cancel"){ _,_ ->

           }


           }.show()

        return dialog
    }




}
