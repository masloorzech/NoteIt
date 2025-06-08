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
import com.example.noteit.data.factory.AttachmentViewModelFactory
import com.example.noteit.data.factory.CategoryViewModelFactory
import com.example.noteit.data.viewModel.TaskViewModel
import com.example.noteit.data.factory.TaskViewModelFactory
import com.example.noteit.data.repository.AttachmentRepository
import com.example.noteit.data.repository.CategoryRepository
import com.example.noteit.data.repository.TaskRepository
import com.example.noteit.data.viewModel.AttachmentViewModel
import com.example.noteit.data.viewModel.CategoryViewModel
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
        } else {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }

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
            Screen(taskViewModel = taskViewModel,
                categoryViewModel = categoryViewModel,
                attachmentViewModel = attachmentViewModel)
        }
    }


}


@Composable
fun Screen(taskViewModel: TaskViewModel, categoryViewModel: CategoryViewModel, attachmentViewModel: AttachmentViewModel) {
    Box(modifier =
        Modifier.background(color = Color(0xFFD9D9D9))
            .fillMaxSize()

    ) {
        MainScreen(taskViewModel,categoryViewModel, attachmentViewModel)
    }
}
