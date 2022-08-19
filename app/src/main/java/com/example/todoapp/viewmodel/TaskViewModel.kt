package com.example.todoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.repositories.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: TaskRepository): ViewModel() {

//    val readAllTasks = repository.readAllTasks().asLiveData()

    private val taskEventChannel = Channel<TaskEvent>()
    val taskEvent = taskEventChannel.receiveAsFlow()

    fun readAllTasks(): LiveData<List<Task>>{
        return repository.readAllTasks()
    }

    fun hideCompletedTasks(): LiveData<List<Task>>{
        return repository.hideCompletedTasks()
    }

    fun showImportantTasks(): LiveData<List<Task>>{
        return repository.showImportantTasks()
    }

    fun sortTitleTask(): LiveData<List<Task>>{
        return repository.sortTitleTask()
    }

    fun sortDateTask(): LiveData<List<Task>>{
        return repository.sortDateTask()
    }

    fun addTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(task)
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
            taskEventChannel.send(TaskEvent.ShowUndoDeleteTaskMessage(task))
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

    fun undoDeleteTask(task: Task) {
        addTask(task)
    }

    fun countImportantTasks(): Int{
        return repository.countImportantTasks()
    }

    fun countNotDoneTasks(): Int{
        return repository.countNotDoneTasks()
    }

    sealed class TaskEvent{
        data class ShowUndoDeleteTaskMessage(val task: Task): TaskEvent()
    }

}
