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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteit.CreateTaskActivity
import com.example.noteit.components.FloatingFrame
import com.example.noteit.components.SwipeableCategoryItem
import com.example.noteit.components.SwipeableTaskItem
import com.example.noteit.data.viewModel.CategoryViewModel
import com.example.noteit.data.viewModel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
@Preview(showBackground = true)
fun CreateTaskScreen() {

    val scrollState = rememberScrollState()

    var isNotificationEnabled by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var selectedDateTime by remember { mutableStateOf("") }
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

    var longText =""

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 64.dp)){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Created:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp)
                    FloatingFrame(
                        modifier = Modifier
                            .padding(vertical = 0.dp, horizontal = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally){
                            Text(
                                "12.02.2002",
                                fontSize = 20.sp,
                            )
                            Text(
                                "10:22",
                                fontSize = 20.sp,
                            )
                        }
                    }
                }
                Column( modifier = Modifier
                    .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Due To:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp)

                    FloatingFrame(
                        modifier = Modifier
                            .padding(vertical = 0.dp, horizontal = 16.dp)
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally) {
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
            Column(
                modifier = Modifier
                .padding(top = 15.dp)){
                Text("Notifications")
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                ) {
                    Box( modifier = Modifier
                        .padding(start = 4.dp)
                        .weight(1f)) {
                        Button(
                            onClick = { isNotificationEnabled = true },
                            enabled = !isNotificationEnabled,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("ON")
                        }
                    }
                    Box( modifier = Modifier
                        .padding(end = 4.dp)
                        .weight(1f)) {
                        Button(
                            onClick = { isNotificationEnabled = false },
                            enabled = isNotificationEnabled
                        ) {
                            Text("OFF")
                        }
                    }
                }
            }
            Column(modifier = Modifier
                .padding(top = 15.dp)){
                Text("Category")

                var text by remember { mutableStateOf("") }

                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Category") },
                    enabled = false
                )
                LazyColumn(){
                }
            }

            Column(modifier = Modifier
                .padding(top = 15.dp)){
                Text("Task name")

                var text by remember { mutableStateOf("") }

                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Category") },
                    enabled = false
                )
            }

            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(top = 15.dp)
            ) {

                Text("Task description")

                TextField(
                    value = longText,
                    onValueChange = { longText = it },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    singleLine = false,
                )
            }
            Text("Attachments:")
            LazyColumn(
                
            ) {

            }

        }
        FloatingActionButton(
            onClick = {
                (context as? Activity)?.finish()
            },
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.BottomCenter)
                .size(64.dp),
            containerColor = Color(0xFFE0E0E0),
            contentColor = Color.Black
        ) {
            Text("Save")
        }
        FloatingActionButton(
            onClick = {
                (context as? Activity)?.finish()
            },
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.BottomStart)
                .size(64.dp),
            containerColor = Color(0xFFE0E0E0),
            contentColor = Color.Black
        ) {
            Text("Discard")
        }
    }
}

