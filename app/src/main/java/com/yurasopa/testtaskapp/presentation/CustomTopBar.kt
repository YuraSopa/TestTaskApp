package com.yurasopa.testtaskapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yurasopa.testtaskapp.navigation.ScreenRoutes
import com.yurasopa.testtaskapp.utils.Typography

@Composable
fun CustomTopBar(currentRoute: String?) {
    val title = when (currentRoute) {
        ScreenRoutes.UsersScreen.route -> "Working with GET request"
        ScreenRoutes.SignUpScreen.route -> "Working with POST request"
        else -> ""
    }
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .height(56.dp)
            .background(androidx.compose.ui.graphics.Color.Yellow)
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center),
            text = title,
            style = Typography.heading1
        )
    }
}