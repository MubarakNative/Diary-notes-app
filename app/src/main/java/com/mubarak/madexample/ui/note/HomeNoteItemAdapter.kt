package com.mubarak.madexample.ui.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.databinding.NoteListItemBinding

class HomeNoteItemAdapter(
    private val homeNoteViewModel: HomeNoteViewModel
): ListAdapter<Note, HomeNoteItemAdapter.HomeViewHolder>(diffCallBack) {

  inner class HomeViewHolder(private val binding: NoteListItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.apply {
                tvTitle.text = note.title
                tvDesc.text = note.description
                homeViewModel = homeNoteViewModel
                this.note = note
                executePendingBindings()

            }
        }
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


    }

   /* private var onClick: ((note: Note?) -> Unit)? = null

    fun setOnClickListener(on: ((note: Note?) -> Unit)) {
        onClick = on
    }*/

}


