package com.example.noteit.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteit.CreateTaskActivity
import com.example.noteit.components.FloatingFrame
import com.example.noteit.components.SwipeableCategoryItem
import com.example.noteit.components.SwipeableTaskItem
import com.example.noteit.data.viewModel.CategoryViewModel
import com.example.noteit.data.viewModel.TaskViewModel
import com.example.noteit.ui.theme.Manuale


@Composable
fun MainScreen(taskViewModel: TaskViewModel, categoryViewModel: CategoryViewModel) {

    val listState = rememberLazyListState()

    val categoryList by categoryViewModel.categories.collectAsState()

    val taskList by taskViewModel.allTasks.collectAsState()

    val categoryMap = remember(categoryList) {
        categoryList.associate { it.id to it.name }
    }

    var selectedCategories by remember {  mutableStateOf(listOf<String>())  }

    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()
        .padding(top =48.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("NoteIt", fontSize = 32.sp, style = TextStyle(
                fontFamily = Manuale
            )
            )

            Row(
                modifier = Modifier
                    .padding(16.dp, 16.dp),

                ) {
                FloatingFrame {
                    Text("Zawartość panelu")
                    Button(onClick = {}) {
                        Text("Akcja")
                    }
                }
                FloatingFrame {

                }
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 16.dp)
            ) {
                items(categoryList.size) { index ->

                    val category = categoryList[index]

                    key(category.id) {
                        Box(contentAlignment = Alignment.TopCenter)
                        {
                            SwipeableCategoryItem(category= category, selectedCategories = selectedCategories,
                                onSwipeUp = {
                                    categoryViewModel.deleteCategory(category)
                                    selectedCategories = selectedCategories - category.name
                                },
                                onClick = {
                                    selectedCategories = if (selectedCategories.contains(category.name)) {
                                        selectedCategories - category.name
                                    } else {
                                        selectedCategories + category.name
                                    }
                                })
                        }
                    }
                }
            }

            Box() {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(taskList.size) { index ->

                        var task = taskList[index]
                            key(task.id, task.isDone) {
                                SwipeableTaskItem(
                                    task = task,
                                    categoryMap = categoryMap,
                                    selectedCategories = selectedCategories,
                                    onSwipeRight = {
                                        taskViewModel.markTask(task)
                                    },
                                    onSwipeLeft = {
                                        taskViewModel.delete(task)
                                    },
                                    onClick = {
                                        val intent = Intent(context, CreateTaskActivity::class.java)
                                        intent.putExtra("TaskId", task.id.toString())
                                        context.startActivity(intent)
                                    }
                                )
                            }
                        }
                }

                if (listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFFD9D9D9), Color.Transparent)
                                )
                            )
                            .align(Alignment.TopCenter)
                    )
                }
                if (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index != listState.layoutInfo.totalItemsCount - 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0xFFD9D9D9))
                                )
                            )
                            .align(Alignment.BottomCenter)
                    )
                }
            }


        }
        FloatingActionButton(
            onClick = {
                val intent = Intent(context, CreateTaskActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
                .size(64.dp),
            containerColor = Color(0xFFE0E0E0),
            contentColor = Color.Black
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add"
            )
        }
    }

}
