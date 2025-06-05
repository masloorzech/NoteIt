package com.example.noteit.screens

import android.content.Intent
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteit.CreateTaskActivity
import com.example.noteit.components.FloatingFrame
import com.example.noteit.components.SwipeableTaskItem
import com.example.noteit.data.viewModel.CategoryViewModel
import com.example.noteit.data.viewModel.TaskViewModel


@Composable
fun MainScreen(taskViewModel: TaskViewModel, categoryViewModel: CategoryViewModel) {

    val listState = rememberLazyListState()

    val categoryList by categoryViewModel.categories.collectAsState()

    val taskList by taskViewModel.allTasks.collectAsState()

    val categoryMap = remember(categoryList) {
        categoryList.associate { it.id to it.name }
    }

    var selectedCategory by remember { mutableStateOf("") }

    val context = LocalContext.current

    val selectedForDeletion = remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()
        .padding(top =48.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("NoteIt", fontSize = 32.sp)

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

                    Box(contentAlignment = Alignment.TopCenter)
                    {
                        FloatingFrame(
                            elevation = if (selectedCategory == category.name) 0.dp else 8.dp,
                            modifier = Modifier
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = {
                                            if (selectedCategory == category.name) {
                                                selectedCategory = ""
                                                selectedForDeletion.value = null
                                            } else {
                                                selectedCategory = category.name
                                                selectedForDeletion.value = null
                                            }
                                        },
                                        onLongPress = {
                                            selectedForDeletion.value = category.name
                                        }
                                    )
                                }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = category.name)

                                AnimatedVisibility(
                                    visible = selectedForDeletion.value == category.name,
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.Red,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable {
                                                categoryViewModel.deleteCategory(category)
                                                if (selectedCategory == category.name) {
                                                    selectedCategory = ""
                                                }
                                                selectedForDeletion.value = null
                                            }
                                    )
                                }
                            }
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

                        if (selectedCategory == "" || categoryMap[task.categoryId] == selectedCategory) {
                            key(task.id, task.isDone) {
                                SwipeableTaskItem(
                                    task = task,
                                    categoryMap = categoryMap,
                                    selectedCategory = selectedCategory,
                                    onSwipeRight = {
                                        taskViewModel.markTask(task)
                                    },
                                    onSwipeLeft = {
                                        taskViewModel.delete(task)
                                    },
                                    onClick = {
                                        val intent = Intent(context, CreateTaskActivity::class.java)
                                        intent.putExtra("TaskId", task.id)
                                        context.startActivity(intent)
                                    }
                                )
                            }
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
