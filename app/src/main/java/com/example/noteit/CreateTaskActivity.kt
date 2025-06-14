package com.example.noteit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.noteit.data.DatabaseProvider
import com.example.noteit.data.factory.AttachmentViewModelFactory
import com.example.noteit.data.factory.CategoryViewModelFactory
import com.example.noteit.data.factory.TaskViewModelFactory
import com.example.noteit.data.repository.AttachmentRepository
import com.example.noteit.data.repository.CategoryRepository
import com.example.noteit.data.repository.TaskRepository
import com.example.noteit.data.viewModel.AttachmentViewModel
import com.example.noteit.data.viewModel.CategoryViewModel
import com.example.noteit.data.viewModel.TaskViewModel
import com.example.noteit.screens.CreateTaskScreen

class CreateTaskActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val taskId = intent.getStringExtra("TaskId")

        val db = DatabaseProvider.getDatabase(applicationContext)

        val taskRepository = TaskRepository(db.taskDao())
        val taskViewModelFactory = TaskViewModelFactory(application = application, repository = taskRepository)
        val taskViewModel = ViewModelProvider(this, taskViewModelFactory)[TaskViewModel::class.java]

        val categoryRepository = CategoryRepository(db.categoryDao())
        val categoryViewModelFactory = CategoryViewModelFactory(categoryRepository)
        val categoryViewModel = ViewModelProvider(this, categoryViewModelFactory)[CategoryViewModel::class.java]

        val attachmentRepository = AttachmentRepository(db.attachmentDao())
        val attachmentViewModelFactory = AttachmentViewModelFactory(attachmentRepository)
        val attachmentViewModel = ViewModelProvider(this, attachmentViewModelFactory)[AttachmentViewModel::class.java]

        setContent {
            CreateTaskScreen(taskId?.toInt(),categoryViewModel,taskViewModel, attachmentViewModel)
        }
    }
}
