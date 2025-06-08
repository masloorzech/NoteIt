package com.example.noteit.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import com.example.noteit.helpers.NotificationPreferences

@Composable
fun NotificationTimeSelector(
    context: Context,
    onTimeSelected: (Int) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    val timeOptions = listOf(5, 10, 15, 30, 60,120,240)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isExpanded) {
                Column(
                    modifier = Modifier.padding(8.dp)
                        .width(100.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    timeOptions.forEach { time ->
                        FloatingFrame(
                            modifier = Modifier
                                .clickable{
                                    NotificationPreferences.saveNotificationTime(context, time)
                                    onTimeSelected(time)
                                    isExpanded = false
                                }
                        ) {
                            Text("$time min")
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = { isExpanded = !isExpanded },
                containerColor = Color(0xFFCECECE),
                contentColor = Color.Black
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification Time"
                )
            }
        }
    }
}

