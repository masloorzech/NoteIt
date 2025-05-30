package com.example.noteit.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteit.R


@Composable
fun TaskPreview(
    title: String,
    category: String,
    desctiption: String,
    date: String
){

    var attachment by remember { mutableStateOf(true) }

    var notification by remember { mutableStateOf(true) }

    FloatingFrame {
        Row(){
            Column(){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text("$title",
                        fontSize = 24.sp,
                        fontWeight = Bold)

                    Spacer(Modifier
                        .weight(1f))
                    CategoryChip(category)

                    if (notification){
                        Icon(
                            painter = painterResource(id = R.drawable.outline_notifications_24),
                            contentDescription = "attachment",
                        )
                    }else{
                        Icon(
                            painter = painterResource(id = R.drawable.outline_notifications_off_24),
                            contentDescription = "attachment",
                        )
                    }
                    if (attachment){
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_attach_file_24),
                            contentDescription = "attachment",
                        )
                    }else{
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_attach_file_24),
                            contentDescription = "no attachment",
                            tint = Color(0xFFCECECE)
                        )
                    }
                }
                Spacer(Modifier
                    .height(10.dp))
                Text("$desctiption",
                    maxLines = 2,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Justify)
                Spacer(Modifier
                    .height(10.dp))
                CategoryChip("$date")
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun TaskPreviewPreview(){

    TaskPreview(
        title = "Task name",
        category = "daily",
        desctiption = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean enim sapien, placerat ut blandit sit amet, condimentum a turpis. In in viverra enim, at blandit lorem. Nulla nisl arcu, mattis non tellus sit amet, auctor vehicula est. Sed justo turpis, dignissim et nisl sed, dictum volutpat justo. Aliquam elementum nulla lorem, quis ultrices libero efficitur eget. Quisque facilisis tellus non eros dignissim accumsan quis ac ante. Sed malesuada ligula eu eros ultrices interdum. Donec nulla neque, sodales id laoreet eget, aliquet posuere nisl. Phasellus facilisis varius tincidunt. Suspendisse nulla enim, aliquam ut sapien eu, posuere tristique est.",
        date = "29.05.2025 12:00"
    )

}