package com.example.noteit.screens

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.Color
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.FileProvider
import com.example.noteit.R
import com.example.noteit.components.FloatingFrame
import com.example.noteit.data.model.Attachment
import com.example.noteit.data.model.Category
import com.example.noteit.data.model.Task
import com.example.noteit.data.viewModel.AttachmentViewModel
import com.example.noteit.data.viewModel.CategoryViewModel
import com.example.noteit.data.viewModel.TaskViewModel
import com.example.noteit.ui.theme.Manuale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
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

suspend fun copyFileToInternalStorage(
    context: Context,
    uri: Uri,
    customName: String
): File? = withContext(Dispatchers.IO) {
    try {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: return@withContext null

        val originalName = contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        } ?: "plik.tmp"

        val extension = originalName.substringAfterLast('.', "")
        val baseFileName = if (extension.isNotEmpty()) "$customName.$extension" else customName

        val attachmentsDir = File(context.filesDir, "attachments")
        if (!attachmentsDir.exists()) attachmentsDir.mkdir()

        val outputFile = generateUniqueFileName(attachmentsDir, baseFileName, extension)

        inputStream.use { input ->
            outputFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        outputFile
    } catch (e: Exception) {
        Log.e("AttachmentCopy", "Błąd kopiowania pliku", e)
        null
    }
}

private fun generateUniqueFileName(directory: File, fileName: String, extension: String): File {
    var outputFile = File(directory, fileName)
    var counter = 1

    while (outputFile.exists()) {
        val nameWithoutExtension = if (extension.isNotEmpty()) {
            fileName.substringBeforeLast(".$extension")
        } else {
            fileName
        }

        val newFileName = if (extension.isNotEmpty()) {
            "${nameWithoutExtension}_$counter.$extension"
        } else {
            "${nameWithoutExtension}_$counter"
        }

        outputFile = File(directory, newFileName)
        counter++
    }

    return outputFile
}

fun openFile(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, context.contentResolver.getType(uri))
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(intent)
}


