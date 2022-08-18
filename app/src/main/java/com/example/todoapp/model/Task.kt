package com.example.todoapp.model

import android.os.Parcelable
import android.text.format.DateFormat
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "task")
data class Task(
    val title: String,
    val date: String,
    val important: Boolean = false,
    var done: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Int
): Parcelable