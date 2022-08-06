package com.example.todoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapp.model.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class Database: RoomDatabase() {

    abstract fun taskDao(): TaskDao

}