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

        // DEBUG
        GlobalScope.launch {
            db.clearAllTables()
            val cat1Id = db.categoryDao().insert(Category(name = "Praca")).toInt()
            val cat2Id = db.categoryDao().insert(Category(name = "Dom")).toInt()
            val cat3Id = db.categoryDao().insert(Category(name = "Hobby")).toInt()

            db.taskDao().insert(
                Task(
                    title = "Zadanie do pracy",
                    description = "Zrobić raport miesięczny",
                    createdAt = System.currentTimeMillis(),
                    dueAt = System.currentTimeMillis() + 86400000,
                    isDone = false,
                    hasNotification = true,
                    categoryId = cat1Id
                )
            )

            db.taskDao().insert(
                Task(
                    title = "Sprzątanie",
                    description = "Odkurzyć pokój i umyć podłogi",
                    createdAt = System.currentTimeMillis(),
                    dueAt = System.currentTimeMillis() + 2 * 86400000,
                    isDone = true,
                    hasNotification = false,
                    categoryId = cat2Id
                )
            )

            db.taskDao().insert(
                Task(
                    title = "Pomalować figurki",
                    description = "Dokończyć malowanie krasnoludów",
                    createdAt = System.currentTimeMillis(),
                    dueAt = System.currentTimeMillis() + 3 * 86400000,
                    isDone = false,
                    hasNotification = true,
                    categoryId = cat3Id
                )
            )
        }

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
        MainScreen(taskViewModel = taskViewModel,categoryViewModel=categoryViewModel)
    }
}
