package com.yurasopa.testtaskapp.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yurasopa.testtaskapp.utils.Typography

@Composable
fun CustomizedGeneralButton(
    title: String,
    onClick: () ->Unit
    ) {
    Button(
        modifier = Modifier
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Yellow,
            contentColor = Color.Black
        ),
        onClick = {
            onClick()
        }) {
        Text(
            text = title,
            style = Typography.body2,
            modifier = Modifier.padding(8.dp)
        )
    }
}