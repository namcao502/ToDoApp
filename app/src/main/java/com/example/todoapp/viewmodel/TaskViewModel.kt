package com.example.todoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.repositories.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: TaskRepository): ViewModel() {

//    val readAllTasks = repository.readAllTasks().asLiveData()

    fun readAllTasks(): LiveData<List<Task>>{
        return repository.readAllTasks()
    }

    fun addTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(task)
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
        }
    }

    fun deleteAllTasks(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTasks()
        }
    }

    fun updateTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
        }
    }

    fun searchTask(query: String): LiveData<List<Task>>{
        return repository.searchTask(query)
    }

}
