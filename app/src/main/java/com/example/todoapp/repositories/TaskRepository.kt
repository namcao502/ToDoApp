package com.example.todoapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.todoapp.data.TaskDao
import com.example.todoapp.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    fun readAllTasks(): LiveData<List<Task>>{
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

    fun deleteAllTasks(){
        taskDao.deleteAllTasks()
    }

    fun searchTask(query: String): LiveData<List<Task>>{
        return taskDao.searchTasks(query)
    }

    fun hideCompletedTasks(): LiveData<List<Task>>{
        return taskDao.hideCompletedTasks()
    }

    fun showImportantTasks(): LiveData<List<Task>>{
        return taskDao.showImportantTasks()
    }

    fun sortTitleTask(): LiveData<List<Task>>{
        return taskDao.sortTitleTask()
    }

    fun sortDateTask(): LiveData<List<Task>>{
        return taskDao.sortDateTask()
    }

    fun countNotDoneTasks(): Int{
        return taskDao.countNotDoneTasks()
    }

    fun countImportantTasks(): Int{
        return taskDao.countImportantTasks()
    }

}