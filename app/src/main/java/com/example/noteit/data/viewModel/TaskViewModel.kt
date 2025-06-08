package com.example.noteit.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteit.data.model.Attachment
import com.example.noteit.data.model.Task
import com.example.noteit.data.repository.TaskRepository
import com.example.noteit.notifications.TaskNotificationManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn

class TaskViewModel(application: Application, private val repository: TaskRepository) : AndroidViewModel(application) {

    val allTasks: StateFlow<List<Task>> = repository.allTasks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        allTasks.onEach { tasks ->
            TaskNotificationManager.updateNotifications(getApplication(), tasks)
        }.launchIn(viewModelScope)
    }

    suspend fun getTaskById(id: Int): Task? {
        return repository.getTaskById(id)
    }

    fun markTask(task: Task) = viewModelScope.launch{
        val updatedTask = task.copy(isDone = !task.isDone)
        repository.update(updatedTask)
    }

    suspend fun insert(task: Task): Int {
        return repository.insert(task)
    }

    fun update(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun delete(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }

}
