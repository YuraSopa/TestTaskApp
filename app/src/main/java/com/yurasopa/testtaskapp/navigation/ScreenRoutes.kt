package com.yurasopa.testtaskapp.navigation

sealed class ScreenRoutes(val route: String) {
    object UsersScreen : ScreenRoutes(route = "Users_Screen")
    object SignUpScreen : ScreenRoutes(route = "SignUp_Screen")
}