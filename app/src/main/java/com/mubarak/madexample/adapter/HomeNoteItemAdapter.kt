package com.mubarak.madexample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mubarak.madexample.data.Note
import com.mubarak.madexample.databinding.NoteListItemBinding
import com.mubarak.madexample.presenter.note.HomeNoteFragmentDirections
import hilt_aggregated_deps._com_mubarak_madexample_presenter_note_HomeNoteViewModel_HiltModules_BindsModule

class HomeNoteItemAdapter : ListAdapter<Note, HomeNoteItemAdapter.HomeViewHolder>(
    diffCallBack
) {


    class HomeViewHolder(private val binding: NoteListItemBinding) :

        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.apply {
                tvTitle.text = note.title
                tvDesc.text = note.description
                root.setOnClickListener {

                }
            }
        }

        val parent = binding.root
    }

    companion object {
        val diffCallBack = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Note, newItem: Note) =
                oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {

        return HomeViewHolder(
            NoteListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem = getItem(position)

        holder.bind(currentItem)
        holder.parent.setOnClickListener {


           val action = HomeNoteFragmentDirections.actionHomeNoteFragmentToActionNoteFragment(
               currentItem.title,
               currentItem.description,
               currentItem.id
            )
            Navigation.findNavController(it).navigate(action)
            onClick?.let {
                it(currentItem)
            }

        }
    }

    private var onClick: ((note: Note?) -> Unit)? = null

    fun setOnClickListener(on: ((note: Note?) -> Unit)) {
        onClick = on
    }


}


