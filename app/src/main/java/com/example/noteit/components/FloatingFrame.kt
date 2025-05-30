package com.example.noteit.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FloatingFrame(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    elevation: Dp = 8.dp,
    backgroundColor: Color = Color(0xFFCECECE),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
){
    Surface(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        shape = shape,
        shadowElevation = elevation,
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FloatingFramePreview() {
    FloatingFrame(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(0.9f)
    ) {
        Text("Nagłówek", style = MaterialTheme.typography.titleMedium)
        Text("To jest przykładowa zawartość komponentu FloatingFrame.")
        Button(onClick = {}) {
            Text("Kliknij mnie")
        }
    }
}