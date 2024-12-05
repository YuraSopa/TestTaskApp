package com.yurasopa.testtaskapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yurasopa.testtaskapp.presentation.MainViewModel
import com.yurasopa.testtaskapp.presentation.NoInternetScreen
import com.yurasopa.testtaskapp.presentation.SignUpScreen
import com.yurasopa.testtaskapp.presentation.SignUpViewModel
import com.yurasopa.testtaskapp.presentation.SuccessScreen
import com.yurasopa.testtaskapp.presentation.UsersScreen
import com.yurasopa.testtaskapp.presentation.UsersViewModel
import com.yurasopa.testtaskapp.system.notifier.RetryConnectionEvent

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
//    onRetry: () -> Unit
) {
    val viewModel = hiltViewModel<MainViewModel>()
    val hasInternet by viewModel.hasInternet.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (hasInternet) ScreenRoutes.MainGraph.route else ScreenRoutes.NoInternetScreen.route,
        modifier = modifier
    ) {

        navigation(
            startDestination = ScreenRoutes.UsersScreen.route,
            route = ScreenRoutes.MainGraph.route
        ) {
            composable(route = ScreenRoutes.UsersScreen.route) {
                val usersViewModel = hiltViewModel<UsersViewModel>()
                UsersScreen(viewModel = usersViewModel)
            }
            composable(route = ScreenRoutes.SignUpScreen.route) {

                val signUpViewModel = hiltViewModel<SignUpViewModel>()
                SignUpScreen(
                    navController = navController,
                    viewModel = signUpViewModel
                )
            }
        }

        // No Internet Screen
        composable(route = ScreenRoutes.NoInternetScreen.route) {
            NoInternetScreen(navController = navController, onRetry = {
                viewModel.sendEvent(
                    RetryConnectionEvent()
                )
            })
        }

        // Success Screen
        composable(route = ScreenRoutes.SuccessScreen.route) {
            SuccessScreen(onClick = {
                navController.navigate(route = ScreenRoutes.UsersScreen.route) {
                    popUpTo(0)
                }
            })
        }
    }
}