package com.mubarak.madexample.ui.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mubarak.madexample.data.sources.local.model.Note
import com.mubarak.madexample.databinding.NoteListItemBinding

class NoteItemAdapter(
    private val noteAdapterListener: NoteAdapterListener? = null,
    private val noteItemClickListener: NoteItemClickListener? = null
) : ListAdapter<Note, NoteItemAdapter.HomeViewHolder>(diffCallBack) {

    inner class HomeViewHolder(private val binding: NoteListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.apply {
                tvTitle.isVisible = note.title.isNotBlank()
                tvDesc.isVisible = note.description.isNotBlank()
                tvTitle.text = note.title
                tvDesc.text = note.description
                cvParent.setOnClickListener {
                    noteAdapterListener?.onNoteItemClicked(note = note)
                    noteItemClickListener?.onNoteItemClick(note = note)
                }
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

    val touchHelper = ItemTouchHelper(SwipeItemHelper(this.noteAdapterListener, this))


    interface NoteAdapterListener {
        fun onNoteItemClicked(note: Note)
        fun onNoteSwipe(note: Note)
    }

    interface NoteItemClickListener {
        fun onNoteItemClick(note: Note)

    }
}


