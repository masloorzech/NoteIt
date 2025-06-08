package com.example.noteit.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.noteit.CreateTaskActivity
import com.example.noteit.R
import com.example.noteit.components.FloatingFrame
import com.example.noteit.components.NotificationTimeSelector
import com.example.noteit.components.SwipeableCategoryItem
import com.example.noteit.components.SwipeableTaskItem
import com.example.noteit.data.model.Task
import com.example.noteit.data.viewModel.AttachmentViewModel
import com.example.noteit.data.viewModel.CategoryViewModel
import com.example.noteit.data.viewModel.TaskViewModel
import com.example.noteit.helpers.NotificationPreferences
import com.example.noteit.notifications.TaskNotificationManager
import com.example.noteit.ui.theme.Manuale
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.apache.commons.text.similarity.LevenshteinDistance


fun fuzzyFilter(tasks: List<Task>, query: String): List<Task> {
    if (query.isBlank()) return tasks

    val distance = LevenshteinDistance()

    return tasks
        .map { task ->
            val combined = listOf(
                task.title,
                task.description,
                task.categoryId?.toString() ?: ""
            ).joinToString(" ").lowercase()

            val score = distance.apply(combined, query.lowercase())

            task to score
        }
        .sortedBy { it.second }
        .map { it.first }
}

@Composable
fun MainScreen(taskViewModel: TaskViewModel, categoryViewModel: CategoryViewModel, attatchmentViewModel: AttachmentViewModel) {

    val listState = rememberLazyListState()

    val rowState = rememberLazyListState()

    val categoryList by categoryViewModel.categories.collectAsState()

    val taskList by taskViewModel.allTasks.collectAsState()

    val categoryMap = remember(categoryList) {
        categoryList.associate { it.id to it.name }
    }

    val coroutineScope = rememberCoroutineScope()

    var searchBar by remember { mutableStateOf("") }

    var selectedCategories by remember {  mutableStateOf(listOf<String>())  }

    val context = LocalContext.current

    var showDoneTasks by remember { mutableStateOf(true) }
    
    var sortByTime by remember { mutableStateOf(false) }

    val filteredTasks = remember(taskList, searchBar, showDoneTasks, sortByTime) {
        val visibleTasks = taskList.filter { showDoneTasks || !it.isDone }
        val matchedTasks = fuzzyFilter(visibleTasks, searchBar)

        if (sortByTime) {
            matchedTasks.sortedBy { it.dueAt }
        } else {
            matchedTasks
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = 48.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("NoteIt", fontSize = 64.sp,
                style = TextStyle(
                fontFamily = Manuale ,
                    shadow = Shadow(
                        color = Color(0xFFFFFFFF),
                    offset = Offset(0f, 8f),
                    blurRadius = 8f
                )),
                color = Color.White
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                ) {
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .weight(3f),
                    contentAlignment = Alignment.Center
                ) {
                    FloatingFrame(
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center
                        ) {
                            TextField(
                                value = searchBar,
                                leadingIcon = {
                                    Icon(Icons.Default.Search, contentDescription = "search")
                                },
                                placeholder = {
                                    Text(
                                        "Search",
                                        fontSize = 18.sp,
                                        color = Color.Black,
                                        style = TextStyle(fontFamily = Manuale)
                                    )
                                },
                                onValueChange = {
                                    searchBar = it
                                },
                                textStyle = TextStyle(
                                    fontFamily = Manuale,
                                    fontSize = 18.sp
                                ),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    errorContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                            )
                        }
                    }
                }
                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                )
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                ) {
                    FloatingFrame {
                        IconButton(onClick = {
                            showDoneTasks = !showDoneTasks
                        }) {
                            if (showDoneTasks) {
                                Icon(
                                    painter = painterResource(id = R.drawable.outline_visibility_24),
                                    contentDescription = "Show done",
                                    tint = Color.Black
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.outline_visibility_off_24),
                                    contentDescription = "Hide done",
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                }
            }

            Row(
                Modifier.padding(start = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.width(50.dp)
                        .aspectRatio(1f)
                ) {
                    FloatingFrame(
                        elevation = if (sortByTime) 0.dp else 8.dp
                    ) {
                        IconButton(onClick = {
                            sortByTime = !sortByTime
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_timeline_24),
                                contentDescription = "sort by time",
                                tint = Color.Black
                            )
                        }
                    }
                }
                Box() {
                    LazyRow(
                        state = rowState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(start = 16.dp)
                    ) {
                        items(categoryList.size) { index ->

                            val category = categoryList[index]

                            key(category.id) {
                                Box(contentAlignment = Alignment.TopCenter)
                                {
                                    SwipeableCategoryItem(
                                        category = category,
                                        selectedCategories = selectedCategories,
                                        onSwipeUp = {
                                            categoryViewModel.deleteCategory(category)
                                            selectedCategories =
                                                selectedCategories - category.name
                                        },
                                        onClick = {
                                            selectedCategories =
                                                if (selectedCategories.contains(category.name)) {
                                                    selectedCategories - category.name
                                                } else {
                                                    selectedCategories + category.name
                                                }
                                        })
                                }
                            }
                        }
                    }
                    if (rowState.firstVisibleItemScrollOffset > 0 || rowState.firstVisibleItemIndex > 0) {
                        Box(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .matchParentSize()
                                .zIndex(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(64.dp)
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(Color(0xFFD9D9D9), Color.Transparent)
                                        )
                                    )
                            )
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
                    items(filteredTasks.size) { index ->
                        var task = filteredTasks[index]
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
                                        val intent =
                                            Intent(context, CreateTaskActivity::class.java)
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

                val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                val isEndReached = lastVisible?.index == listState.layoutInfo.totalItemsCount - 1 &&
                        lastVisible.offset + lastVisible.size <= listState.layoutInfo.viewportEndOffset

                if (!isEndReached) {
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
        NotificationTimeSelector(context = context) { selectedMinutes ->

            NotificationPreferences.saveNotificationTime(context, selectedMinutes)

            coroutineScope.launch {

                TaskNotificationManager.updateNotifications(context, taskList)

            }
        }

    }

}
