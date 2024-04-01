package com.mubarak.madexample.ui.note

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeItemHelper(
    private val listener: NoteItemAdapter.NoteAdapterListener? = null,
    private val adapter: NoteItemAdapter
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener?.onNoteSwipe(adapter.currentList[viewHolder.bindingAdapterPosition])
    }
}
