package com.example.noteit.screens

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.noteit.R
import com.example.noteit.components.FloatingFrame
import com.example.noteit.data.model.Category
import com.example.noteit.data.model.Task
import com.example.noteit.data.viewModel.CategoryViewModel
import com.example.noteit.data.viewModel.TaskViewModel
import com.example.noteit.ui.theme.Manuale
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.min


fun convertDateTimeToTwoStrings(datetime: String): Pair<String, String> {
    val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
    return try {
        val date = formatter.parse(datetime)
        val datePart = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)
        val timePart = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
        Pair(datePart, timePart)
    } catch (e: Exception) {
        Pair("", "")
    }
}

@Composable
fun DateTimePanel(
    date : String,
    time : String
){
    FloatingFrame(
        modifier = Modifier
            .padding(vertical = 0.dp, horizontal = 16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(date,
                fontSize = 20.sp,
                style = TextStyle(fontFamily = Manuale)
            )
            Text(time,
                fontSize = 20.sp,
                style = TextStyle(fontFamily = Manuale)
            )
        }
    }
}

@Composable
fun DueToTimePanel(
    datePickerDialog: DatePickerDialog,
    date: String,
    time: String
){
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
            if (date.isNotEmpty()) {
                Text(
                    date,
                    fontSize = 20.sp,
                    style = TextStyle(fontFamily = Manuale)
                )
                Text(
                    time,
                    fontSize = 20.sp,
                    style = TextStyle(fontFamily = Manuale)
                )
            } else {
                Text(
                    "Select",
                    fontSize = 20.sp,
                    style = TextStyle(fontFamily = Manuale)
                )
                Text(
                    "date & time",
                    fontSize = 20.sp,
                    style = TextStyle(fontFamily = Manuale)
                )
            }
        }
    }
}


@Composable
fun CreateTaskScreen(
    taskId: Int?,
    categoryViewModel: CategoryViewModel,
    taskViewModel: TaskViewModel
) {

    var task by remember { mutableStateOf<Task>(
        Task(
            title = "",
            description = "",
            createdAt = System.currentTimeMillis(),
            dueAt = System.currentTimeMillis())
        )
    }

    val allCategories by categoryViewModel.categories.collectAsState()

    var categoryText by remember { mutableStateOf("") }

    var selectedDateTime by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(taskId, allCategories) {
        if (taskId != null) {
            val loadedTask = taskViewModel.getTaskById(taskId)
            if (loadedTask != null) {
                task = loadedTask
                categoryText = allCategories.find { it.id == task.categoryId }?.name ?: ""
            }
        }
    }


    val scrollState = rememberScrollState()

    var notififationOn by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val calendar = Calendar.getInstance()

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
                    selectedDateTime = calendar.timeInMillis
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

    var expanded by remember { mutableStateOf(false) }


    val filteredCategories = allCategories.filter {
        it.name.contains(categoryText, ignoreCase = true) && categoryText.isNotBlank()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFD9D9D9))
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
                        fontSize = 24.sp,
                        style = TextStyle(fontFamily = Manuale)
                    )
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    val dateString = dateFormat.format(Date(task.createdAt))

                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val timeString = timeFormat.format(Date(task.createdAt))
                    DateTimePanel(dateString,timeString)

                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Due To:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        style = TextStyle(fontFamily = Manuale)
                    )
                    if (selectedDateTime != null) {

                        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        val dateString = dateFormat.format(selectedDateTime)
                        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                        val timeString = timeFormat.format(selectedDateTime)

                        DueToTimePanel(datePickerDialog = datePickerDialog,dateString, timeString)
                    }else{
                        DueToTimePanel(datePickerDialog = datePickerDialog,"", "")
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
                    Text("Task name", style = TextStyle(fontFamily = Manuale))

                    TextField(
                        value = task.title,
                        onValueChange = {
                            task = task.copy(title = it)
                        },
                        placeholder = { Text("Enter task name", fontSize = 18.sp, color = Color.Black,style = TextStyle(fontFamily = Manuale)) },
                        singleLine = true,
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
                        textStyle = TextStyle(
                            fontFamily = Manuale,
                            fontSize = 28.sp
                        ),
                        modifier = Modifier
                            .background(Color.Transparent)
                            .fillMaxWidth()

                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    Column() {
                        Text("Category", style = TextStyle(fontFamily = Manuale))

                        TextField(
                            value = categoryText,
                            onValueChange = {
                                categoryText = it
                                expanded = it.isNotBlank()
                            },
                            textStyle = TextStyle(
                                fontFamily = Manuale,
                                fontSize = 28.sp
                            ),
                            placeholder = { Text("Select or create category", fontSize = 18.sp,
                                color = Color.Black, style = TextStyle(fontFamily = Manuale)) },
                            singleLine = true,
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
                            modifier = Modifier
                                .background(Color.Transparent)
                                .fillMaxWidth()
                        )
                        AnimatedVisibility(
                            visible = expanded && filteredCategories.isNotEmpty(),
                            enter = fadeIn(), exit = fadeOut()
                        ) {
                            Surface(
                                color = Color.Transparent,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize()
                                    .zIndex(1000f)
                                    .background(Color.Transparent),
                            ) {
                                LazyColumn()
                                {
                                    items(min(filteredCategories.size,3)) { index ->
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
                                            color = Color.Black,
                                            style = TextStyle(
                                                fontFamily = Manuale)
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

                Text("Task description", style = TextStyle(fontFamily = Manuale))

                TextField(
                    value = task.description,
                    placeholder = { Text("Enter your task description", fontSize = 18.sp, color = Color.Black, style = TextStyle(fontFamily = Manuale)) },
                    onValueChange = {
                        task = task.copy(description = it)
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
        val coroutineScope = rememberCoroutineScope()

        FloatingActionButton(
            onClick = {
                coroutineScope.launch {

                    val existingCategory = allCategories.find {
                        it.name.trim().equals(categoryText.trim(), ignoreCase = true)
                    }

                    val categoryId = existingCategory?.id ?: categoryViewModel.addCategoryAndReturnId(categoryText.trim()).toInt()

                    task.isDone = false
                    task.categoryId = categoryId
                    taskViewModel.insert(task)
                    (context as? Activity)?.finish()
                }
            },
            modifier = Modifier
                .padding(top = 48.dp, start = 16.dp)
                .align(Alignment.TopStart)
                .size(32.dp),
            containerColor = Color(0xFFD9D9D9),
            contentColor = Color.Black
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "save and quit"
            )
        }
    }
}

