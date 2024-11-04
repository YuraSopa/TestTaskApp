package com.yurasopa.testtaskapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yurasopa.testtaskapp.navigation.BottomNavItem
import com.yurasopa.testtaskapp.utils.Typography

@Composable
fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val items = listOf(
        BottomNavItem.Users,
        BottomNavItem.SignUp
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Box(
        modifier = modifier
            .height(56.dp)
            .background(Color(0xFFF8F8F8))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    alwaysShowLabel = false,
                    selected = currentRoute == item.route,
                    icon = {
                        Row(Modifier.padding(8.dp)) {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.label
                        )
                            Text(
                                text = item.label, Modifier.padding(start = 8.dp),
                                style = Typography.body2
                            )
                        }
                    },
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpToId
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00BDD3),
                        selectedTextColor = Color.Blue,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}


    @Composable
    fun CustomNavigationBarItem(
        icon: Painter,
        label: String,
        selected: Boolean,
        onClick: () -> Unit
    ) {
        // Background color changes based on selection state
        val backgroundColor =
            if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Transparent
        val contentColor =
            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

        Surface(
            color = backgroundColor,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .clickable(onClick = onClick)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null, // Icon is decorative
                    tint = contentColor
                )
                Text(
                    text = label,
                    color = contentColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }