package com.example.todoapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todoapp.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun addTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM task ORDER BY title ASC")
    fun readAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE title LIKE :query OR description LIKE :query")
    fun searchTasks(query: String): LiveData<List<Task>>

    @Query("DELETE FROM task")
    fun deleteAllTasks()

}