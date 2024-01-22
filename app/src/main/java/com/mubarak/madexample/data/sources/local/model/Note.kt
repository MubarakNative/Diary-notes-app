package com.mubarak.madexample.data.sources.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity("note_table")
data class Note(
    @PrimaryKey
    @ColumnInfo("note_id")
    var id:String = UUID.randomUUID().toString(),

    @ColumnInfo("Title")
    var title:String,

    @ColumnInfo("Description")
    var description:String
)
