package com.yurasopa.testtaskapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yurasopa.testtaskapp.presentation.SignUpScreen
import com.yurasopa.testtaskapp.presentation.UsersScreen

@Composable
fun NavigationGraph(
    modifier: Modifier,
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = ScreenRoutes.UsersScreen.route) {
        composable(route = ScreenRoutes.UsersScreen.route) {
            UsersScreen(navController = navController, modifier = modifier)
        }
        composable(route = ScreenRoutes.SignUpScreen.route) {
            SignUpScreen(navController = navController, modifier = modifier)
        }
    }
}