package com.example.noteit.screens

import android.app.AlertDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteit.components.FloatingFrame
import com.example.noteit.components.TaskPreview
import kotlin.String

@Composable
@Preview(showBackground = true)
fun MainScreen() {

    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
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
                items(6) {
                    FloatingFrame(
                    ) {
                        Text("Something")
                    }
                }
            }

            Box() {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(50) {
                        TaskPreview(
                            title = "TitleTitleTitle",
                            category = "important",
                            desctiption = "This task involves implementing a new user interface component that displays a task's title, category, and description in a clean and structured layout. The layout should be responsive and support dynamic text wrapping with proper alignment and visual balance.",
                            date = "31.07.2025 12:00"
                        )
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
            onClick = { /* TODO: Action */ },
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