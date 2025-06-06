package com.example.noteit.components

import android.graphics.RenderEffect
import android.graphics.Shader
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animate
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteit.data.model.Category
import com.example.noteit.data.model.Task
import com.example.noteit.ui.theme.Manuale
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun SwipeableCategoryItem(
    category: Category,
    selectedCategories : List<String>,
    onSwipeUp: () -> Unit,
    onClick: () -> Unit
) {
    val swipeThreshold = 70f
    val offset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val isSelected = selectedCategories.contains(category.name)

    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(
                color = if (offset.value < 0) Color(0x859F9F9F) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = if (isSelected) 0.dp else 8.dp,
            modifier = Modifier
                .offset { IntOffset(0, offset.value.roundToInt()) }
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onVerticalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (offset.value + dragAmount).coerceIn(
                                    -swipeThreshold-10,
                                    0f
                                )
                                offset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            scope.launch {
                                if (offset.value < -swipeThreshold) {
                                    onSwipeUp()
                                    offset.snapTo(0f)
                                } else {
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
            FloatingFrame(
                elevation = if (isSelected) 0.dp else 8.dp,
                modifier = Modifier.clickable{
                    onClick()
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                ) {
                    Text(text = category.name,
                        fontSize = 18.sp,style = TextStyle(fontFamily = Manuale))
                }
            }
        }
    }
}

