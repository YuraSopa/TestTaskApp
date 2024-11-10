package com.yurasopa.testtaskapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yurasopa.testtaskapp.R
import com.yurasopa.testtaskapp.utils.Typography

@Composable
fun NoInternetScreen(
    modifier: Modifier,
    onRetry: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.no_internet_image),
                contentDescription = "no internet connection"
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = "There is no internet connection", color = Color.Black)

            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Yellow,
                    contentColor = Color.Black
                ),
                onClick = {
                    onRetry()
                }) {
                Text(
                    text = "Try again",
                    style = Typography.body2,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

    }
}