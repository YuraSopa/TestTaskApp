package com.yurasopa.testtaskapp.navigation

sealed class ScreenRoutes(val route: String) {
    object MainGraph : ScreenRoutes(route = "Main_Graph")
    object UsersScreen : ScreenRoutes(route = "Users_Screen")
    object SignUpScreen : ScreenRoutes(route = "SignUp_Screen")
    object NoInternetScreen : ScreenRoutes(route = "NoInternet_Screen")
    object SuccessScreen : ScreenRoutes(route = "Success_Screen")
}