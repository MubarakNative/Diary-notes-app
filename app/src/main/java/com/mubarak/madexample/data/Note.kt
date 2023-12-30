package com.mubarak.madexample.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("note_table")
data class Note(
    @ColumnInfo("Title")
    val title:String,
    @ColumnInfo("Description")
    val description:String
){
    @ColumnInfo("note_id")
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
