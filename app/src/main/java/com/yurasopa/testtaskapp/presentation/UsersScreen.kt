package com.yurasopa.testtaskapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yurasopa.testtaskapp.data.remote.User
import com.yurasopa.testtaskapp.utils.Typography


@Composable
fun UsersScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: UsersViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()

    val users = viewModel.users.collectAsState()

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        items(users.value) { user ->
            UserBoxScreen(user)
        }
    }


    LaunchedEffect(key1 = listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.collect { lastVisibleIndex ->
            if (lastVisibleIndex == viewModel.users.value.size - 1) {
                viewModel.getUsers()
            }
        }
    }
}

@Composable
fun UserBoxScreen(user: User) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Row {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .align(Alignment.Top),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = user.photo,
                    contentDescription = user.name,
                    modifier = Modifier.size(60.dp)
                )
            }
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(text = user.name, style = Typography.heading1)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = user.position, style = Typography.body3, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = user.email, style = Typography.body2)
                Text(text = user.phone, style = Typography.body2)
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                )
            }

        }
    }
}