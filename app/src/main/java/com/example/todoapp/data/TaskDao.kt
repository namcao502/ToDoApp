package com.example.todoapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todoapp.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM task ORDER BY title ASC")
    fun readAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE title LIKE :query OR date LIKE :query")
    fun searchTasks(query: String): LiveData<List<Task>>

    @Query("DELETE FROM task")
    fun deleteAllTasks()

    @Query("SELECT * FROM task WHERE done != 1")
    fun hideCompletedTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE important == 1")
    fun showImportantTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task ORDER BY title ASC")
    fun sortTitleTask(): LiveData<List<Task>>

    @Query("SELECT * FROM task ORDER BY title DESC")
    fun sortDateTask(): LiveData<List<Task>>

    @Query("SELECT COUNT(id) FROM task WHERE done == 0")
    fun countNotDoneTasks(): Int

    @Query("SELECT COUNT(id) FROM task WHERE important == 1")
    fun countImportantTasks(): Int

}