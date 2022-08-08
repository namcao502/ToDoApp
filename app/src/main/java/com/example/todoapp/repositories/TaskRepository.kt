package com.example.todoapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.todoapp.data.TaskDao
import com.example.todoapp.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    fun readAllTasks(): LiveData<List<Task>>{
        return taskDao.readAllTasks().asLiveData()
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

    fun deleteAllTasks(){
        taskDao.deleteAllTasks()
    }

    fun searchTask(query: String): LiveData<List<Task>>{
        return taskDao.searchTasks(query).asLiveData()
    }

}