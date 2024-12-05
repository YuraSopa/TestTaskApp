package com.yurasopa.testtaskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yurasopa.testtaskapp.navigation.NavigationGraph
import com.yurasopa.testtaskapp.navigation.ScreenRoutes
import com.yurasopa.testtaskapp.presentation.BottomBar
import com.yurasopa.testtaskapp.presentation.CustomTopBar
import com.yurasopa.testtaskapp.ui.theme.TestTaskAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTaskAppTheme {
                val navController = rememberNavController()

                val noBarsRoutes = listOf(
                    ScreenRoutes.NoInternetScreen.route,
                    ScreenRoutes.SuccessScreen.route
                )

                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                Scaffold(
                    topBar = {
                        if (currentRoute !in noBarsRoutes) {
                            CustomTopBar(currentRoute = currentRoute)
                        }
                    },
                    bottomBar = {
                        if (currentRoute !in noBarsRoutes) {
                            BottomBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    NavigationGraph(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController
                    )
                }
            }
        }
    }
}