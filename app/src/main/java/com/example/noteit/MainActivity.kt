package com.example.noteit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.example.noteit.screens.MainScreen
import com.example.noteit.data.DatabaseProvider
import com.example.noteit.data.factory.CategoryViewModelFactory
import com.example.noteit.data.viewModel.TaskViewModel
import com.example.noteit.data.factory.TaskViewModelFactory
import com.example.noteit.data.model.Category
import com.example.noteit.data.model.Task
import com.example.noteit.data.repository.CategoryRepository
import com.example.noteit.data.repository.TaskRepository
import com.example.noteit.data.viewModel.CategoryViewModel
import com.example.noteit.screens.CreateTaskScreen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = DatabaseProvider.getDatabase(applicationContext)

        val taskRepository = TaskRepository(db.taskDao())
        val taskViewModelFactory = TaskViewModelFactory(taskRepository)
        val taskViewModel = ViewModelProvider(this, taskViewModelFactory)[TaskViewModel::class.java]

        val categoryRepository = CategoryRepository(db.categoryDao())
        val categoryViewModelFactory = CategoryViewModelFactory(categoryRepository)
        val categoryViewModel = ViewModelProvider(this, categoryViewModelFactory)[CategoryViewModel::class.java]

        setContent {
            Screen(taskViewModel = taskViewModel, categoryViewModel=categoryViewModel)
        }
    }

}


@Composable
fun Screen(taskViewModel: TaskViewModel, categoryViewModel: CategoryViewModel) {
    Box(modifier =
        Modifier.background(color = Color(0xFFD9D9D9))
            .fillMaxSize()

    ) {
        CreateTaskScreen()
    }
}
