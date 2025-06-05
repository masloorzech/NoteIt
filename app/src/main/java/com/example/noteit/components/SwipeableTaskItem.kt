package com.example.noteit.components

import android.graphics.RenderEffect
import android.graphics.Shader
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animate
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteit.data.model.Task
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun SwipeableTaskItem(
    task: Task,
    categoryMap: Map<Int, String>,
    selectedCategories: List<String>,
    onSwipeRight: () -> Unit,
    onSwipeLeft: () -> Unit,
    onClick: () -> Unit
) {
    val swipeThreshold = 300f
    var thresholdAcheived = remember { mutableStateOf(false) }
    val offset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val blurEffect = remember {
        RenderEffect
            .createBlurEffect(10f, 10f, Shader.TileMode.CLAMP)
            .asComposeRenderEffect()
    }

    val maxBlurRadius = 10f

    val blurRadius = if (!task.isDone) {
        (offset.value / 200f).coerceIn(0f, 1f) * maxBlurRadius
    } else {
        maxBlurRadius * (1f - (offset.value / 200f).coerceIn(0f, 1f))
    }

    val dynamicBlurEffect = remember(blurRadius) {
        RenderEffect.createBlurEffect(
            blurRadius,
            blurRadius,
            Shader.TileMode.CLAMP
        ).asComposeRenderEffect()
    }

    if (selectedCategories.isEmpty() || selectedCategories.contains(categoryMap[task.categoryId])) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(if (offset.value < 0){
                    Color(0x859F9F9F)
                } else if (offset.value > 0) {
                    if (!task.isDone) {
                        Color(0x8529FFA2)
                    }else{
                        Color(0x8529FFD4)
                    }
                }
                else{
                    Color.Transparent
                }, shape = RoundedCornerShape(16.dp))
            ) {

            Box(
                modifier = Modifier
                    .matchParentSize(),
                contentAlignment = if (offset.value > 0) Alignment.CenterStart else Alignment.CenterEnd
            ) {
                if (offset.value>0) {
                    if (task.isDone) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Rollback",
                            tint = Color.Black,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Done",
                            tint = Color.Black,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }else if (offset.value<0){
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Rollback",
                        tint = Color.Black,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }

            Surface(
                color = Color.Transparent,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .offset { IntOffset(offset.value.roundToInt(), 0) }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onHorizontalDrag = { _, dragAmount ->
                                scope.launch {
                                    val newOffset = (offset.value + dragAmount).coerceIn(-swipeThreshold*100, swipeThreshold*100)
                                    offset.snapTo(newOffset)
                                }
                            },
                            onDragEnd = {
                                scope.launch {
                                    if (offset.value > swipeThreshold*0.8) {
                                        thresholdAcheived.value = !thresholdAcheived.value
                                        offset.animateTo(
                                            targetValue = 0f,
                                            animationSpec = androidx.compose.animation.core.tween(durationMillis = 300)
                                        )
                                        onSwipeRight()
                                        offset.snapTo(0f)
                                    }else if (offset.value < -swipeThreshold*0.8){
                                        offset.animateTo(
                                            targetValue = 0f,
                                            animationSpec = androidx.compose.animation.core.tween(durationMillis = 300)
                                        )
                                        onSwipeLeft()
                                        offset.snapTo(0f)
                                    }
                                    else {
                                        offset.animateTo(
                                            targetValue = 0f,
                                            animationSpec = androidx.compose.animation.core.spring()
                                        )
                                    }
                                }
                            }
                        )
                    }
            ) {
                TaskPreview(
                    title = task.title,
                    category = categoryMap[task.categoryId] ?: "No category",
                    desctiption = task.description,
                    date = SimpleDateFormat(
                        "dd.MM.yyyy HH:mm",
                        Locale.getDefault()
                    ).format(task.dueAt),
                    attachmentFlag = task.attachmentId != null,
                    notificationFlag = task.hasNotification,
                    modifier = Modifier
                        .clickable { onClick() }
                        .graphicsLayer {
                            if (task.isDone == true) {
                                if (thresholdAcheived.value == true) {
                                    Modifier
                                } else {
                                    renderEffect = dynamicBlurEffect
                                }
                            }
                            if (task.isDone == false) {
                                if (thresholdAcheived.value == true) {
                                    renderEffect = blurEffect
                                } else {
                                    renderEffect = dynamicBlurEffect
                                }
                            }
                        }
                )
            }
        }
    }
}
