package com.yurasopa.testtaskapp.navigation

import androidx.annotation.DrawableRes
import com.yurasopa.testtaskapp.R

sealed class BottomNavItem(val route: String, @DrawableRes val icon: Int, val label: String) {
    object Users : BottomNavItem(ScreenRoutes.UsersScreen.route, R.drawable.users_icon, "Users")
    object SignUp : BottomNavItem(ScreenRoutes.SignUpScreen.route, R.drawable.person_add, "Sign up")
}