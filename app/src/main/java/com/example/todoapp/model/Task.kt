package com.example.todoapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "task")
data class Task(
    val title: String,
    val description: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int
): Parcelable