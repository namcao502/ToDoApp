package com.example.todoapp.repositories

import com.example.todoapp.data.TaskDao
import com.example.todoapp.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    fun readAllTasks(): Flow<List<Task>>{
        return taskDao.readAllTasks()
    }

    suspend fun addTask(task: Task) {
        taskDao.addTask(task)
    }

    suspend fun updateTask(task: Task){
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task){
        taskDao.deleteTask(task)
    }

    fun searchTask(query: String): Flow<List<Task>>{
        return taskDao.searchTasks(query)
    }

}