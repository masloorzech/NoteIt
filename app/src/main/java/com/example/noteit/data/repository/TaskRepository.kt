package com.example.noteit.data.repository

import com.example.noteit.data.dao.TaskDao
import com.example.noteit.data.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun insert(task: Task) : Int {
        return taskDao.insert(task).toInt()
    }

    suspend fun update(task: Task) = taskDao.update(task)

    suspend fun delete(task: Task) = taskDao.delete(task)

    suspend fun getTaskById(id : Int) = taskDao.getTaskById(id)
}