@Composable
fun CreateTaskScreen(
    taskId: Int?,
    categoryViewModel: CategoryViewModel,
    taskViewModel: TaskViewModel,
    attachmentViewModel: AttachmentViewModel
) {

    val listState = rememberLazyListState()

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

    val existingAttachments = remember { mutableStateListOf<Attachment>() }

    var doneTask by remember { mutableStateOf(false) }

    LaunchedEffect(taskId, allCategories) {
        if (taskId != null) {
            val loadedTask = taskViewModel.getTaskById(taskId)
            if (loadedTask != null) {
                task = loadedTask
                categoryText = allCategories.find { it.id == task.categoryId }?.name ?: ""
                val attachmentsFromDb = attachmentViewModel.getAttachmentsForTask(taskId).firstOrNull() ?: emptyList()
                existingAttachments.clear()
                existingAttachments.addAll(attachmentsFromDb)
                doneTask = task.isDone
            }
        }
    }

    val newAttachments = remember { mutableStateListOf<Attachment>() }

    val scrollState = rememberScrollState()

    var notififationOn by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val calendar = Calendar.getInstance()

    var selectedDateTime by remember { mutableStateOf<Long?>(task.dueAt) }

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

    val coroutineScope = rememberCoroutineScope()

    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var customFileName by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                selectedUri = it
                showRenameDialog = true
            }
        }
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
        if (showRenameDialog && selectedUri != null) {
            AlertDialog(
                onDismissRequest = {
                    showRenameDialog = false
                    selectedUri = null
                },
                title = { Text("Nazwij załącznik") },
                text = {
                    TextField(
                        value = customFileName,
                        onValueChange = { customFileName = it },
                        label = { Text("Nazwa pliku (bez rozszerzenia)") }
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        showRenameDialog = false

                        coroutineScope.launch {
                            selectedUri?.let { uri ->
                                val file = copyFileToInternalStorage(
                                    context,
                                    uri,
                                    customFileName
                                )
                                file?.let {
                                    val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"
                                    newAttachments.add(
                                        Attachment(
                                            taskId = -1,
                                            filePath = file.absolutePath,
                                            mimeType = mimeType
                                        )
                                    )
                                }
                            }
                            customFileName = ""
                            selectedUri = null
                        }
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showRenameDialog = false
                        selectedUri = null
                        customFileName = ""
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
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
                    DateTimePanel(dateString, timeString)

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

                        DueToTimePanel(datePickerDialog = datePickerDialog, dateString, timeString)
                    } else {
                        DueToTimePanel(datePickerDialog = datePickerDialog, "", "")
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
                        placeholder = {
                            Text(
                                "Enter task name",
                                fontSize = 18.sp,
                                color = Color.Black,
                                style = TextStyle(fontFamily = Manuale)
                            )
                        },
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
                            placeholder = {
                                Text(
                                    "Select or create category", fontSize = 18.sp,
                                    color = Color.Black, style = TextStyle(fontFamily = Manuale)
                                )
                            },
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
                                    items(min(filteredCategories.size, 3)) { index ->
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
                                                fontFamily = Manuale
                                            )
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
                    placeholder = {
                        Text(
                            "Enter your task description",
                            fontSize = 18.sp,
                            color = Color.Black,
                            style = TextStyle(fontFamily = Manuale)
                        )
                    },
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
                            .clickable {
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
                            modifier = Modifier.clickable {
                                launcher.launch(arrayOf("*/*"))
                            }
                        )
                    }
                }
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = if (doneTask) 0.dp else 8.dp,
                ) {
                    Box(
                        modifier = Modifier
                            .background(color = Color(0xFFCECECE))
                            .padding(16.dp)
                            .clickable {
                                doneTask = !doneTask
                            }
                    ) {
                        if (doneTask) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_clear_24),
                                contentDescription = "attachment",
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_check_24),
                                contentDescription = "attachment",
                            )
                        }
                    }
                }
            }

            val allAttachments = existingAttachments + newAttachments
            Box() {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(allAttachments) { attachment ->
                        val file = File(attachment.filePath)
                        FloatingFrame {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_attach_file_24),
                                        contentDescription = "attachment"
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Box(
                                        Modifier
                                            .clickable {
                                                openFile(context, file)
                                            }
                                            .weight(1f)
                                    ) {
                                        Text(
                                            text = file.name,
                                            textDecoration = TextDecoration.Underline,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    }

                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.Red,
                                        modifier = Modifier
                                            .padding(start = 8.dp, end = 16.dp)
                                            .clickable{
                                                if (existingAttachments.contains(attachment)) {
                                                    attachmentViewModel.deleteAttachment(attachment)
                                                    existingAttachments.remove(attachment)
                                                } else if (newAttachments.contains(attachment)) {
                                                    newAttachments.remove(attachment)
                                                }
                                                val deleted = file.delete()
                                                if (!deleted) {
                                                    Log.w("Attachment", "Nie udało się usunąć pliku: ${file.absolutePath}")
                                                }
                                            }
                                    )
                                }
                            }
                        }
                    }
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
                coroutineScope.launch {
                    if (categoryText.isBlank()) {
                        (context as? Activity)?.finish()
                        return@launch
                    }

                    val existingCategory = allCategories.find {
                        it.name.trim().equals(categoryText.trim(), ignoreCase = true)
                    }

                    val categoryId = existingCategory?.id
                        ?: categoryViewModel.addCategoryAndReturnId(categoryText.trim()).toInt()

                    task.isDone = doneTask
                    task.categoryId = categoryId
                    task.hasNotification = notififationOn
                    if (newAttachments.isNotEmpty() || existingAttachments.isNotEmpty()){
                        task.hasAttachment = true
                    }
                    if (task.title.isNotBlank() && selectedDateTime != null && selectedDateTime != 0L) {
                        task.dueAt = selectedDateTime!!
                        val taskId = taskViewModel.insert(task)
                        coroutineScope.launch {
                            for (attachment in newAttachments) {
                                attachmentViewModel.addAttachmentSuspend(attachment.copy(taskId = taskId))
                            }
                            newAttachments.clear()
                        }

                    }
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

