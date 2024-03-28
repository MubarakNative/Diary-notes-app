package com.mubarak.madexample.data.sources.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mubarak.madexample.utils.NoteStatus

@Entity("note_table")
data class Note(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("note_id")
    var id: Long,

    @ColumnInfo("Title")
    val title: String,

    @ColumnInfo("Description")
    val description: String,

    @ColumnInfo("note_status")
    val noteStatus: NoteStatus

)
