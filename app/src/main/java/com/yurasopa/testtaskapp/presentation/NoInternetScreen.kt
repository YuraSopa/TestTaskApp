package com.yurasopa.testtaskapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yurasopa.testtaskapp.R

@Composable
fun NoInternetScreen(
    navController: NavController,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
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
            CustomizedGeneralButton(title = "Try again", onClick = {
                onRetry()
                navController.popBackStack()
            })
        }

    }
}