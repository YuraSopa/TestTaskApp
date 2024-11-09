package com.yurasopa.testtaskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yurasopa.testtaskapp.navigation.NavigationGraph
import com.yurasopa.testtaskapp.presentation.BottomBar
import com.yurasopa.testtaskapp.presentation.CustomTopBar
import com.yurasopa.testtaskapp.ui.theme.TestTaskAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestTaskAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        CustomTopBar(currentRoute = currentRoute)
                    },
                    bottomBar = { BottomBar(navController = navController) }) { innerPadding ->
                    NavigationGraph(modifier = Modifier.padding(innerPadding), navController)

                }
            }
        }
    }
}