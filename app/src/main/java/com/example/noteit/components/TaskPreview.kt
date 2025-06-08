package com.example.noteit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteit.R
import com.example.noteit.ui.theme.Manuale


@Composable
fun TaskPreview(
    title: String,
    category: String,
    desctiption: String,
    date: String,
    attachmentFlag: Boolean,
    notificationFlag: Boolean,
    modifier: Modifier = Modifier,
){

    var attachment by remember { mutableStateOf(attachmentFlag) }

    var notification by remember { mutableStateOf(notificationFlag) }

    FloatingFrame(modifier,
        elevation = 0.dp) {
        Row(){
            Column(){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(Modifier.weight(2f)) {
                        Text(
                            "$title",
                            fontSize = 24.sp,
                            fontWeight = Bold, style = TextStyle(fontFamily = Manuale),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Box(Modifier.weight(1f)) {
                        CategoryChip(category)
                    }

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
                    fontSize = 18.sp,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Justify,
                    style = TextStyle(fontFamily = Manuale))
                Spacer(Modifier
                    .height(10.dp))
                Text("$date",
                    fontSize = 16.sp
                ,style = TextStyle(fontFamily = Manuale))
            }
        }
    }
}

