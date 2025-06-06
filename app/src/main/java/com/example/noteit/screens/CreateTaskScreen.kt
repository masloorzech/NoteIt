package com.example.noteit.screens

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteit.CreateTaskActivity
import com.example.noteit.R
import com.example.noteit.components.FloatingFrame
import com.example.noteit.components.SwipeableCategoryItem
import com.example.noteit.components.SwipeableTaskItem
import com.example.noteit.data.model.Task
import com.example.noteit.data.viewModel.CategoryViewModel
import com.example.noteit.data.viewModel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CreateTaskScreen(
    taskId: Int?,
    categoryViewModel: CategoryViewModel,
    taskViewModel: TaskViewModel
) {

    var task by remember { mutableStateOf<Task?>(null) }

    LaunchedEffect(taskId) {
        if (taskId != null) {
            task = taskViewModel.getTaskById(taskId)
        }
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDateTime by remember { mutableStateOf("") }
    var creationDate by remember { mutableStateOf("") }
    var creationTime by remember { mutableStateOf("") }

    LaunchedEffect(task) {
        task?.let {
            title = it.title
            description = it.description
            selectedDateTime = it.dueAt.toString()
            creationDate
        }
    }

    val allCategories by categoryViewModel.categories.collectAsState()

    val scrollState = rememberScrollState()

    var isNotificationEnabled by remember { mutableStateOf(false) }

    var notififationOn by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val calendar = Calendar.getInstance()

    val dateTimeFormat = remember { SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()) }

    val datetime = selectedDateTime.trim()

    val onlyDate: String
    val onlyTime: String

    if (datetime.isNotEmpty()) {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(datetime)

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        onlyDate = dateFormat.format(date!!)
        onlyTime = timeFormat.format(date)
    } else {
        onlyDate = ""
        onlyTime = ""
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    selectedDateTime = dateTimeFormat.format(calendar.time)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    var categoryText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val filteredCategories = allCategories.filter {
        it.name.contains(categoryText, ignoreCase = true) && categoryText.isNotBlank()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 64.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Created:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    FloatingFrame(
                        modifier = Modifier
                            .padding(vertical = 0.dp, horizontal = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "",
                                fontSize = 20.sp,
                            )
                            Text(
                                "",
                                fontSize = 20.sp,
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Due To:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )

                    FloatingFrame(
                        modifier = Modifier
                            .padding(vertical = 0.dp, horizontal = 16.dp)
                            .clickable{
                                datePickerDialog.show()
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (selectedDateTime.isNotEmpty()) {
                                Text(
                                    "$onlyDate",
                                    fontSize = 20.sp,
                                )
                                Text(
                                    "$onlyTime",
                                    fontSize = 20.sp,
                                )
                            } else {
                                Text(
                                    "Select",
                                    fontSize = 20.sp,
                                )
                                Text(
                                    "date & time",
                                    fontSize = 20.sp,
                                )
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 15.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Task name")

                    var text by remember { mutableStateOf("") }

                    TextField(
                        value = text,
                        onValueChange = {
                            text = it
                        },
                        placeholder = { Text("Enter task name", fontSize = 18.sp) },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            errorContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.LightGray
                        ),
                        modifier = Modifier
                            .background(Color.Transparent)
                            .fillMaxWidth()
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    Column() {
                        Text("Category")

                        TextField(
                            value = categoryText,
                            onValueChange = {
                                categoryText = it
                                expanded = it.isNotBlank()
                            },
                            placeholder = { Text("Select or create category", fontSize = 18.sp,
                                color = Color.Black) },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                errorContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.LightGray
                            ),
                            modifier = Modifier
                                .background(Color.Transparent)
                                .fillMaxWidth()
                        )
                        AnimatedVisibility(
                            visible = expanded && filteredCategories.isNotEmpty(),
                            enter = fadeIn(), exit = fadeOut()
                        ) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize()
                                    .background(Color.DarkGray),
                                shape = RoundedCornerShape(8.dp),
                                shadowElevation = 8.dp
                            ) {
                                LazyColumn {
                                    items(filteredCategories.size) { index ->
                                        val category = filteredCategories[index]
                                        Text(
                                            text = category.name,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    categoryText = category.name
                                                    expanded = false
                                                }
                                                .padding(12.dp),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(top = 15.dp)
            ) {

                Text("Task description")

                TextField(
                    value = description,
                    placeholder = { Text("Enter your task description", fontSize = 18.sp) },
                    onValueChange = { description = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.LightGray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    singleLine = false,
                )
            }
            Row() {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = if (notififationOn) 8.dp else 0.dp,
                ) {
                    Box(
                        modifier = Modifier
                            .background(color = Color(0xFFCECECE))
                            .padding(16.dp)
                            .clickable{
                                notififationOn = !notififationOn
                            }
                    ) {
                        if (notififationOn) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_notifications_24),
                                contentDescription = "attachment",
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_notifications_off_24),
                                contentDescription = "attachment",
                            )
                        }
                    }
                }
                Spacer(
                    modifier = Modifier
                        .width(32.dp)
                )
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 8.dp
                ) {
                    Box(
                        modifier = Modifier
                            .background(color = Color(0xFFCECECE))
                            .padding(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_attach_file_24),
                            contentDescription = "attachment",
                        )
                    }
                }
            }

            LazyColumn(

            ) {

            }

        }
        FloatingActionButton(
            onClick = {
                //todo Add task or modify task
                (context as? Activity)?.finish()
            },
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.TopStart)
                .size(32.dp),
            containerColor = Color(0xFFE0E0E0),
            contentColor = Color.Black
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "save and quit"
            )
        }
    }
}

